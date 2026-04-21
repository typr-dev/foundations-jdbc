package dev.typr.foundations.pg;

import dev.typr.foundations.Connection;
import dev.typr.foundations.SqlFunction;
import dev.typr.foundations.Transactor;
import dev.typr.foundations.connect.DatabaseConfig;
import dev.typr.foundations.connect.TransactionIsolation;

/**
 * A {@link Transactor} backed by PostgreSQL wire-protocol pipelining.
 *
 * <p>Use this type when your code requires PgPipe-specific features (pool metrics, isolation-level
 * transactions):
 *
 * <pre>{@code
 * TransactorPgPipe pool = PgPipelinePool.create(config, pipeConfig);
 * pool.connectionCount();        // pool metrics
 * pool.activeConnectionCount();  // pool metrics
 * op.transact(pool);             // works as Transactor
 * }</pre>
 */
public interface TransactorPgPipe extends Transactor {

  /** Execute a function within a transaction at the given isolation level. */
  <T> T transact(TransactionIsolation isolation, SqlFunction<Connection, T> fn);

  /**
   * Execute one or more SQL statements using PostgreSQL's simple query protocol.
   *
   * <p>Unlike the extended query protocol (Parse/Bind/Execute) which handles one statement per
   * call, the simple query protocol supports multiple semicolon-separated statements in a single
   * call. This is useful for multi-statement DDL like:
   *
   * <pre>{@code
   * pool.simpleExecute("CREATE TABLE foo (id int); CREATE INDEX idx ON foo (id)");
   * }</pre>
   *
   * @param sql one or more SQL statements separated by semicolons
   */
  void simpleExecute(String sql);

  // ========== Pool metrics ==========

  /** Total number of connections in the pool. */
  int connectionCount();

  /** Number of connections currently executing queries. */
  int activeConnectionCount();

  /** Number of healthy idle connections. */
  int idleConnectionCount();

  /** Total number of queries pending across all connections. */
  int totalPendingQueries();

  /** Number of connections currently reserved for transactions. */
  int reservedConnectionCount();

  /** Whether the pool has been closed. */
  boolean isClosed();

  // ========== Factories ==========

  /** Create a PgPipe transactor with default pipeline config. */
  static TransactorPgPipe create(DatabaseConfig config) {
    return PgPipelinePool.create(config);
  }

  /** Create a PgPipe transactor with custom pipeline config. */
  static TransactorPgPipe create(DatabaseConfig config, PgPipelineConfig pipelineConfig) {
    return PgPipelinePool.create(config, pipelineConfig);
  }
}
