package dev.typr.foundations.pg;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

/**
 * A single PostgreSQL connection with wire-protocol pipelining support. Sends multiple
 * Parse+Bind+Execute+Sync sequences without waiting for responses, achieving true wire-protocol
 * pipelining.
 *
 * <p>Uses two virtual threads:
 *
 * <ul>
 *   <li>Sender: reads from submit queue, writes protocol messages to socket
 *   <li>Receiver: reads responses from socket, completes caller futures
 * </ul>
 */
final class PgPipelinedConnection {

  private final Socket socket;
  private final PgProtocol protocol;
  private final int pipeliningLimit;
  private final Semaphore pipelineSemaphore;
  private final int maxCacheSize;
  private final Duration queryTimeout;

  // Statement cache: SQL text → named statement name (access-order LRU via LinkedHashMap).
  // get() promotes entries to most-recently-used — do NOT replace with containsKey().
  private final LinkedHashMap<String, String> stmtCache;
  // Column descriptions: statement name → column descriptors (from RowDescription)
  private final ConcurrentHashMap<String, PgProtocol.ColumnDesc[]> descCache =
      new ConcurrentHashMap<>();
  // Evicted statement names pending Close on the wire
  private final java.util.concurrent.ConcurrentLinkedQueue<String> evictedStatements =
      new java.util.concurrent.ConcurrentLinkedQueue<>();
  private final AtomicInteger stmtCounter = new AtomicInteger(0);
  private final AtomicInteger portalCounter = new AtomicInteger(0);
  // Work queue: callers submit here, sender thread picks up. Bounded for backpressure.
  private final LinkedBlockingQueue<PipelineOp> sendQueue;
  // Items sent but not yet completed by receiver
  private final ConcurrentLinkedDeque<PendingOp> pendingResponses = new ConcurrentLinkedDeque<>();

  private final AtomicBoolean closed = new AtomicBoolean(false);
  private final AtomicReference<String> pendingBeginSql = new AtomicReference<>(null);
  private final AtomicInteger pendingCount = new AtomicInteger(0);
  private volatile Throwable fatalError;
  private volatile Runnable onFatalError;

  private final long createdAtNanos = System.nanoTime();
  private volatile long lastActivityNanos = System.nanoTime();

  private Thread senderThread;
  private Thread receiverThread;

  // Socket yield: receiver parks here when a cursor thread takes over direct socket I/O
  private final Object socketYieldLock = new Object();
  private volatile boolean receiverYielded;

  static final class QueryResult {
    final List<byte[][]> rawRows;
    final PgProtocol.ColumnDesc[] columns;
    final List<String> columnNames;
    final int affectedRows;
    final boolean portalSuspended;
    final int[] batchResults;
    final int[] paramOids;

    QueryResult(
        List<byte[][]> rawRows,
        PgProtocol.ColumnDesc[] columns,
        List<String> columnNames,
        int affectedRows,
        boolean portalSuspended) {
      this.rawRows = rawRows;
      this.columns = columns;
      this.columnNames = columnNames;
      this.affectedRows = affectedRows;
      this.portalSuspended = portalSuspended;
      this.batchResults = null;
      this.paramOids = null;
    }

    QueryResult(
        List<byte[][]> rawRows,
        PgProtocol.ColumnDesc[] columns,
        List<String> columnNames,
        int affectedRows,
        boolean portalSuspended,
        int[] paramOids) {
      this.rawRows = rawRows;
      this.columns = columns;
      this.columnNames = columnNames;
      this.affectedRows = affectedRows;
      this.portalSuspended = portalSuspended;
      this.batchResults = null;
      this.paramOids = paramOids;
    }

    QueryResult(int[] batchResults) {
      this.rawRows = List.of();
      this.columns = null;
      this.columnNames = List.of();
      this.affectedRows = 0;
      this.portalSuspended = false;
      this.batchResults = batchResults;
      this.paramOids = null;
    }
  }

  // ========== Pipeline operation types (sealed — each variant carries exactly its fields)
  // ==========

