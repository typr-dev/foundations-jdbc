package dev.typr.foundations.connect;

import java.util.HashMap;
import java.util.Map;

/**
 * SQLite database configuration with typed builder methods for the documented xerial sqlite-jdbc
 * driver properties.
 *
 * <p>SQLite is an embedded database with no client/server distinction. The {@link #path} is either
 * a filesystem location, {@code :memory:} for an in-memory database, or {@code
 * file::memory:?cache=shared} when multiple connections need to share an in-memory database.
 *
 * <p>Date/time storage is configurable via {@link Builder#dateClass(DateClass)} —
 * foundations-jdbc's {@code SqliteTypes} expects the default {@link DateClass#TEXT} (ISO-8601
 * text). If you change it, the matching read/write codecs will not round-trip correctly.
 *
 * @see <a href="https://github.com/xerial/sqlite-jdbc">xerial sqlite-jdbc</a>
 * @see <a href="https://www.sqlite.org/datatype3.html">SQLite Datatypes</a>
 */
public final class SqliteConfig implements DatabaseConfig {

  /** How DATE / DATETIME / TIMESTAMP / TIME columns are stored on disk. */
  public enum DateClass {
    /** ISO-8601 string ({@code yyyy-MM-dd HH:mm:ss.SSS}). Indexes alphabetically. The default. */
    TEXT,
    /** Unix epoch milliseconds (or seconds, see {@link DatePrecision}). */
    INTEGER,
    /** Julian day as REAL. */
    REAL
  }

  public enum DatePrecision {
    SECONDS,
    MILLISECONDS
  }

  private final String path;

  private final Boolean readOnly;
  private final Boolean foreignKeys;
  private final String journalMode;
  private final String synchronous;
  private final Long busyTimeoutMs;
  private final Long cacheSize;
  private final String tempStore;
  private final String encoding;
  private final Boolean enableLoadExtension;
  private final Boolean explicitReadonly;

  private final DateClass dateClass;
  private final DatePrecision datePrecision;
  private final String dateStringFormat;

  private final Map<String, String> extraProperties;

  private SqliteConfig(Builder b) {
    this.path = b.path;
    this.readOnly = b.readOnly;
    this.foreignKeys = b.foreignKeys;
    this.journalMode = b.journalMode;
    this.synchronous = b.synchronous;
    this.busyTimeoutMs = b.busyTimeoutMs;
    this.cacheSize = b.cacheSize;
    this.tempStore = b.tempStore;
    this.encoding = b.encoding;
    this.enableLoadExtension = b.enableLoadExtension;
    this.explicitReadonly = b.explicitReadonly;
    this.dateClass = b.dateClass;
    this.datePrecision = b.datePrecision;
    this.dateStringFormat = b.dateStringFormat;
    this.extraProperties = Map.copyOf(b.extraProperties);
  }

  /**
   * Build a config for an in-memory database. Each {@code DriverManager.getConnection} call to
   * {@code :memory:} opens an independent database, so this config sets {@link
   * #singleConnectionMode()} to {@code true} and the connection source reuses one connection.
   */
  public static Builder inMemory() {
    return new Builder(":memory:");
  }

  /**
   * Build a config for a shared in-memory database. Multiple connections see the same data via
   * {@code file::memory:?cache=shared}. Useful when the wrapper or tests need parallel
   * connections against an in-memory database.
   */
  public static Builder sharedInMemory() {
    return new Builder("file::memory:?cache=shared");
  }

  /** Build a config for a file-backed database. */
  public static Builder builder(String path) {
    return new Builder(path);
  }

  @Override
  public String jdbcUrl() {
    return "jdbc:sqlite:" + path;
  }

  @Override
  public String username() {
    return "";
  }

  @Override
  public String password() {
    return "";
  }

  @Override
  public DatabaseKind kind() {
    return DatabaseKind.SQLITE;
  }

  @Override
  public boolean singleConnectionMode() {
    return ":memory:".equals(path);
  }

