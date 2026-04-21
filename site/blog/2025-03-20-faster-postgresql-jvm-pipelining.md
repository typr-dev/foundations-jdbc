---
slug: faster-postgresql-jvm-pipelining
title: "Faster PostgreSQL on the JVM: Wire-Protocol Pipelining from Scratch"
authors:
  - name: Foundations JDBC
tags: [postgresql, performance, pipelining, jdbc, transactions]
---

Every JDBC connection can only do one thing at a time. Send a query, wait for the response, send the next. The connection sits idle for the entire network round-trip — and there's nothing you can do about it. No amount of virtual threads, no clever pool tuning, no framework magic. Ten connections at 10ms latency? That's 1,000 queries/second, hard ceiling.

We thought that was unacceptable. So we implemented the PostgreSQL wire protocol from scratch — 3,400 lines of Java, zero dependencies — and now those same 10 connections handle **15x more throughput**. With only 2 connections, the advantage reaches **70x**.

This post explains how it works, what it's good at, where it's honest-to-god worse than JDBC, and why the type system turns out to be the secret weapon.

<!-- truncate -->

## The JDBC Bottleneck

Here's what happens on every JDBC connection, every query, no exceptions:

<img src="/img/blog/jdbc-bottleneck.svg" alt="JDBC: one query at a time per connection, with idle time during each round-trip" style={{maxWidth: '100%', margin: '1rem 0'}} />

Connection pools mitigate this by having many connections. But each connection still processes one query at a time. With 10 connections and 10ms RTT, your ceiling is `10 × (1000ms / 10ms) = 1,000 queries/second`. Add more virtual threads? They just queue for connections. Add more connections? Your DBA says no, and they're right — each PostgreSQL connection costs real memory and each managed database has hard limits.

## What If Connections Could Multitask?

The PostgreSQL wire protocol has supported this since forever: you can send query 2 before query 1's response arrives. The server processes them in order and streams results back. JDBC drivers just don't expose it.

<img src="/img/blog/pipeline-throughput.svg" alt="Pipeline: all queries sent in a single flush, results stream back in one round-trip" style={{maxWidth: '100%', margin: '1rem 0'}} />

With 256 queries in flight per connection, 10 connections do the work of 2,560 — with a fraction of the memory.

## 3,400 Lines, Zero Dependencies

The `foundations-jdbc-pg-pipeline` module implements this directly on `java.net.Socket`. The dependency list:

- `java.net.Socket` — TCP
- `java.security.MessageDigest` — SCRAM-SHA-256
- `javax.crypto.Mac` — HMAC
- `foundations-jdbc` — the type system

That's it. No Netty, no Reactor, no R2DBC, no Vert.x. The module covers authentication (MD5 + SCRAM-SHA-256), the full extended query protocol, SSL/TLS, transactions, streaming COPY, server-side cursors with prefetch, connection lifecycle management, and backpressure. It's a complete PostgreSQL client.

### Same Code, Different Engine

The pipeline pool implements `Transactor` (via `TransactorPgPipe`). Your existing code works unchanged:

```java
// Works with both Transactor and PgPipelinePool
List<Product> products = executor.execute(
    selectByCategory.on("electronics")
);
```

Swap the implementation, keep the types. `Fragment`, `RowCodec`, `Template`, `Operation` — everything carries over.

## Two Breakthroughs: Combine and Readonly Transactions

Pipelining by itself is powerful — queries from many virtual threads share a few connections. But the type system unlocks two optimizations that go further.

### Breakthrough 1: `combine()` — Parallel Queries in One Round-Trip

Consider loading a dashboard — user profile, recent orders, notification count. Three independent queries:

```java
// Sequential: 3 separate execute calls = 3 round-trips (30ms at 10ms RTT)
var user   = pool.execute(findUser.on(userId));
var orders = pool.execute(getOrders.on(userId));
var prefs  = pool.execute(getPreferences.on(userId));
var dashboard = new Dashboard(user, orders, prefs);

// Combined: one execute call = one round-trip (~10ms at 10ms RTT)
var dashboard = pool.execute(
    findUser.on(userId)
        .combineWith(getOrders.on(userId), getPreferences.on(userId),
            Dashboard::new)
);
```

