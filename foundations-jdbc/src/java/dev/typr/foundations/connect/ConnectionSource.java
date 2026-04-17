package dev.typr.foundations.connect;

import dev.typr.foundations.Transactor;
import dev.typr.foundations.Transactor.Strategy;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;
import javax.sql.DataSource;

/**
 * A source of database connections with configured settings.
 *
 * <p>This interface abstracts over pooled and non-pooled connection sources, providing a unified
 * API for obtaining connections and transactors.
 *
 * <p>Extends {@link DataSource} so that any ConnectionSource can be used directly with frameworks
 * that expect a standard DataSource (e.g. Spring's {@code DataSourceTransactionManager}).
 *
 * <p>Use the {@link #of(DatabaseConfig)} factory for non-pooled connections via DriverManager. For
 * production use with connection pooling, use {@code HikariDataSourceFactory} from the
 * foundations-jdbc-hikari module.
 *
 * <p>Example usage:
 *
 * <pre>{@code
 * var ds = ConnectionSource.of(
 *     PgConfig.builder("localhost", 5432, "mydb", "user", "pass").build());
 *
 * var tx = ds.transactor(Transactor.testStrategy());
 * tx.execute(conn -> repo.selectAll(conn));
 * }</pre>
 */
public interface ConnectionSource extends DataSource {

  /**
   * Create a non-pooled connection source from a database configuration.
   *
   * <p>Connections are obtained via {@link java.sql.DriverManager} — suitable for scripts, tests,
   * and low-volume use cases. For production use with connection pooling, use {@code
   * HikariDataSourceFactory} from the foundations-jdbc-hikari module.
   *
   * <p>If the config requires single-connection mode (e.g. DuckDB in-memory), the returned source
   * automatically reuses a single connection across all callers.
   *
   * @param config database configuration
   * @return a ConnectionSource backed by DriverManager
   */
  static ConnectionSource of(DatabaseConfig config) {
    return SimpleDataSource.create(config);
  }

  /**
   * Create a non-pooled connection source with connection settings.
   *
   * @param config database configuration
   * @param settings connection settings to apply to each connection
   * @return a ConnectionSource backed by DriverManager
   * @see #of(DatabaseConfig)
   */
  static ConnectionSource of(DatabaseConfig config, ConnectionSettings settings) {
    return SimpleDataSource.create(config, settings);
  }

  /**
   * Get a connection from this source.
   *
   * @return a configured database connection
   * @throws SQLException if unable to get a connection
   */
  @Override
  Connection getConnection() throws SQLException;

  @Override
  default Connection getConnection(String username, String password) throws SQLException {
    throw new SQLFeatureNotSupportedException();
  }

  @Override
  default PrintWriter getLogWriter() {
    return null;
  }

  @Override
  default void setLogWriter(PrintWriter out) {}

  @Override
  default int getLoginTimeout() {
    return 0;
  }

  @Override
  default void setLoginTimeout(int seconds) {}

  @Override
  default Logger getParentLogger() throws SQLFeatureNotSupportedException {
    throw new SQLFeatureNotSupportedException();
  }

  @Override
  default <T> T unwrap(Class<T> iface) throws SQLException {
    if (iface.isInstance(this)) return iface.cast(this);
    throw new SQLException("Cannot unwrap to " + iface.getName());
  }

  @Override
  default boolean isWrapperFor(Class<?> iface) {
    return iface.isInstance(this);
  }

  /**
   * Create a Transactor with the default strategy (manual transactions with commit on success).
   *
   * @return a Transactor configured for manual transaction management
   */
  default Transactor transactor() {
    return transactor(Transactor.defaultStrategy());
  }

  /**
   * Create a Transactor with a custom strategy.
   *
   * @param strategy the transaction management strategy
   * @return a Transactor configured with the provided strategy
   */
  default Transactor transactor(Strategy strategy) {
    return new Transactor(this::getConnectionUnchecked, strategy);
  }

  private Connection getConnectionUnchecked() {
    try {
      return getConnection();
    } catch (SQLException e) {
      throw new dev.typr.foundations.DatabaseException(e);
    }
  }
}
