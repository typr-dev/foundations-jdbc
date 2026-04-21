---
title: PgPipe
---

import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

# PgPipe

PgPipe is a zero-dependency PostgreSQL connection pool that implements the wire protocol directly over raw sockets. Instead of going through JDBC, it speaks the protocol natively — which unlocks **wire-protocol pipelining**: sending multiple queries back-to-back without waiting for each response.

PgPipe implements `Transactor` (via `TransactorPgPipe`), so the entire foundations-jdbc API works unchanged — `Fragment`, `RowCodec`, `Operation`, `combine()`, `transact()`, streaming cursors, stored procedures. You swap the transactor, not your code.

:::warning Experimental
PgPipe has not been deployed in production yet. The API is stable and benchmarks are promising, but it needs real-world battle-testing before it can be considered production-ready. If you're interested in helping get it there — running it in staging, reporting issues, contributing fixes — that would be hugely appreciated. File issues at [GitHub](https://github.com/typr-dev/foundations-jdbc/issues).
:::

## Why Pipelining

Standard JDBC is request-response: send a query, wait for the result, send the next query. Each query pays a full network round-trip. With 10ms latency, 10 queries take 100ms of pure waiting.

PostgreSQL's wire protocol supports **pipelining** — the client can send multiple Parse/Bind/Execute sequences without waiting for responses. The server processes them back-to-back and streams results in order. 10 queries at 10ms latency take ~10ms total instead of 100ms.

This matters most for:
- **`combine()` / `sequence()`** — independent queries fire in parallel over a single TCP flush
- **Batch writes** — multiple inserts pipeline into a single round-trip
- **Connection-scarce environments** — pipelining multiplexes work over fewer connections
- **High-latency networks** — cloud regions, cross-AZ, VPN connections

## Setup

Add the PgPipe module alongside your language module:

<Tabs groupId="lang">
<TabItem value="java" label="Java">

```kotlin title="build.gradle.kts"
implementation("dev.typr:foundations-jdbc-pg-pipeline:1.0.0-RC1")
```

</TabItem>
<TabItem value="kotlin" label="Kotlin">

```kotlin title="build.gradle.kts"
implementation("dev.typr:foundations-jdbc-pg-pipeline:1.0.0-RC1")
implementation("dev.typr:foundations-jdbc-kotlin:1.0.0-RC1")
```

</TabItem>
<TabItem value="scala" label="Scala">

```kotlin title="build.gradle.kts"
implementation("dev.typr:foundations-jdbc-pg-pipeline:1.0.0-RC1")
implementation("dev.typr:foundations-jdbc-scala_3:1.0.0-RC1")
```

</TabItem>
</Tabs>

No JDBC driver needed — PgPipe talks directly to PostgreSQL over TCP.

Create a pool from any `DatabaseConfig` that points to PostgreSQL:

<Tabs groupId="lang">
<TabItem value="java" label="Java">

```java
import dev.typr.foundations.*;
import dev.typr.foundations.connect.*;
import dev.typr.foundations.pg.*;

var config = PgConfig.builder("localhost", 5432, "mydb", "user", "pass").build();
var pool = PgPipelinePool.create(config);

List<String> names = pool.query(
    Fragment.of("SELECT name FROM users"),
    RowCodec.of(PgTypes.text)
);

pool.close();
```

</TabItem>
<TabItem value="kotlin" label="Kotlin">

```kotlin
import dev.typr.foundations.connect.*
import dev.typr.foundations.pg.*
import dev.typr.foundationskt.*

val config = PgConfig.builder("localhost", 5432, "mydb", "user", "pass").build()
val pool = PgPipelinePool.create(config)
val tx = Transactor(pool)

val names: List<String> = tx.transactRead { conn ->
    conn.query(
        Fragment.of("SELECT name FROM users"),
        RowCodec.of(PgTypes.text)
    )
}

pool.close()
```

</TabItem>
<TabItem value="scala" label="Scala">

```scala
import dev.typr.foundations.connect.*
import dev.typr.foundations.pg.*
import dev.typr.foundationssc.*

val config = PgConfig.builder("localhost", 5432, "mydb", "user", "pass").build()
val pool = PgPipelinePool.create(config)
val tx = Transactor(pool)

val names: List[String] = tx.transactRead { conn =>
  conn.query(
    Fragment.of("SELECT name FROM users"),
    RowCodec.of(PgTypes.text)
  )
}

pool.close()
```

