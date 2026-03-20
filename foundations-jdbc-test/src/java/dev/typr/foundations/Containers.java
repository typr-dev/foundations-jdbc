package dev.typr.foundations;

import dev.typr.foundations.connect.Db2Config;
import dev.typr.foundations.connect.MariaDbConfig;
import dev.typr.foundations.connect.OracleConfig;
import dev.typr.foundations.connect.PostgresConfig;
import dev.typr.foundations.connect.SqlServerConfig;
import dev.typr.foundations.connect.SqlServerEncrypt;
import dev.typr.foundations.hikari.HikariDataSourceFactory;
import dev.typr.foundations.hikari.PoolConfig;
import dev.typr.foundations.hikari.PooledDataSource;
import java.time.Duration;
import org.testcontainers.containers.Db2Container;
import org.testcontainers.containers.MSSQLServerContainer;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.oracle.OracleContainer;
import org.testcontainers.utility.DockerImageName;

/**
 * Lazily-initialized Testcontainers for database integration tests.
 *
 * <p>Containers are started on first access and shared across all tests in the JVM. They are
 * automatically stopped when the JVM exits.
 *
 * <p>Usage:
 *
 * <pre>{@code
 * var tx = Containers.postgresTransactor();
 * tx.execute(conn -> {
 *     // run tests
 * });
 * }</pre>
 */
public final class Containers {

  private Containers() {}

  // Lazy initialization holders - thread-safe singleton pattern
  private static class PostgresHolder {
    static final PostgreSQLContainer<?> INSTANCE;
    static final Transactor TRANSACTOR;

    static {
      INSTANCE =
          new PostgreSQLContainer<>(
                  DockerImageName.parse("pgvector/pgvector:pg16")
                      .asCompatibleSubstituteFor("postgres"))
              .withDatabaseName("test")
              .withUsername("postgres")
              .withPassword("password")
              .withInitScript("postgres-init.sql");
      INSTANCE.start();
      Runtime.getRuntime().addShutdownHook(new Thread(INSTANCE::stop));

      TRANSACTOR =
          PostgresConfig.builder(
                  INSTANCE.getHost(),
                  INSTANCE.getMappedPort(5432),
                  INSTANCE.getDatabaseName(),
                  INSTANCE.getUsername(),
                  INSTANCE.getPassword())
              .build()
              .transactor(Transactor.testStrategy());
    }
  }

  private static class MariaDbHolder {
    static final MariaDBContainer<?> INSTANCE;
    static final Transactor TRANSACTOR;

    static {
      INSTANCE =
          new MariaDBContainer<>(DockerImageName.parse("mariadb:11.7"))
              .withDatabaseName("typr")
              .withUsername("typr")
              .withPassword("password");
      INSTANCE.start();
      Runtime.getRuntime().addShutdownHook(new Thread(INSTANCE::stop));

      TRANSACTOR =
          MariaDbConfig.builder(
                  INSTANCE.getHost(),
                  INSTANCE.getMappedPort(3306),
                  INSTANCE.getDatabaseName(),
                  INSTANCE.getUsername(),
                  INSTANCE.getPassword())
              .build()
              .transactor(Transactor.testStrategy());
    }
  }

  private static class SqlServerHolder {
    static final MSSQLServerContainer<?> INSTANCE;
    static final Transactor TRANSACTOR;

    static {
      INSTANCE =
          new MSSQLServerContainer<>(
                  DockerImageName.parse("mcr.microsoft.com/mssql/server:2022-latest"))
              .withPassword("YourStrong@Passw0rd")
              .acceptLicense();
      INSTANCE.start();
      Runtime.getRuntime().addShutdownHook(new Thread(INSTANCE::stop));

      TRANSACTOR =
          SqlServerConfig.builder(
                  INSTANCE.getHost(),
                  INSTANCE.getMappedPort(1433),
                  "master",
                  INSTANCE.getUsername(),
                  INSTANCE.getPassword())
              .encrypt(SqlServerEncrypt.FALSE)
              .trustServerCertificate(true)
              .build()
              .transactor(Transactor.testStrategy());
    }
  }

