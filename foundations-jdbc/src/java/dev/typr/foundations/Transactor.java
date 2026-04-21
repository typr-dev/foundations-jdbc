package dev.typr.foundations;

import dev.typr.foundations.connect.ConnectionSettings;
import dev.typr.foundations.connect.ConnectionSource;
import dev.typr.foundations.connect.DatabaseConfig;
import dev.typr.foundations.internal.TransactorJdbcImpl;
import java.util.List;
import java.util.Optional;

/**
 * Unified entry point for database access. All database backends implement this interface: JDBC
 * pools, PgPipelinePool, etc.
 *
 * <p>Typically created via factory methods or backend-specific constructors:
 *
 * <pre>{@code
 * // JDBC
 * TransactorJdbc tx = Transactor.create(config);
 *
 * // PgPipelinePool (is already a Transactor)
 * var pool = PgPipelinePool.create(config, pipeConfig);
 * pool.execute(op);  // direct
 *
 * // Test mode (rollback on success)
 * var tx = Transactor.create(config).rollbackOnly();
 *
 * // With observability
 * var tx = Transactor.create(config).withListener(otelListener);
 * }</pre>
 */
public interface Transactor extends AutoCloseable {

  <T> T execute(Operation<T> op);

  /**
   * Execute a function within a transaction with full read/write access. The {@link Connection}
   * provides {@link Connection#execute(OperationRead)}, {@link Connection#update(Fragment)}, and
   * {@link Connection#unwrap()} for raw JDBC access.
   */
  <T> T transact(SqlFunction<Connection, T> fn);

  /**
   * Execute a function with a read-only connection. The {@link ConnectionRead} can only {@link
   * ConnectionRead#query query} and {@link ConnectionRead#queryFirst queryFirst} — no updates, no
   * raw JDBC access.
   *
   * <p>Implementations may optimize read-only transactions: skip BEGIN/COMMIT, distribute queries
   * across multiple connections, or route to read replicas.
   */
  <T> T transactRead(SqlFunction<ConnectionRead, T> fn);

  // ========== Convenience ==========

  /** Execute a query and return all rows. */
  default <T> List<T> query(Fragment sql, RowCodec<T> codec) {
    return execute(sql.query(codec.all()));
  }

  /** Execute a query and return the first row, or empty. */
  default <T> Optional<T> queryFirst(Fragment sql, RowCodec<T> codec) {
    return execute(sql.query(codec.first()));
  }

  /** Execute an update and return the affected row count. */
  default int update(Fragment sql) {
    return execute(sql.update());
  }

  // ========== Decoration ==========

  /** Return a transactor that rolls back on success (for tests). */
  default Transactor rollbackOnly() {
    if (this instanceof TransactorDecorator d) {
      return d.rollbackOnly();
    }
    return new TransactorDecorator(this, true, QueryListener.NOOP);
  }

  /** Return a transactor that instruments all queries with the given listener. */
  default Transactor withListener(QueryListener listener) {
    if (this instanceof TransactorDecorator d) {
      return d.withListener(listener);
    }
    return new TransactorDecorator(this, false, listener);
  }

  /** Return a transactor that adds the given listener alongside any existing listener. */
  default Transactor mergeListener(QueryListener other) {
    if (this instanceof TransactorDecorator d) {
      return d.mergeListener(other);
    }
    return withListener(other);
  }

  // ========== Factories ==========

  /** Create a JDBC-backed transactor from a database configuration. */
  static TransactorJdbc create(DatabaseConfig config) {
    return TransactorJdbcImpl.create(ConnectionSource.of(config), config::mapException);
  }

  /** Create a JDBC-backed transactor with connection settings. */
  static TransactorJdbc create(DatabaseConfig config, ConnectionSettings settings) {
    return TransactorJdbcImpl.create(ConnectionSource.of(config, settings), config::mapException);
  }

  @Override
  default void close() throws Exception {}
}