  sealed interface PipelineOp
      permits PipelineOp.Query,
          PipelineOp.CursorOpen,
          PipelineOp.Batch,
          PipelineOp.Copy,
          PipelineOp.SimpleQuery,
          PipelineOp.Analyze,
          PipelineOp.WithBegin {
    CompletableFuture<QueryResult> future();

    record Query(
        String sql,
        byte[][] paramValues,
        boolean firstParse,
        String stmtName,
        short[] resultFormats,
        CompletableFuture<QueryResult> future)
        implements PipelineOp {}

    record CursorOpen(
        String sql,
        byte[][] paramValues,
        boolean firstParse,
        String stmtName,
        String portalName,
        int maxRows,
        boolean yieldAfterComplete,
        short[] resultFormats,
        CompletableFuture<QueryResult> future)
        implements PipelineOp {}

    record Batch(
        String sql,
        boolean firstParse,
        String stmtName,
        List<byte[][]> paramValues,
        short[] resultFormats,
        CompletableFuture<QueryResult> future)
        implements PipelineOp {}

    record Copy(
        String sql,
        Iterator<byte[]> rowBytes,
        CompletableFuture<Void> copyReady,
        CompletableFuture<QueryResult> future)
        implements PipelineOp {}

    record SimpleQuery(String sql, CompletableFuture<QueryResult> future) implements PipelineOp {}

    record Analyze(String sql, String stmtName, CompletableFuture<QueryResult> future)
        implements PipelineOp {}

    record WithBegin(Query beginOp, PipelineOp inner) implements PipelineOp {
      @Override
      public CompletableFuture<QueryResult> future() {
        return inner.future();
      }
    }
  }

  // Mutable response accumulator — one per in-flight operation, owned by the receiver thread
  static final class ResponseState {
    PgProtocol.ColumnDesc[] columns;
    int[] paramOids;
    final List<byte[][]> rawRows = new ArrayList<>();
    String commandTag;
    dev.typr.foundations.PgError pgError;
    boolean portalSuspended;
    int[] batchResults;
    int batchCompletedCount;
  }

  // Pairs an immutable operation with its mutable response state
  static final class PendingOp {
    final PipelineOp op;
    final ResponseState state;

    PendingOp(PipelineOp op, ResponseState state) {
      this.op = op;
      this.state = state;
    }
  }

  private PgPipelinedConnection(
      Socket socket,
      PgProtocol protocol,
      int pipeliningLimit,
      int sendQueueCapacity,
      int maxCacheSize,
      Duration queryTimeout) {
    this.socket = socket;
    this.protocol = protocol;
    this.pipeliningLimit = pipeliningLimit;
    this.pipelineSemaphore = new Semaphore(pipeliningLimit);
    this.sendQueue = new LinkedBlockingQueue<>(sendQueueCapacity);
    this.maxCacheSize = maxCacheSize;
    this.queryTimeout = queryTimeout;
    this.stmtCache = new LinkedHashMap<>(16, 0.75f, true);
  }

  // ========== Connection factory ==========

  static PgPipelinedConnection connect(
      String host, int port, String database, String user, String password, PgPipelineConfig config)
      throws IOException {

    Socket socket = new Socket();
    socket.setTcpNoDelay(true);
    socket.setKeepAlive(true);
    int socketTimeoutMs = (int) config.queryTimeout().toMillis() + 5000;
    socket.setSoTimeout(socketTimeoutMs);
    socket.setReceiveBufferSize(65536);
    socket.setSendBufferSize(65536);
    socket.connect(new InetSocketAddress(host, port), (int) config.connectTimeout().toMillis());

    // SSL upgrade if requested
    Socket activeSocket = socket;
    if (config.sslMode() != PgPipelineSslMode.DISABLE) {
      PgProtocol.writeSSLRequest(socket.getOutputStream());
      boolean sslAccepted = PgProtocol.readSSLResponse(socket.getInputStream());
      if (!sslAccepted) {
        socket.close();
        throw new PgPipelineException(
            "Server does not support SSL but sslMode=" + config.sslMode());
      }
      try {
        SSLContext sslContext = PgSslContextFactory.createSslContext(config);
        SSLSocketFactory factory = sslContext.getSocketFactory();
        SSLSocket sslSocket = (SSLSocket) factory.createSocket(socket, host, port, true);
        if (config.sslMode() == PgPipelineSslMode.VERIFY_FULL) {
          SSLParameters params = sslSocket.getSSLParameters();
          params.setEndpointIdentificationAlgorithm("HTTPS");
          sslSocket.setSSLParameters(params);
        }
        sslSocket.startHandshake();
        activeSocket = sslSocket;
      } catch (Exception e) {
        socket.close();
        throw e;
      }
    }

    PgProtocol protocol =
        new PgProtocol(activeSocket.getInputStream(), activeSocket.getOutputStream());

    // Startup
    protocol.writeStartup(user, database, config.applicationName());
    protocol.flush();

    // Authentication loop
    authenticate(protocol, user, password);

    // Read until ReadyForQuery
    while (true) {
      PgProtocol.Message msg = protocol.readMessage();
      if (msg instanceof PgProtocol.Message.ReadyForQuery) break;
      if (msg instanceof PgProtocol.Message.ErrorResponse err) {
        activeSocket.close();
        throw new PgPipelineException(err.pgError().formatted());
      }
      // ParameterStatus, BackendKeyData — ignore
    }

    PgPipelinedConnection conn =
        new PgPipelinedConnection(
            activeSocket,
            protocol,
            config.pipeliningLimit(),
            config.sendQueueCapacity(),
            config.preparedStatementCacheSize(),
            config.queryTimeout());
    conn.startWorkers();

    config
        .connectionInitSql()
        .ifPresent(
            sql -> {
              try {
                conn.submit(sql, new byte[0][], null).join();
              } catch (Exception e) {
                conn.close();
                throw new PgPipelineException("Connection init failed: " + e.getMessage(), e);
              }
            });

    return conn;
  }

