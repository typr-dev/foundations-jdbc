package dev.typr.foundations;

/**
 * A {@link Transactor} backed by JDBC, providing raw {@link java.sql.Connection} access as an
 * escape hatch.
 *
 * <p>Returned by {@link Transactor#create(dev.typr.foundations.connect.DatabaseConfig)} and {@link
 * dev.typr.foundations.spring.SpringTransactor#create(javax.sql.DataSource)}.
 *
 * <p>Use this type when your code needs raw JDBC access (e.g. migration tooling, vendor-specific
 * JDBC extensions):
 *
 * <pre>{@code
 * TransactorJdbc tx = Transactor.create(config);
 * tx.executeJdbc(conn -> {
 *     // raw JDBC operations
 *     return conn.getMetaData().getTables(...);
 * });
 * }</pre>
 */
public interface TransactorJdbc extends Transactor, RollbackCapable, AutoCloseable {

  /**
   * Execute a function with a raw JDBC connection. The connection is obtained from the underlying
   * pool, used within a transaction (autoCommit=false, commit on success, rollback on failure), and
   * returned to the pool.
   *
   * @param operation the function to execute with the connection
   * @return the result of the function
   * @throws DatabaseException if a database error occurs
   */
  <T> T executeJdbc(SqlFunction<java.sql.Connection, T> operation);

  /** Void variant of {@link #executeJdbc(SqlFunction)}. */
  default void executeJdbcVoid(SqlConsumer<java.sql.Connection> operation) {
    executeJdbc(
        conn -> {
          operation.apply(conn);
          return null;
        });
  }
}
