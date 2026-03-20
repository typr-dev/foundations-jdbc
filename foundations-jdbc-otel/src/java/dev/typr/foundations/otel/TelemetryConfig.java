package dev.typr.foundations.otel;

import dev.typr.foundations.connect.DatabaseConfig;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Configuration for OpenTelemetry database instrumentation.
 *
 * <p>Example:
 *
 * <pre>{@code
 * var config = TelemetryConfig.builder(DatabaseSystem.POSTGRESQL)
 *     .serverAddress("localhost")
 *     .serverPort(5432)
 *     .dbNamespace("mydb")
 *     .build();
 * }</pre>
 */
public final class TelemetryConfig {

  private final DatabaseSystem dbSystem;
  private final Optional<String> serverAddress;
  private final Optional<Integer> serverPort;
  private final Optional<String> dbNamespace;
  private final boolean recordQueryText;

  private TelemetryConfig(Builder b) {
    this.dbSystem = b.dbSystem;
    this.serverAddress = Optional.ofNullable(b.serverAddress);
    this.serverPort = Optional.ofNullable(b.serverPort);
    this.dbNamespace = Optional.ofNullable(b.dbNamespace);
    this.recordQueryText = b.recordQueryText;
  }

  public DatabaseSystem dbSystem() {
    return dbSystem;
  }

  public Optional<String> serverAddress() {
    return serverAddress;
  }

  public Optional<Integer> serverPort() {
    return serverPort;
  }

  public Optional<String> dbNamespace() {
    return dbNamespace;
  }

  public boolean recordQueryText() {
    return recordQueryText;
  }

  /** Create a builder with a required database system. */
  public static Builder builder(DatabaseSystem dbSystem) {
    return new Builder(dbSystem);
  }

  /**
   * Create a builder that extracts defaults from a {@link DatabaseConfig}. Parses host, port, and
   * database name from the JDBC URL.
   */
  public static Builder builder(DatabaseConfig config) {
    DatabaseSystem system = DatabaseSystem.from(config.kind());
    Builder b = new Builder(system);
    parseJdbcUrl(config.jdbcUrl(), b);
    return b;
  }

  // Common JDBC URL patterns: jdbc:type://host:port/database
  private static final Pattern JDBC_URL_PATTERN =
      Pattern.compile("jdbc:[^:]+://([^/:?]+)(?::(\\d+))?(?:/([^?;]+))?");

  private static void parseJdbcUrl(String url, Builder b) {
    if (url == null) return;
    Matcher m = JDBC_URL_PATTERN.matcher(url);
    if (m.find()) {
      String host = m.group(1);
      if (host != null && !host.isEmpty()) {
        b.serverAddress(host);
      }
      String port = m.group(2);
      if (port != null) {
        try {
          b.serverPort(Integer.parseInt(port));
        } catch (NumberFormatException ignored) {
        }
      }
      String db = m.group(3);
      if (db != null && !db.isEmpty()) {
        b.dbNamespace(db);
      }
    }
  }

  public static final class Builder {
    private final DatabaseSystem dbSystem;
    private String serverAddress;
    private Integer serverPort;
    private String dbNamespace;
    private boolean recordQueryText = true;

    private Builder(DatabaseSystem dbSystem) {
      this.dbSystem = dbSystem;
    }

    public Builder serverAddress(String serverAddress) {
      this.serverAddress = serverAddress;
      return this;
    }

    public Builder serverPort(int serverPort) {
      this.serverPort = serverPort;
      return this;
    }

    public Builder dbNamespace(String dbNamespace) {
      this.dbNamespace = dbNamespace;
      return this;
    }

    public Builder recordQueryText(boolean recordQueryText) {
      this.recordQueryText = recordQueryText;
      return this;
    }

    public TelemetryConfig build() {
      return new TelemetryConfig(this);
    }
  }
}