  private static void authenticate(PgProtocol protocol, String user, String password)
      throws IOException {
    PgProtocol.Message msg = protocol.readMessage();

    if (msg instanceof PgProtocol.Message.ErrorResponse err) {
      throw new PgPipelineException(err.pgError().formatted());
    }

    if (msg instanceof PgProtocol.Message.AuthenticationOk) {
      return;
    }

    if (msg instanceof PgProtocol.Message.AuthenticationCleartextPassword) {
      protocol.writePassword(password);
      protocol.flush();
      msg = protocol.readMessage();
      if (msg instanceof PgProtocol.Message.AuthenticationOk) return;
      throw new PgPipelineException("Authentication failed after cleartext password");
    }

    if (msg instanceof PgProtocol.Message.AuthenticationMD5(byte[] salt)) {
      String md5Pass = PgScramAuth.md5Password(user, password, salt);
      protocol.writePassword(md5Pass);
      protocol.flush();
      msg = protocol.readMessage();
      if (msg instanceof PgProtocol.Message.AuthenticationOk) return;
      throw new PgPipelineException("Authentication failed after MD5 password");
    }

    if (msg instanceof PgProtocol.Message.AuthenticationSASL(List<String> mechanisms)) {
      if (!mechanisms.contains("SCRAM-SHA-256")) {
        throw new PgPipelineException("Server requires unsupported SASL mechanism: " + mechanisms);
      }

      PgScramAuth scram = new PgScramAuth(user, password);

      // Step 1: client-first-message
      byte[] clientFirst = scram.clientFirstMessage();
      protocol.writeSASLInitialResponse("SCRAM-SHA-256", clientFirst);
      protocol.flush();

      // Step 2: server-first-message
      msg = protocol.readMessage();
      if (!(msg instanceof PgProtocol.Message.AuthenticationSASLContinue(byte[] data1))) {
        throw new PgPipelineException("Expected SASLContinue, got: " + msg);
      }

      // Step 3: client-final-message
      byte[] clientFinal = scram.clientFinalMessage(data1);
      protocol.writeSASLResponse(clientFinal);
      protocol.flush();

      // Step 4: server-final-message
      msg = protocol.readMessage();
      if (msg instanceof PgProtocol.Message.AuthenticationSASLFinal(byte[] data)) {
        scram.verifyServerFinal(data);
      } else {
        throw new PgPipelineException("Expected SASLFinal, got: " + msg);
      }

      // Step 5: AuthenticationOk
      msg = protocol.readMessage();
      if (msg instanceof PgProtocol.Message.AuthenticationOk) return;
      throw new PgPipelineException("Expected AuthenticationOk after SCRAM, got: " + msg);
    }

    throw new PgPipelineException("Unsupported authentication: " + msg);
  }

  // ========== Worker threads ==========

  private void startWorkers() {
    senderThread =
        Thread.ofVirtual()
            .name("pg-pipeline-sender")
            .start(
                () -> {
                  try {
                    senderLoop();
                  } catch (Exception e) {
                    handleFatalError(e);
                  }
                });

    receiverThread =
        Thread.ofVirtual()
            .name("pg-pipeline-receiver")
            .start(
                () -> {
                  try {
                    receiverLoop();
                  } catch (Exception e) {
                    handleFatalError(e);
                  }
                });
  }

  private void senderLoop() throws IOException, InterruptedException {
    List<PipelineOp> batch = new ArrayList<>();
    while (!closed.get()) {
      batch.clear();
      batch.add(sendQueue.take());
      sendQueue.drainTo(batch, pipeliningLimit - 1);
      if (closed.get()) {
        for (PipelineOp op : batch)
          op.future().completeExceptionally(new PgPipelineException("Connection closed"));
        return;
      }

      boolean needsFlush = false;
      for (int i = 0; i < batch.size(); i++) {
        PipelineOp op = batch.get(i);
        try {
          needsFlush = sendOp(op, needsFlush);
        } catch (IOException e) {
          failOp(op, e);
          for (int j = i + 1; j < batch.size(); j++) {
            failOp(batch.get(j), e);
          }
          throw e;
        }
      }
      String evicted;
      while ((evicted = evictedStatements.poll()) != null) {
        protocol.writeClose('S', evicted);
        needsFlush = true;
      }
      if (needsFlush) {
        protocol.flush();
      }
    }
  }