Same result. Same transaction. One-third the latency. And the gap widens:

| Queries per transaction | `combine()` | Sequential | Speedup |
| :--- | ---: | ---: | ---: |
| 5 queries @ 10ms RTT | 3.7s wall | 9.5s wall | **2.7x** |
| 10 queries @ 10ms RTT | 3.7s wall | 17.1s wall | **4.9x** |

Notice: 5 queries and 10 queries have **nearly identical wall time** with `combine()`. The RTT cost is paid once regardless of query count.

When you write `a.combine(b)`, you're telling the execution engine: *"these are independent — run them however you want."* The pipeline optimizer submits both to the connection's send queue simultaneously, the sender thread batches them into a single TCP flush, and results stream back in one round-trip.

When you write `a.then(template)`, you're saying: *"I need a's result first."* That forces sequential execution — correct, because the dependency is real.

Most database libraries only offer the sequential path. By offering both, the optimizer parallelizes where it can and sequentializes where it must.

:::info Under the hood
This is the *applicative functor* pattern. `combine()` is the applicative product (declares independence), `then()` is the monadic bind (declares dependency). The distinction is invisible at the API level, but it gives the optimizer permission to batch independent queries into a single round-trip. See [Composing Operations](/docs/composing-operations#performance-why-composition-matters) for the full breakdown.
:::

### Breakthrough 2: `readonlyTransact()` — Zero-Cost Read Safety

Operations are modeled as two separate types: `Operation` for reads and `MutableOperation` for reads + writes. Connections follow the same split: `ReadonlyConnection` can only execute `Operation`, while `MutableConnection` (which extends it) can execute both.

When you need writes, use `transact()` — it reserves a connection, wraps in BEGIN/COMMIT, and gives you a `MutableConnection` that can do anything:

```java
pool.transact(tx -> {
    var user = tx.execute(findUser.on(userId));
    tx.execute(insertAudit.on(userId, "login"));
    return user;
});
```

For reads, just call `execute()` directly — no transaction needed, queries fan out across all connections:

```java
var dashboard = pool.execute(
    findUser.on(userId)
        .combineWith(getOrders.on(userId), getPreferences.on(userId),
            Dashboard::new)
);
```

When you have multiple separate read calls and want compile-time safety that no writes sneak in, use `readonlyTransact()`. The `ReadonlyConnection` rejects `MutableOperation` at compile time:

```java
pool.readonlyTransact(conn -> {
    var user = conn.queryFirst(userSql, userCodec);
    var orders = conn.query(ordersSql, orderCodec);
    return new Dashboard(user.orElseThrow(), orders);
    // conn.execute(insertAudit.on(...))  ← WON'T COMPILE
});
```

`readonlyTransact()` tells the pool: *"this block only reads."* The pool trusts the type system and skips all transaction overhead:

- **No BEGIN/COMMIT** — zero protocol messages wasted
- **No connection reservation** — the connection isn't monopolized
- **Queries fan out across ALL connections** — `combine()` dispatches each sub-query to a different connection via round-robin

The result? Readonly + combine is the fastest path through the system:

| Path (5 queries, 10ms RTT) | ops/sec | vs Raw JDBC |
| :--- | ---: | ---: |
| **readonly + combine** | **130** | **9.6x faster** |
| readonly + sequential | 99 | 7.1x faster |
| mutable + combine | 45 | 3.2x faster |
| mutable + sequential | 21 | 1.5x faster |
| Raw JDBC | 14 | baseline |

And the safety is compile-time — try to sneak a `MutableOperation` into a `readonlyTransact` block and the compiler rejects it. No runtime checks, no annotations, no hope-and-pray.

The overhead of the `ReadonlyConnection` abstraction itself? **5%** vs direct `pool.execute()`. The type safety is essentially free.

## More Numbers

We benchmarked against Raw JDBC + HikariCP, Foundations+Hikari, Vert.x, and Hibernate across 9 benchmark suites. All latency benchmarks use a **10ms round-trip** via TCP proxy — typical for cloud deployments where your app and database are in different availability zones.

### Throughput Under Latency

1,000 concurrent reads, 10 connections, 10ms RTT:

| | ops/sec |
| :--- | ---: |
| Raw JDBC | 598 |
| Hibernate | 612 |
| **PgPipe** | **8,980** |

**15x faster.** JDBC's ceiling is ~1,000 ops/sec. PgPipe blows past it.

### Extreme Scarcity

Same workload, **2 connections**:

| | ops/sec |
| :--- | ---: |
| Raw JDBC | 116 |
| **PgPipe** | **8,195** |

**70x faster.** Two pipelined connections handle what would require 140+ JDBC connections.

### Bulk Loading

10,000 rows via `COPY FROM STDIN`:

| | rows/sec |
| :--- | ---: |
| Hibernate batch | 21,368 |
| JDBC executeBatch | 175,439 |
| **PgPipe COPY** | **333,333** |

**COPY is 2x faster than JDBC executeBatch** and **16x faster than Hibernate**.

### Even Cursors Get Faster

PgPipe prefetches the next cursor batch while the application processes the current one. At 10ms RTT with fetchSize=100: **938ms** vs JDBC's **2,164ms** — 2.3x faster.

### What About Localhost?

Even locally, PgPipe delivers ~2x throughput over JDBC (47,000 vs 18,000 ops/sec). And Foundations+Hikari matches or beats Raw JDBC — the type system adds zero overhead.

See the [interactive benchmark charts](/docs/benchmarks) for all suites with full percentile breakdowns.

## Honest Trade-Offs

**Sequential transactions are ~1.2x slower than JDBC.** When each query depends on the previous result, JDBC's direct socket writes beat our send-queue architecture. The fix: use `combine()` for independent queries, accept the small overhead for sequential chains.

**Tail latency is high under concurrency.** With 200 virtual threads sharing 10 connections, p99 can be 50-100x the p50. The same pipeline that enables 15x throughput causes queuing at the tail. This is the fundamental throughput-vs-tail-latency trade-off.

**Pipelined batch inserts are slower than JDBC executeBatch.** For bulk loading, use `COPY FROM STDIN` — it's faster than everything.

These are the trade-offs of an architecture optimized for many concurrent clients, limited connections, and real network latency — which is every cloud deployment.

## When to Use What

**Use `Transactor` (JDBC)** for multi-vendor support, query analysis, Spring integration, or simple applications.

**Use `PgPipelinePool`** for PostgreSQL under network latency, connection-constrained environments, fan-out patterns, or bulk data loading.

**Use `readonlyTransact()`** whenever your block only reads. It's free safety with real performance benefits.

**Use `combine()`** whenever queries are independent. Locally it's neutral. Under latency it's transformative.

Both `Transactor.create()` and `PgPipelinePool.create()` return a `Transactor`. Write your data access layer against the interface, swap the implementation per environment.

## Get Started

```xml
<dependency>
    <groupId>dev.typr</groupId>
    <artifactId>foundations-jdbc-pg-pipeline</artifactId>
</dependency>
```

```java
var pool = PgPipelinePool.create(
    PgConfig.builder("host", 5432, "mydb", "user", "pass").build(),
    PgPipelineConfig.builder()
        .connectionCount(10)
        .pipeliningLimit(256)
        .build());

// Every query you already have — just faster
var products = pool.execute(selectProducts.on("electronics"));

// Fan-out: all three queries in a single round-trip
var dashboard = pool.execute(
    findUser.on(userId)
        .combineWith(getOrders.on(userId), getPrefs.on(userId),
            Dashboard::new)
);

// Writes need transact — BEGIN/COMMIT on a reserved connection
pool.transact(conn -> {
    conn.execute(insertAudit.on(userId, "login"));
    conn.update(Fragment.of("DELETE FROM expired_sessions"));
});
```

Run the [benchmarks](/docs/benchmarks) yourself: `bleep run benchmark`. The numbers render as interactive charts on the documentation site.
