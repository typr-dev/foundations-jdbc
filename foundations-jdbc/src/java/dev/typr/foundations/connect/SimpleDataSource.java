package dev.typr.foundations.connect;

import dev.typr.foundations.TransactorJdbc;
import dev.typr.foundations.internal.TransactorJdbcImpl;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Non-pooled connection source using DriverManager. Package-private — use {@link
 * ConnectionSource#of(DatabaseConfig)} as the public entry point.
 */
final class SimpleDataSource implements ConnectionSource {

  private final DatabaseConfig config;
  private final ConnectionSettings settings;

  private SimpleDataSource(DatabaseConfig config, ConnectionSettings settings) {
    this.config = config;
    this.settings = settings;
  }

  static ConnectionSource create(DatabaseConfig config, ConnectionSettings settings) {
    var simple = new SimpleDataSource(config, settings);
    if (config.singleConnectionMode()) {
      return new SingleConnection(simple);
    }
    return simple;
  }

  static ConnectionSource create(DatabaseConfig config) {
    return create(config, ConnectionSettings.EMPTY);
  }

  @Override
  public Connection getConnection() throws SQLException {
    Properties props = new Properties();
    props.setProperty("user", config.username());
    props.setProperty("password", config.password());
    config.driverProperties().forEach(props::setProperty);

    Connection conn = DriverManager.getConnection(config.jdbcUrl(), props);
    applySettings(conn);
    return conn;
  }

  private void applySettings(Connection conn) throws SQLException {
    if (settings.transactionIsolation() != null) {
      conn.setTransactionIsolation(settings.transactionIsolation().jdbcLevel());
    }
    if (settings.autoCommit() != null) {
      conn.setAutoCommit(settings.autoCommit());
    }
    if (settings.readOnly() != null) {
      conn.setReadOnly(settings.readOnly());
    }
    if (settings.catalog() != null) {
      conn.setCatalog(settings.catalog());
    }
    if (settings.schema() != null) {
      conn.setSchema(settings.schema());
    }
    if (settings.connectionInitSql() != null) {
      try (Statement stmt = conn.createStatement()) {
        stmt.execute(settings.connectionInitSql());
      }
    }
  }

  /** Get the database configuration. */
  public DatabaseConfig config() {
    return config;
  }

  /** Get the connection settings. */
  public ConnectionSettings settings() {
    return settings;
  }

  @Override
  public TransactorJdbc transactor() {
    return TransactorJdbcImpl.create(this, config::mapException);
  }

  /**
   * A connection source that lazily creates a single connection and reuses it for all callers. Each
   * call to {@link #getConnection()} returns a non-closing proxy around the same underlying
   * connection.
   */
  private static final class SingleConnection implements ConnectionSource {

    private final ConnectionSource underlying;
    private final ReentrantLock lock = new ReentrantLock();
    private Connection connection;

    SingleConnection(ConnectionSource underlying) {
      this.underlying = underlying;
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

    @Override
    public TransactorJdbc transactor() {
      return underlying.transactor();
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
}