  private boolean sendOp(PipelineOp op, boolean needsFlush)
      throws IOException, InterruptedException {
    if (op instanceof PipelineOp.WithBegin wb) {
      sendOp(wb.beginOp(), needsFlush);
      boolean result = sendOp(wb.inner(), true);
      wb.beginOp()
          .future()
          .whenComplete(
              (r, ex) -> {
                if (ex != null) {
                  wb.inner()
                      .future()
                      .completeExceptionally(
                          new PgPipelineException("Transaction BEGIN failed: " + ex.getMessage()));
                }
              });
      return result;
    }

    ResponseState state = new ResponseState();
    if (!pipelineSemaphore.tryAcquire()) {
      protocol.flush();
      pipelineSemaphore.acquire();
    }
    pendingResponses.addLast(new PendingOp(op, state));
    pendingCount.incrementAndGet();

    switch (op) {
      case PipelineOp.Copy c -> {
        protocol.writeSimpleQuery(c.sql());
        protocol.flush();
        needsFlush = false;
        try {
          c.copyReady().join();
        } catch (CompletionException e) {
          return needsFlush;
        }
        while (c.rowBytes().hasNext()) {
          protocol.writeCopyData(c.rowBytes().next());
        }
        protocol.writeCopyDone();
        protocol.flush();
      }
      case PipelineOp.CursorOpen c -> {
        if (c.firstParse()) {
          protocol.writeParse(c.stmtName(), c.sql());
          protocol.writeDescribe('S', c.stmtName());
        }
        protocol.writeBind(c.portalName(), c.stmtName(), c.paramValues(), c.resultFormats());
        protocol.writeExecute(c.portalName(), c.maxRows());
        protocol.writeSync();
        needsFlush = true;
      }
      case PipelineOp.Batch b -> {
        state.batchResults = new int[b.paramValues().size()];
        if (b.firstParse()) {
          protocol.writeParse(b.stmtName(), b.sql());
          protocol.writeDescribe('S', b.stmtName());
        }
        byte[] stmtNameBytes = b.stmtName().getBytes(StandardCharsets.UTF_8);
        protocol.writeBatchBindExecute(stmtNameBytes, b.paramValues(), b.resultFormats());
        protocol.writeSync();
        needsFlush = true;
      }
      case PipelineOp.SimpleQuery sq -> {
        protocol.writeSimpleQuery(sq.sql());
        protocol.flush();
        needsFlush = false;
      }
      case PipelineOp.Query q -> {
        if (q.firstParse()) {
          protocol.writeParse(q.stmtName(), q.sql());
          protocol.writeDescribe('S', q.stmtName());
        }
        protocol.writeBind("", q.stmtName(), q.paramValues(), q.resultFormats());
        protocol.writeExecute("", 0);
        protocol.writeSync();
        needsFlush = true;
      }
      case PipelineOp.Analyze a -> {
        protocol.writeParse(a.stmtName(), a.sql());
        protocol.writeDescribe('S', a.stmtName());
        protocol.writeClose('S', a.stmtName());
        protocol.writeSync();
        needsFlush = true;
      }
      case PipelineOp.WithBegin wb -> throw new AssertionError("unreachable: " + wb);
    }
    return needsFlush;
  }