</TabItem>
</Tabs>

With custom pipeline configuration:

```java
var pool = PgPipelinePool.create(config,
    PgPipelineConfig.builder()
        .connectionCount(20)
        .queryTimeout(Duration.ofSeconds(10))
        .sslMode(PgPipelineSslMode.VERIFY_FULL)
        .sslRootCert("/etc/ssl/ca.pem")
        .applicationName("order-service")
        .build()
);
```

`PgPipelinePool` implements `TransactorPgPipe` (which extends `Transactor`). All the APIs documented in [Fragments](./fragments), [Operations](./operations), [Composing Operations](./composing-operations), [Streaming Reads](./streaming-reads), [Streaming Inserts](./streaming-inserts), and [Stored Procedures](./stored-procedures) work identically. The rest of this page focuses on what's different.

## Benchmarks

All benchmarks use PostgreSQL 16. "10ms RTT" means a TCP proxy adding 10ms round-trip latency to simulate real-world network conditions. See [Benchmarks](./benchmarks) for interactive charts.

### Reads: Where Pipelining Dominates

PgPipe's `transactRead` skips BEGIN/COMMIT and doesn't reserve a connection. Each query dispatches to the pool via round-robin with full pipelining. This is the fast path.

**Point reads (10 connections):**

| Executor | ops/sec | vs Raw JDBC |
|:---|---:|:---|
| **PgPipe (readonly)** | **50,467** | **2.9x** |
| Raw JDBC | 17,525 | baseline |
| Foundations+Hikari | 17,115 | 1.0x |
| Hibernate | 14,905 | 0.9x |

**Point reads at 10ms RTT (10 connections):**

| Executor | ops/sec | vs Raw JDBC |
|:---|---:|:---|
| **PgPipe (readonly)** | **9,890** | **13.3x** |
| Raw JDBC | 744 | baseline |
| Foundations+Hikari | 732 | 1.0x |
| Hibernate | 710 | 1.0x |

The gap widens dramatically with latency. Pipelining amortizes network round-trips — the higher the latency, the bigger the win.

### combine(): The Multiplier