  private static class OracleHolder {
    static final OracleContainer INSTANCE;
    static final OracleConfig CONFIG;
    static final PooledDataSource POOL;
    static final Transactor TRANSACTOR;

    static {
      // gvenzl/oracle-free uses PDB (pluggable database) which requires service name format
      // OracleContainer's default wait strategy has only 60s timeout - increase to 10 minutes
      INSTANCE =
          new OracleContainer(DockerImageName.parse("gvenzl/oracle-free:23-slim"))
              .withUsername("typr")
              .withPassword("typr_password")
              .withStartupTimeout(Duration.ofMinutes(10))
              .withSharedMemorySize(1024L * 1024L * 1024L);
      INSTANCE.start();
      Runtime.getRuntime().addShutdownHook(new Thread(INSTANCE::stop));

      // Use serviceName() for PDB - it generates: jdbc:oracle:thin:@//host:port/servicename
      // Without serviceName(), it generates SID format which doesn't work for PDBs
      CONFIG =
          OracleConfig.builder(
                  INSTANCE.getHost(),
                  INSTANCE.getMappedPort(1521),
                  INSTANCE.getDatabaseName(), // This is "freepdb1"
                  INSTANCE.getUsername(),
                  INSTANCE.getPassword())
              .serviceName(INSTANCE.getDatabaseName()) // Use service name format for PDB
              .build();

      // Oracle Free has limited connections, use a small pool
      var poolConfig =
          PoolConfig.builder().maximumPoolSize(5).connectionTimeout(Duration.ofMinutes(2)).build();
      POOL = HikariDataSourceFactory.create(CONFIG, poolConfig);
      TRANSACTOR = POOL.transactor(Transactor.testStrategy());
    }
  }

  private static class Db2Holder {
    static final Db2Container INSTANCE;
    static final Transactor TRANSACTOR;

    static {
      INSTANCE =
          new Db2Container(DockerImageName.parse("ibmcom/db2:latest"))
              .withDatabaseName("typr")
              .withUsername("db2inst1")
              .withPassword("password")
              .withStartupTimeout(Duration.ofMinutes(10))
              .acceptLicense();
      INSTANCE.start();
      Runtime.getRuntime().addShutdownHook(new Thread(INSTANCE::stop));

      TRANSACTOR =
          Db2Config.builder(
                  INSTANCE.getHost(),
                  INSTANCE.getMappedPort(50000),
                  INSTANCE.getDatabaseName(),
                  INSTANCE.getUsername(),
                  INSTANCE.getPassword())
              .build()
              .transactor(Transactor.testStrategy());
    }
  }

  /** Get a PostgreSQL Transactor (starts container on first access). */
  public static Transactor postgresTransactor() {
    return PostgresHolder.TRANSACTOR;
  }

  /** Get a MariaDB Transactor (starts container on first access). */
  public static Transactor mariadbTransactor() {
    return MariaDbHolder.TRANSACTOR;
  }

  /** Get a SQL Server Transactor (starts container on first access). */
  public static Transactor sqlserverTransactor() {
    return SqlServerHolder.TRANSACTOR;
  }

  /** Get an Oracle Transactor (starts container on first access). */
  public static Transactor oracleTransactor() {
    return OracleHolder.TRANSACTOR;
  }

  /** Get Oracle pooled data source (starts container on first access). */
  public static PooledDataSource oraclePool() {
    return OracleHolder.POOL;
  }

  /** Get Oracle config (starts container on first access). */
  public static OracleConfig oracleConfig() {
    return OracleHolder.CONFIG;
  }

  /** Get a DB2 Transactor (starts container on first access). */
  public static Transactor db2Transactor() {
    return Db2Holder.TRANSACTOR;
  }

  // Keep container accessors for direct access if needed
  public static PostgreSQLContainer<?> postgres() {
    return PostgresHolder.INSTANCE;
  }

  public static MariaDBContainer<?> mariadb() {
    return MariaDbHolder.INSTANCE;
  }

  public static MSSQLServerContainer<?> sqlserver() {
    return SqlServerHolder.INSTANCE;
  }

  public static OracleContainer oracle() {
    return OracleHolder.INSTANCE;
  }

  public static Db2Container db2() {
    return Db2Holder.INSTANCE;
  }
}