  private void receiverLoop() throws IOException {
    PendingOp current = null;

    while (!closed.get()) {
      if (receiverYielded) {
        synchronized (socketYieldLock) {
          while (receiverYielded && !closed.get()) {
            socketYieldLock.notifyAll();
            try {
              socketYieldLock.wait();
            } catch (InterruptedException e) {
              break;
            }
          }
        }
        continue;
      }

      PgProtocol.Message msg;
      try {
        msg = protocol.readMessage();
      } catch (java.net.SocketTimeoutException e) {
        if (pendingCount.get() > 0) {
          throw new IOException(
              "Socket read timed out with "
                  + pendingCount.get()
                  + " pending operations (network partition?)",
              e);
        }
        continue; // idle timeout — just loop
      }

      if (current == null) {
        current = pendingResponses.peekFirst();
        if (current == null) continue;
      }

      ResponseState s = current.state;

      switch (msg) {
        case PgProtocol.Message.ParseComplete ignored -> {}
        case PgProtocol.Message.ParameterDescription pd -> s.paramOids = pd.paramOids();

        case PgProtocol.Message.RowDescription rd -> {
          s.columns = rd.columns();
          switch (current.op) {
            case PipelineOp.Query q -> descCache.put(q.stmtName(), rd.columns());
            case PipelineOp.CursorOpen c -> descCache.put(c.stmtName(), rd.columns());
            case PipelineOp.Batch b -> descCache.put(b.stmtName(), rd.columns());
            case PipelineOp.Analyze ignored -> {}
            case PipelineOp.Copy ignored -> {}
            case PipelineOp.SimpleQuery ignored -> {}
            case PipelineOp.WithBegin ignored -> {}
          }
        }

        case PgProtocol.Message.NoData ignored -> {}
        case PgProtocol.Message.BindComplete ignored -> {}
        case PgProtocol.Message.DataRow dr -> s.rawRows.add(dr.values());
        case PgProtocol.Message.PortalSuspended ignored -> s.portalSuspended = true;
        case PgProtocol.Message.CloseComplete ignored -> {}

        case PgProtocol.Message.CommandComplete cc -> {
          if (current.op instanceof PipelineOp.Batch) {
            int idx = s.batchCompletedCount++;
            if (idx < s.batchResults.length) {
              s.batchResults[idx] = PgProtocol.parseAffectedRows(cc.tag());
            }
          } else {
            s.commandTag = cc.tag();
          }
        }

        case PgProtocol.Message.EmptyQuery ignored -> s.commandTag = "";

        case PgProtocol.Message.CopyInResponse ignored -> {
          if (current.op instanceof PipelineOp.Copy c) c.copyReady().complete(null);
        }

        case PgProtocol.Message.ErrorResponse err -> {
          if ("FATAL".equals(err.pgError().severity())) {
            handleFatalError(new PgPipelineException(err.pgError().formatted()));
            return;
          }
          s.pgError = err.pgError();
        }

        case PgProtocol.Message.ReadyForQuery ignored -> {
          completePendingOp(current);
          current = null;
        }

        case PgProtocol.Message.Notice ignored -> {}
        case PgProtocol.Message.ParameterStatus ignored -> {}
        default -> {}
      }
    }
  }

  private void completePendingOp(PendingOp pending) {
    pendingResponses.pollFirst();
    pendingCount.decrementAndGet();
    pipelineSemaphore.release();
    lastActivityNanos = System.nanoTime();

    ResponseState s = pending.state;
    PipelineOp op = pending.op;

    if (s.pgError != null) {
      invalidateStaleStatement(op, s.pgError.sqlState());
      String sql = extractSqlFromOp(op);
      var ex = new dev.typr.foundations.DatabaseException.Postgres(s.pgError, sql);
      if (op instanceof PipelineOp.Copy c && !c.copyReady().isDone()) {
        c.copyReady().completeExceptionally(ex);
      }
      op.future().completeExceptionally(ex);
    } else {
      switch (op) {
        case PipelineOp.Batch b -> op.future().complete(new QueryResult(s.batchResults));
        case PipelineOp.Query q -> completeWithRows(q.future(), q.stmtName(), s);
        case PipelineOp.CursorOpen c -> completeWithRows(c.future(), c.stmtName(), s);
        case PipelineOp.Copy c -> completeWithRows(c.future(), "", s);
        case PipelineOp.SimpleQuery sq ->
            sq.future().complete(new QueryResult(List.of(), null, List.of(), 0, false));
        case PipelineOp.Analyze a ->
            a.future()
                .complete(new QueryResult(List.of(), s.columns, List.of(), 0, false, s.paramOids));
        case PipelineOp.WithBegin ignored -> {}
      }
    }

    if (op instanceof PipelineOp.CursorOpen c && c.yieldAfterComplete() && s.portalSuspended) {
      receiverYielded = true;
    }
  }

  private static String extractSqlFromOp(PipelineOp op) {
    return switch (op) {
      case PipelineOp.Query q -> q.sql();
      case PipelineOp.CursorOpen c -> c.sql();
      case PipelineOp.Batch b -> b.sql();
      case PipelineOp.Copy c -> c.sql();
      case PipelineOp.SimpleQuery sq -> sq.sql();
      case PipelineOp.Analyze a -> a.sql();
      case PipelineOp.WithBegin w -> extractSqlFromOp(w.inner());
    };
  }

  private void completeWithRows(
      CompletableFuture<QueryResult> future, String stmtName, ResponseState s) {
    PgProtocol.ColumnDesc[] cols = s.columns;
    if (cols == null) cols = descCache.get(stmtName);

    List<String> columnNames;
    if (cols != null) {
      columnNames = new ArrayList<>(cols.length);
      for (PgProtocol.ColumnDesc col : cols) columnNames.add(col.name());
    } else {
      columnNames = List.of();
    }

    int affected = PgProtocol.parseAffectedRows(s.commandTag != null ? s.commandTag : "");
    future.complete(new QueryResult(s.rawRows, cols, columnNames, affected, s.portalSuspended));
  }

