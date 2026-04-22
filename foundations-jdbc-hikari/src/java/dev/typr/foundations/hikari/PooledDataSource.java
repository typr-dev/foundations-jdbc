package dev.typr.foundations.hikari;

import com.zaxxer.hikari.HikariDataSource;
import dev.typr.foundations.DatabaseException;
import dev.typr.foundations.TransactorJdbc;
import dev.typr.foundations.connect.ConnectionSource;
import dev.typr.foundations.internal.TransactorJdbcImpl;
import java.io.Closeable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.function.Function;
import javax.sql.DataSource;

/**
 * A pooled connection source using HikariCP.
 *
 * <p>This class wraps a HikariDataSource and implements {@link ConnectionSource} for unified API
 * with {@link
 * dev.typr.foundations.connect.ConnectionSource#of(dev.typr.foundations.connect.DatabaseConfig)}.
 *
 * <p>Example usage:
 *
 * <pre>{@code
 * var ds = PooledDataSource.create(
 *     PgConfig.builder("localhost", 5432, "mydb", "user", "pass").build(),
 *     ConnectionSettings.builder()
 *         .transactionIsolation(TransactionIsolation.READ_UNCOMMITTED)
 *         .build(),
 *     PoolConfig.builder()
 *         .maximumPoolSize(20)
 *         .build());
 *
 * var tx = ds.transactor();
 * tx.execute(conn -> repo.selectAll(conn));
 * }</pre>
 */
public final class PooledDataSource implements ConnectionSource, Closeable {

  private final HikariDataSource dataSource;
  private final Function<SQLException, DatabaseException> exceptionMapper;

  PooledDataSource(
      HikariDataSource dataSource, Function<SQLException, DatabaseException> exceptionMapper) {
    this.dataSource = dataSource;
    this.exceptionMapper = exceptionMapper;
  }

  /**
   * Get the underlying HikariDataSource.
   *
   * @return the wrapped HikariDataSource
   */
  public HikariDataSource unwrap() {
    return dataSource;
  }

  /**
   * Get this as a standard JDBC DataSource.
   *
   * @return this as DataSource
   */
  public DataSource asDataSource() {
    return dataSource;
  }

  @Override
  public Connection getConnection() throws SQLException {
    return dataSource.getConnection();
  }

  @Override
  public TransactorJdbc transactor() {
    return TransactorJdbcImpl.create(this, exceptionMapper);
  }

  /**
   * Close the underlying connection pool.
   *
   * <p>This will close all connections in the pool and release resources.
   */
  @Override
  public void close() {
    dataSource.close();
  }

  /**
   * Check if the pool is closed.
   *
   * @return true if the pool has been closed
   */
  public boolean isClosed() {
    return dataSource.isClosed();
  }

  /**
   * Check if the pool is running (not suspended or closed).
   *
   * @return true if the pool is running
   */
  public boolean isRunning() {
    return dataSource.isRunning();
  }
}
