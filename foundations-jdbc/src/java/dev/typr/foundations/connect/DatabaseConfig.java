package dev.typr.foundations.connect;

import java.util.Map;

/**
 * Configuration for connecting to a database. Implemented by database-specific config classes
 * (PostgresConfig, MariaDbConfig, SqlServerConfig, etc.).
 *
 * <p>Each implementation provides typed builder methods for all documented JDBC driver properties.
 *
 * <p>Example:
 *
 * <pre>{@code
 * var config = PostgresConfig.builder("localhost", 5432, "mydb", "user", "pass")
 *     .sslmode(PgSslMode.REQUIRE)
 *     .build();
 *
 * var tx = Transactor.create(config, Transactor.testStrategy());
 * }</pre>
 */
public interface DatabaseConfig {

  /** Get the JDBC URL for this database configuration. */
  String jdbcUrl();

  /** Get the username for authentication. */
  String username();

  /** Get the password for authentication. */
  String password();

  /** Get the database kind (POSTGRESQL, MARIADB, etc.). */
  DatabaseKind kind();

  /**
   * Get all driver-specific properties (excluding user/password which are handled separately).
   * These are passed to the JDBC driver via DataSource properties or connection URL parameters.
   */
  Map<String, String> driverProperties();
}