  private void invalidateStaleStatement(PipelineOp op, String sqlState) {
    if (!isStaleStatementError(sqlState)) return;
    switch (op) {
      case PipelineOp.Query q -> {
        synchronized (stmtCache) {
          stmtCache.remove(q.sql());
        }
        descCache.remove(q.stmtName());
      }
      case PipelineOp.CursorOpen c -> {
        synchronized (stmtCache) {
          stmtCache.remove(c.sql());
        }
        descCache.remove(c.stmtName());
      }
      case PipelineOp.Batch b -> {
        synchronized (stmtCache) {
          stmtCache.remove(b.sql());
        }
        descCache.remove(b.stmtName());
      }
      case PipelineOp.Analyze ignored -> {}
      case PipelineOp.Copy ignored -> {}
      case PipelineOp.SimpleQuery ignored -> {}
      case PipelineOp.WithBegin wb -> invalidateStaleStatement(wb.inner(), sqlState);
    }
  }

  // ========== Public API ==========

  CompletableFuture<QueryResult> submit(String sql, byte[][] paramValues, short[] resultFormats) {
    if (closed.get())
      return CompletableFuture.failedFuture(new PgPipelineException("Connection closed"));
    if (fatalError != null)
      return CompletableFuture.failedFuture(new PgPipelineException(fatalError));

    CompletableFuture<QueryResult> future = new CompletableFuture<>();
    synchronized (stmtCache) {
      String stmtName = stmtCache.get(sql);
      boolean firstParse = (stmtName == null);
      if (firstParse) {
        stmtName = "s" + stmtCounter.incrementAndGet();
        stmtCache.put(sql, stmtName);
        evictIfNeeded();
      }
      PipelineOp op =
          new PipelineOp.Query(sql, paramValues, firstParse, stmtName, resultFormats, future);
      enqueue(wrapBeginIfPending(op));
    }
    if (queryTimeout.toMillis() > 0)
      future.orTimeout(queryTimeout.toMillis(), TimeUnit.MILLISECONDS);
    return future;
  }

  CompletableFuture<QueryResult> submitBatch(
      String sql, List<byte[][]> allParamValues, short[] resultFormats) {
    if (closed.get())
      return CompletableFuture.failedFuture(new PgPipelineException("Connection closed"));
    if (fatalError != null)
      return CompletableFuture.failedFuture(new PgPipelineException(fatalError));

    CompletableFuture<QueryResult> future = new CompletableFuture<>();
    synchronized (stmtCache) {
      String stmtName = stmtCache.get(sql);
      boolean firstParse = (stmtName == null);
      if (firstParse) {
        stmtName = "s" + stmtCounter.incrementAndGet();
        stmtCache.put(sql, stmtName);
        evictIfNeeded();
      }
      PipelineOp op =
          new PipelineOp.Batch(sql, firstParse, stmtName, allParamValues, resultFormats, future);
      enqueue(wrapBeginIfPending(op));
    }
    if (queryTimeout.toMillis() > 0)
      future.orTimeout(queryTimeout.toMillis(), TimeUnit.MILLISECONDS);
    return future;
  }

  CompletableFuture<QueryResult> submitCopy(String copyCommand, Iterator<byte[]> rowBytes) {
    if (closed.get())
      return CompletableFuture.failedFuture(new PgPipelineException("Connection closed"));
    if (fatalError != null)
      return CompletableFuture.failedFuture(new PgPipelineException(fatalError));

    CompletableFuture<QueryResult> future = new CompletableFuture<>();
    enqueue(new PipelineOp.Copy(copyCommand, rowBytes, new CompletableFuture<>(), future));
    return future;
  }

  CompletableFuture<QueryResult> submitSimpleQuery(String sql) {
    if (closed.get())
      return CompletableFuture.failedFuture(new PgPipelineException("Connection closed"));
    if (fatalError != null)
      return CompletableFuture.failedFuture(new PgPipelineException(fatalError));

    CompletableFuture<QueryResult> future = new CompletableFuture<>();
    enqueue(new PipelineOp.SimpleQuery(sql, future));
    if (queryTimeout.toMillis() > 0)
      future.orTimeout(queryTimeout.toMillis(), TimeUnit.MILLISECONDS);
    return future;
  }

  CompletableFuture<QueryResult> submitAnalyze(String sql) {
    if (closed.get())
      return CompletableFuture.failedFuture(new PgPipelineException("Connection closed"));
    if (fatalError != null)
      return CompletableFuture.failedFuture(new PgPipelineException(fatalError));

    CompletableFuture<QueryResult> future = new CompletableFuture<>();
    String stmtName = "analyze_" + stmtCounter.incrementAndGet();
    enqueue(new PipelineOp.Analyze(sql, stmtName, future));
    if (queryTimeout.toMillis() > 0)
      future.orTimeout(queryTimeout.toMillis(), TimeUnit.MILLISECONDS);
    return future;
  }

