---
title: PgPipe Configuration
---

# PgPipe Configuration

All configuration is done through `PgPipelineConfig.builder()`. Every setting has a sensible default — you can start with no configuration at all.

## Quick Example

```java
var pool = PgPipelinePool.create(dbConfig,
    PgPipelineConfig.builder()
        .connectionCount(20)
        .pipeliningLimit(512)
        .queryTimeout(Duration.ofSeconds(10))
        .sslMode(PgPipelineSslMode.VERIFY_FULL)
        .sslRootCert("/etc/ssl/ca.pem")
        .applicationName("order-service")
        .queryListener(myListener)
        .build()
);
```

## Pool Sizing

| Setting | Type | Default | Description |
|---------|------|---------|-------------|
| `connectionCount` | int | 10 | Number of persistent connections to PostgreSQL. Each connection has its own sender/receiver thread pair (virtual threads). |
| `maxTransactionConnections` | int | = connectionCount | Maximum concurrent `transact()` calls. Transactions reserve a dedicated connection. Set lower than `connectionCount` to guarantee some connections remain available for non-transactional queries. Cannot exceed `connectionCount`. |

### Sizing Guidance

- **connectionCount**: Start with the number of CPU cores on your application server. PgPipe connections are lightweight (virtual threads), so the limiting factor is usually PostgreSQL's `max_connections`. Each PgPipe pool connection maps to one PostgreSQL backend process.
- **maxTransactionConnections**: If your workload is mostly reads via `transactRead()` (which doesn't reserve connections), you can set this well below `connectionCount`. If you see `PgPipelineException: Max transaction connections reached`, increase this value.

## Pipelining

| Setting | Type | Default | Description |
|---------|------|---------|-------------|
| `pipeliningLimit` | int | 256 | Maximum in-flight operations per connection. A semaphore blocks the sender when this limit is reached, providing backpressure. Higher values increase throughput but consume more memory for pending futures. |
| `sendQueueCapacity` | int | 1024 | Capacity of the internal send queue. Operations are enqueued here before the sender thread writes them to the socket. The queue provides backpressure — callers block when it's full. |

## Timeouts

| Setting | Type | Default | Description |
|---------|------|---------|-------------|
| `connectTimeout` | Duration | 30s | Maximum time to establish a TCP connection to PostgreSQL. Applies to initial pool creation and reconnection attempts. |
| `queryTimeout` | Duration | 30s | Maximum time to wait for a query response. Applied via `CompletableFuture.orTimeout()`. Also used as the timeout for waiting on connection reservation (`transact()`) and connection selection when all connections are busy. |
| `shutdownTimeout` | Duration | 5s | Maximum time to drain in-flight operations during `pool.close()`. After this timeout, connections are closed regardless of pending operations. |

## Connection Lifecycle

| Setting | Type | Default | Description |
|---------|------|---------|-------------|
| `maxLifetime` | Duration | 30min | Maximum age of a connection before it is replaced. Only idle connections (no pending operations) are replaced. Prevents issues with server-side connection limits, firewall timeouts, and DNS changes. |
| `idleTimeout` | Duration | 10min | Maximum idle time before a connection is replaced. Frees server resources when connections aren't being used. |
| `validationInterval` | Duration | 30s | How often the maintenance thread checks connection health, enforces max lifetime, and sends keepalive pings. |
| `keepaliveTime` | Duration | 30s | If a connection has been idle longer than this, the maintenance thread sends a validation ping (`SELECT 1`). If the ping fails, the connection is replaced. |
| `connectionInitSql` | String | (none) | SQL statement executed once after each connection is established. Useful for setting session parameters like `SET search_path TO myschema` or `SET statement_timeout TO 5000`. |

### Lifecycle Interaction

The maintenance thread runs every `validationInterval` and checks each non-reserved connection:

1. Is the connection closed or unhealthy? **Replace immediately.**
2. Has the connection exceeded `maxLifetime` and has no pending operations? **Replace.**
3. Has the connection been idle longer than `idleTimeout`? **Replace.**
4. Has the connection been idle longer than `keepaliveTime`? **Send a validation ping.** If the ping fails, replace.

## Caching

| Setting | Type | Default | Description |
|---------|------|---------|-------------|
| `preparedStatementCacheSize` | int | 256 | Maximum entries in the per-connection LRU prepared statement cache. Repeated SQL strings skip the Parse step entirely. Set to 0 to disable caching. |

SQL strings are also cached globally (across connections) for JDBC-to-PostgreSQL placeholder conversion (`?` to `$1`, `$2`, ...). This cache is unbounded but entries are small (string-to-string mappings).

## Streaming Cursors

| Setting | Type | Default | Description |
|---------|------|---------|-------------|
| `defaultFetchSize` | int | 1000 | Default number of rows per cursor batch when no explicit fetch size is provided in the streaming query. |
| `cursorPrefetchDepth` | int | 8 | Number of Execute messages sent ahead of consumption. After the initial batch arrives, PgPipe sends this many Execute requests proactively. This means after one round-trip of latency, subsequent batches arrive continuously without waiting. |

### Prefetch Tuning

With `cursorPrefetchDepth=8` and `defaultFetchSize=1000`:
- First batch: 1 round-trip to fetch 1000 rows
- While you consume the first batch, 8 more Execute requests are in flight
- Net effect: after the initial RTT, rows stream continuously at wire speed

Increase `cursorPrefetchDepth` if your row processing is faster than network latency. Decrease it if memory is a concern (each prefetched batch is held in memory).

## SSL/TLS

| Setting | Type | Default | Description |
|---------|------|---------|-------------|
| `sslMode` | PgPipelineSslMode | DISABLE | SSL mode. See table below. |
| `sslRootCert` | String | null | Path to PEM file containing trusted CA certificates. Required for `VERIFY_CA` and `VERIFY_FULL`. |
| `sslCert` | String | null | Path to PEM file containing the client certificate. For mutual TLS. |
| `sslKey` | String | null | Path to PEM file containing the client private key (PKCS#8 format). For mutual TLS. Supports RSA and EC keys. |

| Mode | Encryption | Verifies Server Certificate | Verifies Hostname |
|------|-----------|---------------------------|-------------------|
| `DISABLE` | No | No | No |
| `REQUIRE` | Yes | No | No |
| `VERIFY_CA` | Yes | Against `sslRootCert` | No |
| `VERIFY_FULL` | Yes | Against `sslRootCert` | Yes (HTTPS algorithm) |

## Exhaustion Strategy

| Setting | Type | Default | Description |
|---------|------|---------|-------------|
| `exhaustionStrategy` | PgExhaustionStrategy | BLOCK | Behavior when all connections are busy or reserved. |

| Strategy | Behavior |
|----------|----------|
| `BLOCK` | Wait up to `queryTimeout` for a connection to become available. Suitable for most workloads — callers queue up rather than fail. |
| `THROW` | Throw `PgPipelineException` immediately. Suitable for latency-sensitive services where waiting is worse than failing — lets the caller shed load. |

## Observability

| Setting | Type | Default | Description |
|---------|------|---------|-------------|
| `applicationName` | String | "foundations-jdbc" | Sent to PostgreSQL during connection startup. Visible in `pg_stat_activity.application_name`. Set this to your service name. |
| `queryListener` | QueryListener | NOOP | Global listener for all queries executed through the pool. Receives `beforeQuery`, `afterQuery`, and `failedQuery` callbacks. Compose with per-operation listeners via `QueryListener.compose()`. |

## Defaults Summary

For quick reference, here are all defaults in one place:

```java
PgPipelineConfig.builder()
    // Pool
    .connectionCount(10)
    .maxTransactionConnections(10)  // = connectionCount

    // Pipelining
    .pipeliningLimit(256)
    .sendQueueCapacity(1024)

    // Timeouts
    .connectTimeout(Duration.ofSeconds(30))
    .queryTimeout(Duration.ofSeconds(30))
    .shutdownTimeout(Duration.ofSeconds(5))

    // Connection lifecycle
    .maxLifetime(Duration.ofMinutes(30))
    .idleTimeout(Duration.ofMinutes(10))
    .validationInterval(Duration.ofSeconds(30))
    .keepaliveTime(Duration.ofSeconds(30))

    // Caching
    .preparedStatementCacheSize(256)

    // Cursors
    .defaultFetchSize(1000)
    .cursorPrefetchDepth(8)

    // SSL
    .sslMode(PgPipelineSslMode.DISABLE)

    // Exhaustion
    .exhaustionStrategy(PgExhaustionStrategy.BLOCK)

    // Observability
    .applicationName("foundations-jdbc")
    .queryListener(QueryListener.NOOP)

    .build();
```