  @Override
  public Map<String, String> driverProperties() {
    Map<String, String> props = new HashMap<>();

    if (readOnly != null) props.put("open_mode", readOnly ? "1" : "6");
    if (foreignKeys != null) props.put("foreign_keys", foreignKeys ? "true" : "false");
    if (journalMode != null) props.put("journal_mode", journalMode);
    if (synchronous != null) props.put("synchronous", synchronous);
    if (busyTimeoutMs != null) props.put("busy_timeout", busyTimeoutMs.toString());
    if (cacheSize != null) props.put("cache_size", cacheSize.toString());
    if (tempStore != null) props.put("temp_store", tempStore);
    if (encoding != null) props.put("encoding", encoding);
    if (enableLoadExtension != null)
      props.put("enable_load_extension", enableLoadExtension.toString());
    if (explicitReadonly != null)
      props.put("jdbc.explicit_readonly", explicitReadonly.toString());

    if (dateClass != null) props.put("date_class", dateClass.name().toLowerCase());
    if (datePrecision != null) props.put("date_precision", datePrecision.name().toLowerCase());
    if (dateStringFormat != null) props.put("date_string_format", dateStringFormat);

    props.putAll(extraProperties);
    return props;
  }

  /** Builder for {@link SqliteConfig}. */
  public static final class Builder {
    private final String path;

    private Boolean readOnly;
    private Boolean foreignKeys;
    private String journalMode;
    private String synchronous;
    private Long busyTimeoutMs;
    private Long cacheSize;
    private String tempStore;
    private String encoding;
    private Boolean enableLoadExtension;
    private Boolean explicitReadonly;

    private DateClass dateClass;
    private DatePrecision datePrecision;
    private String dateStringFormat;

    private final Map<String, String> extraProperties = new HashMap<>();

    private Builder(String path) {
      this.path = path;
    }

    /** Open in read-only mode. Maps to SQLITE_OPEN_READONLY. */
    public Builder readOnly(boolean readOnly) {
      this.readOnly = readOnly;
      return this;
    }

    /**
     * Enable foreign-key enforcement. SQLite has foreign keys off by default for backwards
     * compatibility — enable per connection.
     */
    public Builder foreignKeys(boolean foreignKeys) {
      this.foreignKeys = foreignKeys;
      return this;
    }

    /** Journal mode (DELETE, TRUNCATE, PERSIST, MEMORY, WAL, OFF). WAL recommended. */
    public Builder journalMode(String journalMode) {
      this.journalMode = journalMode;
      return this;
    }

    /** Synchronous mode (OFF, NORMAL, FULL, EXTRA). NORMAL recommended for WAL. */
    public Builder synchronous(String synchronous) {
      this.synchronous = synchronous;
      return this;
    }

    /** Busy-timeout in milliseconds. */
    public Builder busyTimeoutMs(long busyTimeoutMs) {
      this.busyTimeoutMs = busyTimeoutMs;
      return this;
    }

    /** Page-cache size; positive = pages, negative = kibibytes. */
    public Builder cacheSize(long cacheSize) {
      this.cacheSize = cacheSize;
      return this;
    }

    /** Temp-store backing (DEFAULT, FILE, MEMORY). */
    public Builder tempStore(String tempStore) {
      this.tempStore = tempStore;
      return this;
    }

    /** Database text encoding (UTF-8, UTF-16, UTF-16le, UTF-16be). UTF-8 default. */
    public Builder encoding(String encoding) {
      this.encoding = encoding;
      return this;
    }

    /** Allow {@code load_extension()}. Off by default for security. */
    public Builder enableLoadExtension(boolean enableLoadExtension) {
      this.enableLoadExtension = enableLoadExtension;
      return this;
    }

    /** Honour {@code Connection.setReadOnly} by switching the database to read-only. */
    public Builder explicitReadonly(boolean explicitReadonly) {
      this.explicitReadonly = explicitReadonly;
      return this;
    }

    /**
     * How DATE / DATETIME / TIMESTAMP / TIME columns are stored. The default {@code TEXT} (ISO-8601)
     * is what {@code SqliteTypes} expects.
     */
    public Builder dateClass(DateClass dateClass) {
      this.dateClass = dateClass;
      return this;
    }

    /** When {@link DateClass#INTEGER} is used, choose seconds or milliseconds. */
    public Builder datePrecision(DatePrecision datePrecision) {
      this.datePrecision = datePrecision;
      return this;
    }

    /** Custom pattern for {@link DateClass#TEXT}; default is {@code yyyy-MM-dd HH:mm:ss.SSS}. */
    public Builder dateStringFormat(String dateStringFormat) {
      this.dateStringFormat = dateStringFormat;
      return this;
    }

    /** Set an arbitrary driver property. */
    public Builder property(String key, String value) {
      this.extraProperties.put(key, value);
      return this;
    }

    public SqliteConfig build() {
      return new SqliteConfig(this);
    }
  }
}