`combine()` tells PgPipe that operations are independent. PgPipe fires them in parallel over the pipeline, collapsing N queries into ~1 round-trip. See [Composing Operations](./composing-operations#performance-why-composition-matters) for how this works.

**Fan-out: 10 queries per request, 5 connections, 10ms RTT:**

| Executor | ops/sec | vs Raw JDBC |
|:---|---:|:---|
| **PgPipe (readonly combine)** | **581** | **9.0x** |
| PgPipe (mutable combine) | 281 | 4.4x |
| Raw JDBC (sequential) | 64 | baseline |
| Hibernate (sequential) | 66 | 1.0x |

With a standard `Transactor`, `combine()` executes sequentially — there's no pipelining to exploit. PgPipe is the only executor where `combine()` actually runs in parallel.

### Scarce Connections

When connections are limited, pipelining multiplexes work over fewer connections instead of blocking:

**Point reads with only 2 connections, 10ms RTT:**

| Executor | ops/sec | vs Raw JDBC |
|:---|---:|:---|
| **PgPipe (readonly)** | **9,419** | **66x** |
| Raw JDBC | 142 | baseline |
| Hibernate | 129 | 0.9x |

This is PgPipe's most dramatic advantage. JDBC can only do one thing per connection at a time, so 2 connections means 2 concurrent queries. PgPipe pipelines hundreds of queries over those same 2 connections.

### Bulk Loading

**COPY FROM (batch size 100, single-threaded):**

| Executor | rows/sec | vs Raw JDBC |
|:---|---:|:---|
| **PgPipe (COPY)** | **303,030** | **5.5x** |
| Foundations+Hikari | 138,889 | 2.5x |
| PgPipe (batch insert) | 108,696 | 2.0x |
| Raw JDBC (batch) | 54,945 | baseline |
| Hibernate (batch) | 28,249 | 0.5x |

PgPipe's COPY implementation streams data over the wire protocol without JDBC overhead.

### Streaming Cursors

PgPipe uses deep prefetching — after the initial batch, it sends `cursorPrefetchDepth` (default 8) Execute messages ahead. After one round-trip, batches stream continuously.

**100k rows, fetchSize=100, 10ms RTT:**

| Executor | scans/sec | wall time |
|:---|---:|---:|
| **PgPipe** | **1.0** | **959ms** |
| Raw JDBC | 0.5 | 2,088ms |
| Vert.x | 0.5 | 2,165ms |

**2.2x faster** wall time. The difference grows with smaller fetch sizes (more round-trips for JDBC to wait on).

### Mixed Workloads

**80/20 read/write mix (10 connections):**

| Executor | ops/sec | vs Raw JDBC |
|:---|---:|:---|
| **PgPipe (mixed)** | **15,046** | **1.4x** |
| Raw JDBC | 10,983 | baseline |
| Vert.x | 8,818 | 0.8x |
| Hibernate | 6,027 | 0.5x |

### transactRead vs transact

The choice between `transactRead` and `transact` has significant performance implications:

**Single read, 10 connections, local:**

| Mode | ops/sec |
|:---|---:|
| `transactRead` | 42,815 |
| `transact` | 6,907 |

**6.2x difference.** `transactRead` skips BEGIN/COMMIT (2 fewer round-trips) and doesn't reserve a connection (no contention). Use it for all read-only workloads.

## Where PgPipe is Slower

Pipelining has overhead. For workloads that don't benefit from it, JDBC can be faster.

### Sequential Mutable Transactions

When every operation is a write that must be sequential within a transaction, PgPipe's pipelining offers no advantage, and the connection reservation cost adds overhead:

**5-op transaction (10 connections, local):**

| Executor | ops/sec |
|:---|---:|
| Hibernate | 2,779 |
| Raw JDBC | 2,590 |
| PgPipe (transact) | 2,098 |

**10-op transaction at 10ms RTT:**

| Executor | ops/sec |
|:---|---:|
| Hibernate | 73 |
| Raw JDBC | 68 |
| PgPipe (transact) | 57 |

For pure sequential write transactions, JDBC is ~20-30% faster. The pipelining machinery (sender/receiver threads, futures, send queue) adds overhead that isn't recouped when operations can't overlap.

### Single Inserts

**Single INSERT (50 connections, local):**

| Executor | ops/sec |
|:---|---:|
| Raw JDBC | 13,245 |
| Foundations+Hikari | 12,723 |
| PgPipe (mutable) | 7,037 |
| Hibernate | 5,411 |

Single-statement writes are ~1.9x slower than raw JDBC. Each mutable operation needs connection reservation, BEGIN, the statement, COMMIT — all pipelined but with more coordination than a direct JDBC call.

### Single Updates

**UPDATE (50 connections, local):**

| Executor | ops/sec |
|:---|---:|
| Raw JDBC | 16,047 |
| PgPipe (mutable) | 7,446 |

Same pattern as inserts. The overhead of pipelining coordination isn't justified when there's only one statement.

## Tradeoffs Summary

| Dimension | PgPipe | JDBC + HikariCP |
|:---|:---|:---|
| **Read throughput** | 2.9x–66x faster (grows with latency and connection scarcity) | Baseline |
| **combine() fan-out** | 5x–9x faster (parallel pipelining) | Sequential only |
| **Bulk COPY** | 2x–5.5x faster | Baseline |
| **Cursor prefetch** | 2.2x faster wall time | Baseline |
| **Sequential writes** | ~20-30% slower (coordination overhead) | Faster |
| **Single mutations** | ~1.5-2x slower | Faster |
| **Dependencies** | Zero — raw sockets, JDK crypto | JDBC driver + HikariCP |
| **Database support** | PostgreSQL only | All supported databases |
| **Raw JDBC access** | No (`unwrap()` throws) | Yes |
| **JDBC ecosystem** | No (no DataSource, no JPA) | Full compatibility |
| **Connection model** | Fixed-size array, round-robin | HikariCP pool with checkout/return |
| **Thread model** | Virtual threads (Java 21+) | Platform or virtual threads |
| **Authentication** | SCRAM-SHA-256, MD5, cleartext, trust | Delegated to JDBC driver |
| **SSL/TLS** | Built-in (DISABLE, REQUIRE, VERIFY_CA, VERIFY_FULL) | Delegated to JDBC driver |
| **Prepared stmt cache** | Built-in LRU per connection (default 256) | Delegated to JDBC driver / PgBouncer |
| **Connection recovery** | Automatic with exponential backoff | Delegated to HikariCP |
| **Observability** | `QueryListener`, `applicationName`, pool metrics | `QueryListener` + HikariCP metrics |

### When to Use PgPipe

- You're on **PostgreSQL** and don't need other databases
- Your workload is **read-heavy** or uses `combine()` / `sequence()` for fan-out
- You have **network latency** (cloud, cross-AZ, VPN) — the higher the latency, the bigger the win
- You're **connection-constrained** — pipelining extracts more throughput from fewer connections
- You want **zero dependencies** — no JDBC driver, no connection pool library

### When to Use Transactor + HikariCP

- You need **multi-database support** (MariaDB, Oracle, SQL Server, etc.)
- Your workload is **write-heavy with sequential transactions** — JDBC is faster here
- You need **raw `java.sql.Connection`** access (via `unwrap()`)
- You need **JDBC ecosystem compatibility** (JPA, Spring Data, connection pool monitoring tools)
- You're on **Java < 21** — PgPipe requires virtual threads and sealed interfaces

### Mixing Both

You can use both in the same application. PgPipe for read-heavy query paths, Transactor for write-heavy transaction paths or where you need raw JDBC access:

```java
var pgPipe = PgPipelinePool.create(config);
var hikari = HikariDataSourceFactory.create(config);
var transactor = hikari.transactor();

// Read-heavy dashboard: use PgPipe for pipelining
var dashboard = pgPipe.transactRead(tx ->
    tx.execute(loadUser.combine(loadOrders).combine(loadPrefs))
);

// Write-heavy import: use Transactor for sequential efficiency
transactor.transact(tx -> {
    for (var batch : batches) {
        tx.update(insertBatch);
    }
});
```

## SSL/TLS

PgPipe supports SSL/TLS with the same modes as PostgreSQL's `sslmode` parameter. All certificate parsing uses JDK-only APIs — no BouncyCastle or other crypto dependencies.

```java
PgPipelineConfig.builder()
    .sslMode(PgPipelineSslMode.VERIFY_FULL)
    .sslRootCert("/path/to/ca.pem")
    .sslCert("/path/to/client-cert.pem")   // optional: mutual TLS
    .sslKey("/path/to/client-key.pem")     // optional: mutual TLS
    .build();
```

| Mode | Encryption | Server Cert | Hostname |
|------|-----------|------------|----------|
| `DISABLE` | No | No | No |
| `REQUIRE` | Yes | No | No |
| `VERIFY_CA` | Yes | Yes | No |
| `VERIFY_FULL` | Yes | Yes | Yes |

## Authentication

Negotiated automatically during connection startup:

- **SCRAM-SHA-256** — default for PostgreSQL 14+, implemented with JDK crypto only
- **MD5** — legacy password hashing
- **Cleartext** — plain password (only use with SSL)
- **Trust** — no authentication required

## Connection Lifecycle

### Automatic Recovery

Dead connections are detected and replaced transparently. Read-only `execute()` calls retry on another connection. Reconnection uses exponential backoff (1s, 2s, 4s, 8s, 16s, max 30s, up to 5 attempts).

### Maintenance

A background virtual thread runs every `validationInterval` (default 30s):

- Replaces connections older than `maxLifetime` (default 30min) when idle
- Replaces connections idle longer than `idleTimeout` (default 10min)
- Sends keepalive pings after `keepaliveTime` (default 30s) of inactivity
- Replaces closed or unhealthy connections immediately

### Shutdown

`pool.close()` drains in-flight operations up to `shutdownTimeout` (default 5s), then closes all connections. A JVM shutdown hook ensures cleanup on unexpected termination.

## Pool Metrics

```java
pool.connectionCount()         // Total pool size
pool.activeConnectionCount()   // Connections with pending operations
pool.idleConnectionCount()     // Healthy, idle connections
pool.totalPendingQueries()     // Sum of pending ops across connections
pool.reservedConnectionCount() // Connections held for transactions
pool.isClosed()                // Pool shutdown status
```

## Observability

```java
PgPipelineConfig.builder()
    .queryListener(myListener)
    .applicationName("my-service")
    .build();
```

`applicationName` appears in `pg_stat_activity`. Per-operation `.named()` and `.withListener()` work the same as with a standard `Transactor`. See [Observability](./observability).

## Unsupported Operations

These JDBC-specific operations are not available:

- `Connection.unwrap()` — throws `PgPipelineException`
- `UpdateReturningGeneratedKeys` — use `RETURNING` clause with `UpdateReturning` instead
- `UpdateMany` / `UpdateManyReturning` — use `UpdateManyTemplate` for batch operations

## Full Configuration Reference

See [PgPipe Configuration](./pgpipe-configuration) for all options and defaults.
