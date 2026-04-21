package dev.typr.foundations.pg;

import dev.typr.foundations.*;
import dev.typr.foundations.connect.DatabaseConfig;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReferenceArray;

/**
 * A pipelined PostgreSQL connection pool that implements the PostgreSQL wire protocol directly.
 * Achieves wire-protocol pipelining without any external dependencies — uses only {@code
 * java.net.Socket} (or {@code javax.net.ssl.SSLSocket} when SSL is enabled).
 *
 * <p>Implements {@link Transactor} for full compatibility with the foundations-jdbc type system
 * ({@link Fragment}, {@link RowCodec}, {@link Operation}).
 *
 * <p>Connection lifecycle: dead connections are detected and replaced transparently. A background
 * maintenance thread validates connections, enforces max lifetime and idle timeout.
 */
public final class PgPipelinePool
    implements TransactorPgPipe, dev.typr.foundations.RollbackCapable, AutoCloseable {

  private final AtomicReferenceArray<PgPipelinedConnection> connections;
  private final ConcurrentHashMap<String, String> sqlCache = new ConcurrentHashMap<>();
  private final AtomicBoolean[] reserved;
  private final Semaphore transactionSemaphore;
  private final AtomicInteger roundRobin = new AtomicInteger();
  private final AtomicBoolean poolClosed = new AtomicBoolean(false);

  // Connection factory parameters — stored for reconnection
  private final String host;
  private final int port;
  private final String database;
  private final String user;
  private final String password;
  private final PgPipelineConfig config;
  private final QueryListener queryListener;

  private Thread maintenanceThread;
  private Thread shutdownHook;

  private PgPipelinePool(
      AtomicReferenceArray<PgPipelinedConnection> connections,
      int maxTransactionConnections,
      String host,
      int port,
      String database,
      String user,
      String password,
      PgPipelineConfig config) {
    this.connections = connections;
    this.reserved = new AtomicBoolean[connections.length()];
    for (int i = 0; i < connections.length(); i++) {
      this.reserved[i] = new AtomicBoolean(false);
    }
    this.transactionSemaphore = new Semaphore(maxTransactionConnections);
    this.host = host;
    this.port = port;
    this.database = database;
    this.user = user;
    this.password = password;
    this.config = config;
    this.queryListener = config.queryListener();
  }

  public static PgPipelinePool create(DatabaseConfig config) {
    return create(config, PgPipelineConfig.builder().build());
  }

  public static PgPipelinePool create(DatabaseConfig config, PgPipelineConfig pipelineConfig) {
    String jdbcUrl = config.jdbcUrl();
    JdbcUrlParts parts = parseJdbcUrl(jdbcUrl);

    AtomicReferenceArray<PgPipelinedConnection> conns =
        new AtomicReferenceArray<>(pipelineConfig.connectionCount());
    for (int i = 0; i < conns.length(); i++) {
      try {
        conns.set(
            i,
            PgPipelinedConnection.connect(
                parts.host,
                parts.port,
                parts.database,
                config.username(),
                config.password(),
                pipelineConfig));
      } catch (IOException e) {
        // Close already-opened connections
        for (int j = 0; j < i; j++) {
          PgPipelinedConnection c = conns.get(j);
          if (c != null) c.close();
        }
        throw new PgPipelineException("Failed to connect to PostgreSQL: " + e.getMessage(), e);
      }
    }

    PgPipelinePool pool =
        new PgPipelinePool(
            conns,
            pipelineConfig.maxTransactionConnections(),
            parts.host,
            parts.port,
            parts.database,
            config.username(),
            config.password(),
            pipelineConfig);
    for (int i = 0; i < conns.length(); i++) {
      final int idx = i;
      conns
          .get(i)
          .setOnFatalError(
              () ->
                  Thread.ofVirtual()
                      .name("pg-reconnect-" + idx)
                      .start(() -> pool.replaceConnection(idx)));
    }
    pool.startMaintenance();
    pool.shutdownHook = new Thread(pool::close, "pg-pipeline-shutdown");
    Runtime.getRuntime().addShutdownHook(pool.shutdownHook);
    return pool;
  }

  // ========== Convenience API ==========

  public <T> List<T> query(Fragment sql, RowCodec<T> codec) {
    return execute(sql.query(codec.all()));
  }

  public <T> Optional<T> queryFirst(Fragment sql, RowCodec<T> codec) {
    return execute(sql.query(codec.first()));
  }

  public int update(Fragment sql) {
    return execute(sql.update());
  }

  @Override
  public void simpleExecute(String sql) {
    PgPipelinedConnection conn = selectConnection();
    try {
      conn.submitSimpleQuery(sql).join();
    } catch (CompletionException e) {
      Throwable cause = e.getCause();
      if (cause instanceof RuntimeException re) throw re;
      throw new PgPipelineException(cause);
    }
  }

  // ========== OperationRunner factory ==========

  private OperationRunner createPooledRunner() {
    return new OperationRunner(
        new PgPipeOperationExecutor(this, this::selectConnection, false, sqlCache, config),
        CombineStrategy.PARALLEL,
        baseContext());
  }

  private OperationRunner createTransactionRunner(PgPipelinedConnection conn) {
    return new OperationRunner(
        new PgPipeOperationExecutor(this, () -> conn, true, sqlCache, config),
        CombineStrategy.PARALLEL,
        baseContext());
  }

  private InstrumentationContext baseContext() {
    if (queryListener == QueryListener.NOOP) return InstrumentationContext.EMPTY;
    return InstrumentationContext.EMPTY.withListener(queryListener);
  }

  // ========== Transactor ==========

  @Override
  public <T> T execute(Operation<T> op) {
    return createPooledRunner().run(op);
  }

  // ========== Transaction support ==========

  @Override
  public <T> T transact(SqlFunction<Connection, T> fn) {
    return doTransact(fn, "BEGIN", "COMMIT");
  }

  @Override
  public <T> T transactRollback(SqlFunction<Connection, T> fn) {
    return doTransact(fn, "BEGIN", "ROLLBACK");
  }

  public <T> T transact(
      dev.typr.foundations.connect.TransactionIsolation isolation, SqlFunction<Connection, T> fn) {
    return doTransact(fn, beginSql(isolation), "COMMIT");
  }

  private static final byte[][] NO_PARAMS = new byte[0][];

  private static String beginSql(dev.typr.foundations.connect.TransactionIsolation isolation) {
    return switch (isolation) {
      case READ_UNCOMMITTED -> "BEGIN ISOLATION LEVEL READ UNCOMMITTED";
      case READ_COMMITTED -> "BEGIN ISOLATION LEVEL READ COMMITTED";
      case REPEATABLE_READ -> "BEGIN ISOLATION LEVEL REPEATABLE READ";
      case SERIALIZABLE -> "BEGIN ISOLATION LEVEL SERIALIZABLE";
      case NONE -> "BEGIN";
    };
  }

  private <T> T doTransact(SqlFunction<Connection, T> fn, String beginSql, String successSql) {
    int connIdx = reserveConnectionIdx();
    PgPipelinedConnection conn = connections.get(connIdx);
    try {
      boolean[] begun = {false};
      conn.markBeginPending(beginSql);
      OperationRunner runner = createTransactionRunner(conn);
      Connection mc =
          new Connection() {
            @Override
            public <R> List<R> query(Fragment sql, RowCodec<R> codec) {
              begun[0] = true;
              return runner.run(sql.query(codec.all()));
            }

            @Override
            public <R> Optional<R> queryFirst(Fragment sql, RowCodec<R> codec) {
              begun[0] = true;
              return runner.run(sql.query(codec.first()));
            }

            @Override
            public <R> R execute(OperationRead<R> op) {
              begun[0] = true;
              return runner.run(op);
            }

            @Override
            public <R> R execute(Operation<R> op) {
              begun[0] = true;
              return runner.run(op);
            }

            @Override
            public int update(Fragment sql) {
              begun[0] = true;
              return runner.run(sql.update());
            }

            @Override
            public java.sql.Connection unwrap() {
              throw new PgPipelineException(
                  "PgPipelinePool does not use JDBC connections. Use transact() on a Transactor for"
                      + " raw JDBC access.");
            }
          };
      T result = fn.apply(mc);
      if (!begun[0]) {
        conn.clearPendingBegin();
        return result;
      }
      conn.submit(successSql, NO_PARAMS, null).join();
      return result;
    } catch (Exception e) {
      try {
        conn.submit("ROLLBACK", NO_PARAMS, null).join();
      } catch (Exception re) {
        e.addSuppressed(re);
      }
      if (e instanceof RuntimeException re) throw re;
      throw new PgPipelineException(e);
    } finally {
      unreserveConnection(connIdx);
    }
  }

  @Override
  public <T> T transactRead(SqlFunction<ConnectionRead, T> fn) {
    OperationRunner runner = createPooledRunner();
    ConnectionRead rc =
        new ConnectionRead() {
          @Override
          public <R> R execute(OperationRead<R> op) {
            return runner.run(op);
          }

          @Override
          public <R> List<R> query(Fragment sql, RowCodec<R> codec) {
            return runner.run(sql.query(codec.all()));
          }

          @Override
          public <R> Optional<R> queryFirst(Fragment sql, RowCodec<R> codec) {
            return runner.run(sql.query(codec.first()));
          }
        };
    try {
      return fn.apply(rc);
    } catch (Exception e) {
      if (e instanceof RuntimeException re) throw re;
      throw new PgPipelineException(e);
    }
  }

  @Override
  public void close() {
    if (poolClosed.compareAndSet(false, true)) {
      if (shutdownHook != null) {
        try {
          Runtime.getRuntime().removeShutdownHook(shutdownHook);
        } catch (IllegalStateException ignored) {
        }
      }
      if (maintenanceThread != null) maintenanceThread.interrupt();

      // Drain: wait for in-flight operations to complete
      long deadlineNanos = System.nanoTime() + config.shutdownTimeout().toNanos();
      for (int i = 0; i < connections.length(); i++) {
        PgPipelinedConnection conn = connections.get(i);
        if (conn != null) {
          long remainingMs = (deadlineNanos - System.nanoTime()) / 1_000_000;
          if (remainingMs > 0 && conn.pendingCount() > 0) {
            conn.awaitDrain(remainingMs);
          }
          conn.close();
        }
      }
    }
  }

  // ========== Statement analysis (used by PgPipeQueryChecker) ==========

  PgPipelinedConnection.QueryResult analyzeStatement(String pgSql) {
    PgPipelinedConnection conn = selectConnection();
    try {
      return conn.submitAnalyze(pgSql).join();
    } catch (java.util.concurrent.CompletionException e) {
      Throwable cause = e.getCause();
      if (cause instanceof RuntimeException re) throw re;
      throw new PgPipelineException(cause);
    }
  }

  // ========== Pool metrics ==========

  public int connectionCount() {
    return connections.length();
  }

  public int activeConnectionCount() {
    int count = 0;
    for (int i = 0; i < connections.length(); i++) {
      PgPipelinedConnection conn = connections.get(i);
      if (conn != null && conn.pendingCount() > 0) count++;
    }
    return count;
  }

  public int idleConnectionCount() {
    int count = 0;
    for (int i = 0; i < connections.length(); i++) {
      PgPipelinedConnection conn = connections.get(i);
      if (conn != null && conn.isHealthy() && conn.pendingCount() == 0) count++;
    }
    return count;
  }

  public int totalPendingQueries() {
    int count = 0;
    for (int i = 0; i < connections.length(); i++) {
      PgPipelinedConnection conn = connections.get(i);
      if (conn != null) count += conn.pendingCount();
    }
    return count;
  }

  public int reservedConnectionCount() {
    int count = 0;
    for (AtomicBoolean r : reserved) {
      if (r.get()) count++;
    }
    return count;
  }

  public boolean isClosed() {
    return poolClosed.get();
  }

  // ========== Connection lifecycle ==========

  private void startMaintenance() {
    maintenanceThread =
        Thread.ofVirtual()
            .name("pg-pipeline-maintenance")
            .start(
                () -> {
                  while (!poolClosed.get()) {
                    try {
                      Thread.sleep(config.validationInterval());
                    } catch (InterruptedException e) {
                      return;
                    }
                    runMaintenance();
                  }
                });
  }

  private void runMaintenance() {
    long now = System.nanoTime();
    long maxLifetimeNanos = config.maxLifetime().toNanos();
    long idleTimeoutNanos = config.idleTimeout().toNanos();
    long keepaliveNanos = config.keepaliveTime().toNanos();
    for (int i = 0; i < connections.length(); i++) {
      if (reserved[i].get()) continue;

      PgPipelinedConnection conn = connections.get(i);
      if (conn == null || conn.isClosed()) {
        replaceConnection(i);
        continue;
      }

      if (shouldReplaceConnection(
          conn.createdAtNanos(),
          conn.lastActivityNanos(),
          conn.pendingCount(),
          maxLifetimeNanos,
          idleTimeoutNanos,
          now)) {
        replaceConnection(i);
        continue;
      }

      if (conn.pendingCount() == 0 && (now - conn.lastActivityNanos()) > keepaliveNanos) {
        if (!conn.validate()) {
          replaceConnection(i);
        }
      }
    }
  }

  enum ReplaceReason {
    NONE,
    MAX_LIFETIME,
    IDLE_TIMEOUT
  }

  static ReplaceReason checkReplaceReason(
      long createdAtNanos,
      long lastActivityNanos,
      int pendingCount,
      long maxLifetimeNanos,
      long idleTimeoutNanos,
      long nowNanos) {
    if (maxLifetimeNanos > 0
        && (nowNanos - createdAtNanos) > maxLifetimeNanos
        && pendingCount == 0) {
      return ReplaceReason.MAX_LIFETIME;
    }
    if (idleTimeoutNanos > 0
        && pendingCount == 0
        && (nowNanos - lastActivityNanos) > idleTimeoutNanos) {
      return ReplaceReason.IDLE_TIMEOUT;
    }
    return ReplaceReason.NONE;
  }

  private static boolean shouldReplaceConnection(
      long createdAtNanos,
      long lastActivityNanos,
      int pendingCount,
      long maxLifetimeNanos,
      long idleTimeoutNanos,
      long nowNanos) {
    return checkReplaceReason(
            createdAtNanos,
            lastActivityNanos,
            pendingCount,
            maxLifetimeNanos,
            idleTimeoutNanos,
            nowNanos)
        != ReplaceReason.NONE;
  }

  private PgPipelinedConnection replaceConnection(int index) {
    PgPipelinedConnection old = connections.get(index);
    try {
      PgPipelinedConnection fresh =
          PgPipelinedConnection.connect(host, port, database, user, password, config);
      fresh.setOnFatalError(
          () ->
              Thread.ofVirtual()
                  .name("pg-reconnect-" + index)
                  .start(() -> replaceConnection(index)));
      if (connections.compareAndSet(index, old, fresh)) {
        if (old != null) old.close();
        return fresh;
      } else {
        fresh.close();
        return connections.get(index);
      }
    } catch (Exception e) {
      // First attempt failed — schedule background retry with backoff
      Thread.ofVirtual()
          .name("pg-reconnect-backoff-" + index)
          .start(() -> replaceWithBackoff(index, old));
    }
    return old;
  }

  private void replaceWithBackoff(int index, PgPipelinedConnection old) {
    for (int attempt = 1; attempt <= 5; attempt++) {
      try {
        Thread.sleep(Math.min(1000L << (attempt - 1), 30000));
      } catch (InterruptedException ie) {
        Thread.currentThread().interrupt();
        return;
      }
      try {
        PgPipelinedConnection fresh =
            PgPipelinedConnection.connect(host, port, database, user, password, config);
        fresh.setOnFatalError(
            () ->
                Thread.ofVirtual()
                    .name("pg-reconnect-" + index)
                    .start(() -> replaceConnection(index)));
        if (connections.compareAndSet(index, old, fresh)) {
          if (old != null) old.close();
          return;
        } else {
          fresh.close();
          return;
        }
      } catch (Exception ignored) {
      }
    }
  }

  // ========== Connection selection ==========

  private PgPipelinedConnection selectConnection() {
    PgPipelinedConnection conn = trySelectConnection();
    if (conn != null) return conn;

    if (config.exhaustionStrategy() == PgExhaustionStrategy.THROW) {
      throw new PgPipelineException("Connection unavailable — all connections reserved");
    }

    long deadlineNanos = System.nanoTime() + config.queryTimeout().toNanos();
    while (System.nanoTime() < deadlineNanos) {
      try {
        Thread.sleep(10);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        throw new PgPipelineException("Interrupted while waiting for available connection");
      }
      conn = trySelectConnection();
      if (conn != null) return conn;
    }
    throw new PgPipelineException(
        "Connection unavailable — all connections reserved (timed out after "
            + config.queryTimeout().toMillis()
            + "ms)");
  }

  private PgPipelinedConnection trySelectConnection() {
    int len = connections.length();
    if (len == 1) {
      if (reserved[0].get()) return null;
      PgPipelinedConnection conn = connections.get(0);
      if (conn != null && conn.isHealthy()) return conn;
      PgPipelinedConnection replaced = replaceConnection(0);
      if (replaced != null && replaced.isHealthy()) return replaced;
      return null;
    }

    int start = (roundRobin.getAndIncrement() & 0x7FFF_FFFF) % len;
    for (int attempt = 0; attempt < len; attempt++) {
      int idx = (start + attempt) % len;
      if (reserved[idx].get()) continue;
      PgPipelinedConnection conn = connections.get(idx);
      if (conn != null && conn.isHealthy()) return conn;
      PgPipelinedConnection replaced = replaceConnection(idx);
      if (replaced != null && replaced.isHealthy()) return replaced;
    }
    return null;
  }

  int reserveConnectionIdx() {
    if (config.exhaustionStrategy() == PgExhaustionStrategy.THROW) {
      if (!transactionSemaphore.tryAcquire()) {
        throw new PgPipelineException("Max transaction connections reached");
      }
    } else {
      try {
        if (!transactionSemaphore.tryAcquire(
            config.queryTimeout().toMillis(), java.util.concurrent.TimeUnit.MILLISECONDS)) {
          throw new PgPipelineException(
              "Max transaction connections reached (timed out after "
                  + config.queryTimeout().toMillis()
                  + "ms)");
        }
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        throw new PgPipelineException("Interrupted while waiting for transaction connection");
      }
    }
    long deadlineNanos = System.nanoTime() + config.queryTimeout().toNanos();
    int len = connections.length();
    while (System.nanoTime() < deadlineNanos) {
      int start = (roundRobin.getAndIncrement() & 0x7FFF_FFFF) % len;
      for (int attempt = 0; attempt < len; attempt++) {
        int idx = (start + attempt) % len;
        if (reserved[idx].compareAndSet(false, true)) {
          PgPipelinedConnection conn = connections.get(idx);
          if (conn != null && conn.isHealthy()) return idx;
          PgPipelinedConnection replaced = replaceConnection(idx);
          if (replaced != null && replaced.isHealthy()) return idx;
          reserved[idx].set(false);
        }
      }
      try {
        Thread.sleep(1);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        break;
      }
    }
    transactionSemaphore.release();
    throw new PgPipelineException(
        "Failed to reserve a healthy connection for transaction (timed out)");
  }

  void unreserveConnection(int idx) {
    reserved[idx].set(false);
    transactionSemaphore.release();
  }

  PgPipelinedConnection getConnection(int index) {
    return connections.get(index);
  }

  // ========== JDBC URL parsing ==========

  record JdbcUrlParts(String host, int port, String database) {}

  static JdbcUrlParts parseJdbcUrl(String url) {
    String withoutPrefix = url;
    if (url.startsWith("jdbc:postgresql://")) {
      withoutPrefix = url.substring("jdbc:postgresql://".length());
    }

    int slashIdx = withoutPrefix.indexOf('/');
    String hostPort = slashIdx >= 0 ? withoutPrefix.substring(0, slashIdx) : withoutPrefix;
    String database = slashIdx >= 0 ? withoutPrefix.substring(slashIdx + 1) : "";

    int questionIdx = database.indexOf('?');
    if (questionIdx >= 0) {
      database = database.substring(0, questionIdx);
    }

    String host;
    int port;
    int colonIdx = hostPort.indexOf(':');
    if (colonIdx >= 0) {
      host = hostPort.substring(0, colonIdx);
      port = Integer.parseInt(hostPort.substring(colonIdx + 1));
    } else {
      host = hostPort;
      port = 5432;
    }

    return new JdbcUrlParts(host, port, database);
  }
}
