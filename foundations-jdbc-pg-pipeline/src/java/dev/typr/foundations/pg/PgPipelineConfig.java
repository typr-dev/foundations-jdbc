package dev.typr.foundations.pg;

import dev.typr.foundations.QueryListener;
import java.time.Duration;
import java.util.Optional;

public final class PgPipelineConfig {

  private final int connectionCount;
  private final int pipeliningLimit;
  private final Duration connectTimeout;
  private final Duration queryTimeout;
  private final int preparedStatementCacheSize;
  private final int maxTransactionConnections;
  private final int sendQueueCapacity;
  private final PgPipelineSslMode sslMode;
  private final String sslRootCert;
  private final String sslCert;
  private final String sslKey;
  private final Duration maxLifetime;
  private final Duration idleTimeout;
  private final Duration validationInterval;
  private final PgExhaustionStrategy exhaustionStrategy;
  private final int defaultFetchSize;
  private final int cursorPrefetchDepth;
  private final Duration keepaliveTime;
  private final String applicationName;
  private final QueryListener queryListener;
  private final Duration shutdownTimeout;
  private final Optional<String> connectionInitSql;

  private PgPipelineConfig(Builder builder) {
    this.connectionCount = builder.connectionCount;
    this.pipeliningLimit = builder.pipeliningLimit;
    this.connectTimeout = builder.connectTimeout;
    this.queryTimeout = builder.queryTimeout;
    this.preparedStatementCacheSize = builder.preparedStatementCacheSize;
    this.sendQueueCapacity = builder.sendQueueCapacity;
    this.sslMode = builder.sslMode;
    this.sslRootCert = builder.sslRootCert;
    this.sslCert = builder.sslCert;
    this.sslKey = builder.sslKey;
    this.maxLifetime = builder.maxLifetime;
    this.idleTimeout = builder.idleTimeout;
    this.validationInterval = builder.validationInterval;
    this.exhaustionStrategy = builder.exhaustionStrategy;
    this.defaultFetchSize = builder.defaultFetchSize;
    this.cursorPrefetchDepth = builder.cursorPrefetchDepth;
    this.keepaliveTime = builder.keepaliveTime;
    this.applicationName = builder.applicationName;
    this.queryListener = builder.queryListener;
    this.shutdownTimeout = builder.shutdownTimeout;
    this.connectionInitSql = builder.connectionInitSql;
    int maxTx = builder.maxTransactionConnections;
    if (maxTx < 0) {
      this.maxTransactionConnections = builder.connectionCount;
    } else {
      if (maxTx > builder.connectionCount) {
        throw new IllegalArgumentException(
            "maxTransactionConnections ("
                + maxTx
                + ") must not exceed connectionCount ("
                + builder.connectionCount
                + ")");
      }
      this.maxTransactionConnections = maxTx;
    }
  }

  public int connectionCount() {
    return connectionCount;
  }

  public int pipeliningLimit() {
    return pipeliningLimit;
  }

  public Duration connectTimeout() {
    return connectTimeout;
  }

  public Duration queryTimeout() {
    return queryTimeout;
  }

  public int preparedStatementCacheSize() {
    return preparedStatementCacheSize;
  }

  public int maxTransactionConnections() {
    return maxTransactionConnections;
  }

  public int sendQueueCapacity() {
    return sendQueueCapacity;
  }

  public PgPipelineSslMode sslMode() {
    return sslMode;
  }

  public String sslRootCert() {
    return sslRootCert;
  }

  public String sslCert() {
    return sslCert;
  }

  public String sslKey() {
    return sslKey;
  }

  public Duration maxLifetime() {
    return maxLifetime;
  }

  public Duration idleTimeout() {
    return idleTimeout;
  }

  public Duration validationInterval() {
    return validationInterval;
  }

  public PgExhaustionStrategy exhaustionStrategy() {
    return exhaustionStrategy;
  }

  public int defaultFetchSize() {
    return defaultFetchSize;
  }

  public int cursorPrefetchDepth() {
    return cursorPrefetchDepth;
  }

  public Duration keepaliveTime() {
    return keepaliveTime;
  }

  public String applicationName() {
    return applicationName;
  }

  public QueryListener queryListener() {
    return queryListener;
  }

  public Duration shutdownTimeout() {
    return shutdownTimeout;
  }