  CompletableFuture<QueryResult> submitCursor(
      String sql, byte[][] paramValues, int fetchSize, short[] resultFormats) {
    if (closed.get())
      return CompletableFuture.failedFuture(new PgPipelineException("Connection closed"));
    if (fatalError != null)
      return CompletableFuture.failedFuture(new PgPipelineException(fatalError));

    CompletableFuture<QueryResult> future = new CompletableFuture<>();
    synchronized (stmtCache) {
      String stmtName = stmtCache.get(sql);
      boolean firstParse = (stmtName == null);
      if (firstParse) {
        stmtName = "s" + stmtCounter.incrementAndGet();
        stmtCache.put(sql, stmtName);
        evictIfNeeded();
      }
      String portalName = "p" + portalCounter.incrementAndGet();
      PipelineOp op =
          new PipelineOp.CursorOpen(
              sql,
              paramValues,
              firstParse,
              stmtName,
              portalName,
              fetchSize,
              true,
              resultFormats,
              future);
      enqueue(wrapBeginIfPending(op));
    }
    if (queryTimeout.toMillis() > 0)
      future.orTimeout(queryTimeout.toMillis(), TimeUnit.MILLISECONDS);
    return future;
  }

  void markBeginPending() {
    pendingBeginSql.set("BEGIN");
  }

  void markBeginPending(String beginSql) {
    pendingBeginSql.set(beginSql);
  }

  void clearPendingBegin() {
    pendingBeginSql.set(null);
  }

  private PipelineOp wrapBeginIfPending(PipelineOp inner) {
    String beginSql = pendingBeginSql.getAndSet(null);
    if (beginSql == null) return inner;
    String beginStmt = stmtCache.get(beginSql);
    boolean beginFirstParse = (beginStmt == null);
    if (beginFirstParse) {
      beginStmt = "s" + stmtCounter.incrementAndGet();
      stmtCache.put(beginSql, beginStmt);
      evictIfNeeded();
    }
    PipelineOp.Query beginOp =
        new PipelineOp.Query(
            beginSql, new byte[0][], beginFirstParse, beginStmt, null, new CompletableFuture<>());
    return new PipelineOp.WithBegin(beginOp, inner);
  }

  private void enqueue(PipelineOp op) {
    try {
      sendQueue.put(op);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      op.future().completeExceptionally(e);
    }
  }

  String lastPortalName() {
    return "p" + portalCounter.get();
  }

  // ========== Direct cursor I/O (socket owned by cursor thread) ==========

  /**
   * Wait for the receiver thread to yield the socket. Call after the initial submitCursor future
   * completes — the receiver will have set receiverYielded=true if the cursor has more batches.
   */
  void awaitReceiverYield() {
    synchronized (socketYieldLock) {
      while (!receiverYielded && !closed.get()) {
        try {
          socketYieldLock.wait();
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
          break;
        }
      }
    }
  }

  /** Write Execute+Flush for the next cursor batch (write-ahead). Does not read the response. */
  void directCursorSend(String portalName, int fetchSize) {
    try {
      protocol.writeExecute(portalName, fetchSize);
      protocol.writeFlush();
      protocol.flush();
    } catch (IOException e) {
      handleFatalError(e);
      throw new PgPipelineException(e);
    }
  }

  /** Read DataRows until PortalSuspended or CommandComplete. Caller must own the socket. */
  QueryResult directCursorReceive(int fetchSize) {
    try {
      List<byte[][]> rows = new ArrayList<>(fetchSize);
      boolean suspended = false;

      readLoop:
      while (true) {
        PgProtocol.Message msg = protocol.readMessage();
        switch (msg) {
          case PgProtocol.Message.DataRow dr -> rows.add(dr.values());
          case PgProtocol.Message.PortalSuspended ignored -> {
            suspended = true;
            break readLoop;
          }
          case PgProtocol.Message.CommandComplete cc -> {
            break readLoop;
          }
          case PgProtocol.Message.ErrorResponse err -> {
            throw new dev.typr.foundations.DatabaseException.Postgres(err.pgError(), null);
          }
          default -> {}
        }
      }

      lastActivityNanos = System.nanoTime();
      return new QueryResult(rows, null, List.of(), 0, suspended);
    } catch (IOException e) {
      handleFatalError(e);
      throw new PgPipelineException(e);
    }
  }

