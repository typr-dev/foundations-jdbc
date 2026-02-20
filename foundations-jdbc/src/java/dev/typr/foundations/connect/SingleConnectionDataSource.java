package dev.typr.foundations.connect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.locks.ReentrantLock;

/**
 * A connection source that lazily creates a single connection and reuses it for all callers.
 *
 * <p>Each call to {@link #getConnection()} returns a non-closing proxy around the same underlying
 * connection. This is useful for embedded databases like DuckDB in-memory mode, where each {@code
 * DriverManager.getConnection()} creates an independent database.
 *
 * <p>Example usage:
 *
 * <pre>{@code
 * var ds = SingleConnectionDataSource.create(DuckDbConfig.inMemory().build());
 * var tx = ds.transactor();
 * tx.execute(conn -> repo.selectAll(conn));
 * }</pre>
 */
public final class SingleConnectionDataSource implements ConnectionSource {

  private final ConnectionSource underlying;
  private final ReentrantLock lock = new ReentrantLock();
  private Connection connection;

  private SingleConnectionDataSource(ConnectionSource underlying) {
    this.underlying = underlying;
  }

  /**
   * Create a SingleConnectionDataSource from a database configuration.
   *
   * @param config database configuration
   * @return a new SingleConnectionDataSource
   */
  public static SingleConnectionDataSource create(DatabaseConfig config) {
    return new SingleConnectionDataSource(SimpleDataSource.create(config));
  }

  /**
   * Create a SingleConnectionDataSource from a database configuration with connection settings.
   *
   * @param config database configuration
   * @param settings connection settings to apply
   * @return a new SingleConnectionDataSource
   */
  public static SingleConnectionDataSource create(
      DatabaseConfig config, ConnectionSettings settings) {
    return new SingleConnectionDataSource(SimpleDataSource.create(config, settings));
  }

  @Override
  public Connection getConnection() throws SQLException {
    lock.lock();
    try {
      if (connection == null || connection.isClosed()) {
        connection = underlying.getConnection();
      }
      return nonClosingProxy(connection);
    } finally {
      lock.unlock();
    }
  }

  private static Connection nonClosingProxy(Connection real) {
    return (Connection)
        Proxy.newProxyInstance(
            Connection.class.getClassLoader(),
            new Class<?>[] {Connection.class},
            (proxy, method, args) -> {
              if ("close".equals(method.getName())) {
                return null;
              }
              try {
                return method.invoke(real, args);
              } catch (InvocationTargetException e) {
                throw e.getCause();
              }
            });
  }
}