  public Optional<String> connectionInitSql() {
    return connectionInitSql;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private int connectionCount = 10;
    private int pipeliningLimit = 256;
    private Duration connectTimeout = Duration.ofSeconds(30);
    private Duration queryTimeout = Duration.ofSeconds(30);
    private int preparedStatementCacheSize = 256;
    private int maxTransactionConnections = -1;
    private int sendQueueCapacity = 1024;
    private PgPipelineSslMode sslMode = PgPipelineSslMode.DISABLE;
    private String sslRootCert;
    private String sslCert;
    private String sslKey;
    private Duration maxLifetime = Duration.ofMinutes(30);
    private Duration idleTimeout = Duration.ofMinutes(10);
    private Duration validationInterval = Duration.ofSeconds(30);
    private PgExhaustionStrategy exhaustionStrategy = PgExhaustionStrategy.BLOCK;
    private int defaultFetchSize = 1000;
    private int cursorPrefetchDepth = 8;
    private Duration keepaliveTime = Duration.ofSeconds(30);
    private String applicationName = "foundations-jdbc";
    private QueryListener queryListener = QueryListener.NOOP;
    private Duration shutdownTimeout = Duration.ofSeconds(5);
    private Optional<String> connectionInitSql = Optional.empty();

    private Builder() {}

    public Builder connectionCount(int connectionCount) {
      this.connectionCount = connectionCount;
      return this;
    }

    public Builder pipeliningLimit(int pipeliningLimit) {
      this.pipeliningLimit = pipeliningLimit;
      return this;
    }

    public Builder connectTimeout(Duration connectTimeout) {
      this.connectTimeout = connectTimeout;
      return this;
    }

    public Builder queryTimeout(Duration queryTimeout) {
      this.queryTimeout = queryTimeout;
      return this;
    }

    public Builder preparedStatementCacheSize(int preparedStatementCacheSize) {
      this.preparedStatementCacheSize = preparedStatementCacheSize;
      return this;
    }

    public Builder maxTransactionConnections(int maxTransactionConnections) {
      this.maxTransactionConnections = maxTransactionConnections;
      return this;
    }

    public Builder sendQueueCapacity(int sendQueueCapacity) {
      this.sendQueueCapacity = sendQueueCapacity;
      return this;
    }

    public Builder sslMode(PgPipelineSslMode sslMode) {
      this.sslMode = sslMode;
      return this;
    }

    public Builder sslRootCert(String sslRootCert) {
      this.sslRootCert = sslRootCert;
      return this;
    }

    public Builder sslCert(String sslCert) {
      this.sslCert = sslCert;
      return this;
    }

    public Builder sslKey(String sslKey) {
      this.sslKey = sslKey;
      return this;
    }

    public Builder maxLifetime(Duration maxLifetime) {
      this.maxLifetime = maxLifetime;
      return this;
    }

    public Builder idleTimeout(Duration idleTimeout) {
      this.idleTimeout = idleTimeout;
      return this;
    }

    public Builder validationInterval(Duration validationInterval) {
      this.validationInterval = validationInterval;
      return this;
    }

    public Builder exhaustionStrategy(PgExhaustionStrategy exhaustionStrategy) {
      this.exhaustionStrategy = exhaustionStrategy;
      return this;
    }

    public Builder defaultFetchSize(int defaultFetchSize) {
      this.defaultFetchSize = defaultFetchSize;
      return this;
    }

    public Builder cursorPrefetchDepth(int cursorPrefetchDepth) {
      this.cursorPrefetchDepth = cursorPrefetchDepth;
      return this;
    }

    public Builder keepaliveTime(Duration keepaliveTime) {
      this.keepaliveTime = keepaliveTime;
      return this;
    }

    public Builder applicationName(String applicationName) {
      this.applicationName = applicationName;
      return this;
    }

    public Builder queryListener(QueryListener queryListener) {
      this.queryListener = queryListener;
      return this;
    }

    public Builder shutdownTimeout(Duration shutdownTimeout) {
      this.shutdownTimeout = shutdownTimeout;
      return this;
    }

    public Builder connectionInitSql(String connectionInitSql) {
      this.connectionInitSql = Optional.of(connectionInitSql);
      return this;
    }

    public PgPipelineConfig build() {
      if (connectionCount <= 0)
        throw new IllegalArgumentException("connectionCount must be > 0, got " + connectionCount);
      if (pipeliningLimit <= 0)
        throw new IllegalArgumentException("pipeliningLimit must be > 0, got " + pipeliningLimit);
      if (sendQueueCapacity <= 0)
        throw new IllegalArgumentException(
            "sendQueueCapacity must be > 0, got " + sendQueueCapacity);
      if (preparedStatementCacheSize < 0)
        throw new IllegalArgumentException(
            "preparedStatementCacheSize must be >= 0, got " + preparedStatementCacheSize);
      if (maxTransactionConnections == 0)
        throw new IllegalArgumentException(
            "maxTransactionConnections must be > 0 (or -1 for default), got 0");
      if (maxTransactionConnections > connectionCount)
        throw new IllegalArgumentException(
            "maxTransactionConnections ("
                + maxTransactionConnections
                + ") must be <= connectionCount ("
                + connectionCount
                + ")");
      return new PgPipelineConfig(this);
    }
  }
}