  /** Close a named portal directly and read until ReadyForQuery. Caller must own the socket. */
  void directCursorClose(String portalName) {
    try {
      protocol.writeClose('P', portalName);
      protocol.writeSync();
      protocol.flush();
      while (true) {
        PgProtocol.Message msg = protocol.readMessage();
        if (msg instanceof PgProtocol.Message.ReadyForQuery) break;
      }
      lastActivityNanos = System.nanoTime();
    } catch (IOException e) {
      handleFatalError(e);
      throw new PgPipelineException(e);
    }
  }

  /** Resume the receiver thread after direct cursor I/O is complete. */
  void resumeReceiver() {
    synchronized (socketYieldLock) {
      receiverYielded = false;
      socketYieldLock.notifyAll();
    }
  }

  private void evictIfNeeded() {
    // Called under synchronized(stmtCache)
    if (maxCacheSize > 0 && stmtCache.size() > maxCacheSize) {
      Iterator<Map.Entry<String, String>> it = stmtCache.entrySet().iterator();
      if (it.hasNext()) {
        Map.Entry<String, String> eldest = it.next();
        it.remove();
        String evictedStmtName = eldest.getValue();
        descCache.remove(evictedStmtName);
        evictedStatements.add(evictedStmtName);
      }
    }
  }

  boolean validate() {
    if (closed.get() || fatalError != null) return false;
    try {
      CompletableFuture<QueryResult> f = submit("SELECT 1", new byte[0][], null);
      f.get(5, TimeUnit.SECONDS);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  int pendingCount() {
    return pendingCount.get();
  }

  boolean isClosed() {
    return closed.get();
  }

  boolean isHealthy() {
    return !closed.get() && fatalError == null;
  }

  long createdAtNanos() {
    return createdAtNanos;
  }

  long lastActivityNanos() {
    return lastActivityNanos;
  }

  void awaitDrain(long timeoutMs) {
    long deadline = System.currentTimeMillis() + timeoutMs;
    while (pendingCount.get() > 0 && !closed.get()) {
      long remaining = deadline - System.currentTimeMillis();
      if (remaining <= 0) break;
      try {
        Thread.sleep(Math.min(remaining, 10));
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        break;
      }
    }
  }

  void close() {
    if (closed.compareAndSet(false, true)) {
      // Wake receiver if it's parked waiting for cursor thread to finish
      synchronized (socketYieldLock) {
        socketYieldLock.notifyAll();
      }

      if (senderThread != null) {
        senderThread.interrupt();
        try {
          senderThread.join(1000);
        } catch (InterruptedException ignored) {
          Thread.currentThread().interrupt();
        }
      }

      if (receiverThread != null) {
        receiverThread.interrupt();
        try {
          receiverThread.join(1000);
        } catch (InterruptedException ignored) {
          Thread.currentThread().interrupt();
        }
      }

      try {
        protocol.writeTerminate();
        protocol.flush();
      } catch (IOException ignored) {
      }

      try {
        socket.close();
      } catch (IOException ignored) {
      }

      PgPipelineException closedEx = new PgPipelineException("Connection closed");
      PendingOp pending;
      while ((pending = pendingResponses.pollFirst()) != null) {
        failOp(pending.op, closedEx);
      }
      PipelineOp queuedOp;
      while ((queuedOp = sendQueue.poll()) != null) {
        failOp(queuedOp, closedEx);
      }
    }
  }

  private static void failOp(PipelineOp op, Exception ex) {
    op.future().completeExceptionally(ex);
    if (op instanceof PipelineOp.WithBegin wb) {
      wb.beginOp().future().completeExceptionally(ex);
    }
    failCopyReady(op, ex);
  }

  private static void failCopyReady(PipelineOp op, Exception ex) {
    if (op instanceof PipelineOp.Copy c && !c.copyReady().isDone()) {
      c.copyReady().completeExceptionally(ex);
    }
    if (op instanceof PipelineOp.WithBegin wb) {
      failCopyReady(wb.inner(), ex);
    }
  }

  static boolean isStaleStatementError(String sqlState) {
    return "26000".equals(sqlState) || "42P05".equals(sqlState) || "0A000".equals(sqlState);
  }

  void setOnFatalError(Runnable callback) {
    this.onFatalError = callback;
  }

  private void handleFatalError(Exception e) {
    if (closed.get()) return;
    fatalError = e;
    PgPipelineException fatalEx = new PgPipelineException("Fatal error: " + e.getMessage(), e);
    PendingOp pending;
    while ((pending = pendingResponses.pollFirst()) != null) {
      failOp(pending.op, fatalEx);
    }
    PipelineOp queuedOp;
    while ((queuedOp = sendQueue.poll()) != null) {
      failOp(queuedOp, fatalEx);
    }
    Runnable callback = onFatalError;
    close();
    if (callback != null) callback.run();
  }
}
