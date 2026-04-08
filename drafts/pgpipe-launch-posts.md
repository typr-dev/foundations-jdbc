# PgPipe Launch Post Drafts

## Timing

Post ~1 week after the Foundations JDBC launch. If JDBC launch is Sunday April 12 / Tuesday April 14, then PgPipe goes out **Sunday April 19** (HN) and **Tuesday April 21** (Reddit, X, Bluesky).

This gives the base library a full week to settle, lets the HN/Reddit threads die down naturally, and the PgPipe announcement can reference "the library we launched last week" for context.

Same time-of-day strategy: HN on Sunday 8-10 AM ET, Reddit/X/Bluesky on Tuesday 8-10 AM ET.

---

## Hacker News — Show HN

**Title:** Show HN: We implemented PostgreSQL's wire protocol from scratch in 3,400 lines of Java — 15x throughput over JDBC

**URL:** https://foundations.typr.dev/blog/faster-postgresql-jvm-pipelining

**Comment (post immediately after submitting):**

Last week we launched Foundations JDBC (https://github.com/typr-dev/foundations-jdbc), a type-safe JDBC library for Java/Kotlin/Scala. This is the experimental companion piece.

The premise: JDBC connections can only do one thing at a time. Send a query, wait for the response, send the next. The connection is idle during every network round-trip. With 10 connections and 10ms latency, you get ~1,000 queries/second. Hard ceiling.

PostgreSQL's wire protocol has supported pipelining since forever — send query 2 before query 1's response arrives. JDBC drivers just don't expose it. So we implemented the protocol ourselves. 3,400 lines of Java, zero dependencies — just java.net.Socket, JDK crypto, and our type system.

The numbers we're getting are frankly hard to believe:

- 15x throughput over JDBC (10 connections, 10ms RTT)
- 70x with only 2 connections (same latency)
- 9.6x for combine() fan-out (5 independent queries become 1 round-trip)
- COPY bulk loading at 2x JDBC executeBatch speed

The part I'm most interested in feedback on: the type system does double duty. `Operation<T>` is for reads, `MutableOperation<T>` is for writes. `combine()` declares independence between queries — the pipeline optimizer submits them simultaneously. `then()` declares a dependency — sequential execution. This is the applicative functor pattern, but you never need to know that.

`readonlyTransact()` uses the type distinction to skip BEGIN/COMMIT entirely and fan queries across all connections via round-robin. The overhead vs direct pool.execute() is 5%. The type safety is essentially free.

I want to be honest: this has not been deployed in production yet. The benchmarks are run with a TCP proxy adding real 10ms latency, and they're reproducible (bleep run benchmark), but I'm genuinely unsure if I'm missing something. The numbers seem too good. I would deeply appreciate people who run PostgreSQL in production taking a look and telling me what I'm getting wrong — or confirming that this is as good as it looks.

Known tradeoffs:
- Sequential mutable transactions are ~20-30% slower than JDBC
- Single inserts/updates are ~1.5-2x slower (pipelining coordination overhead)
- Tail latency is high under heavy concurrency (p99 can be 50-100x p50)

These make sense architecturally. The thing that makes me nervous is how large the read throughput gains are. 15x feels too clean. I'd love people with deep PostgreSQL wire protocol experience to review the implementation.

Source: https://github.com/typr-dev/foundations-jdbc (foundations-jdbc-pg-pipeline module)

---

## r/programming

**Title:** We implemented PostgreSQL's wire protocol in 3,400 lines of Java to bypass JDBC's one-query-at-a-time limitation. The results seem too good — we need help verifying them.

**Body:**

JDBC connections can only process one query at a time. Send, wait for response, send next. Every query pays a full network round-trip. With 10ms latency and 10 connections, you hit a ceiling of ~1,000 queries/second. Add more virtual threads? They just queue for connections.

PostgreSQL's wire protocol supports pipelining — send multiple queries back-to-back without waiting for responses. The server processes them in order and streams results back. JDBC drivers don't expose this.

So we implemented the wire protocol from scratch. 3,400 lines of Java, zero dependencies beyond java.net.Socket and JDK crypto. The result: **15x throughput over JDBC** under real network latency.

Here's the thing — we're not sure we believe our own numbers:

| Scenario | PgPipe | Raw JDBC | Speedup |
|---|---:|---:|---:|
| 1000 concurrent reads, 10 conns, 10ms RTT | 8,980 ops/s | 598 ops/s | **15x** |
| Same workload, 2 connections | 8,195 ops/s | 116 ops/s | **70x** |
| 5-query fan-out, combine(), 10ms RTT | 130 ops/s | 14 ops/s | **9.6x** |
| COPY FROM bulk load | 333K rows/s | 175K rows/s | **1.9x** |
| Cursor streaming, 100k rows, 10ms RTT | 959ms | 2,088ms | **2.2x** |

The benchmarks use a TCP proxy adding real 10ms latency (simulating cloud cross-AZ). They're reproducible — clone the repo and run them yourself.

**The interesting design part:** the library's type system does double duty as an optimization hint. `Operation<T>` is for reads, `MutableOperation<T>` is for writes. `combine(a, b)` declares independence — the optimizer batches both into a single TCP flush. `a.then(template)` declares a dependency — sequential execution. This is the applicative functor pattern, but at the API level it's just two methods.

`readonlyTransact()` exploits this: skip BEGIN/COMMIT, don't reserve a connection, fan queries across all connections. The overhead of the type-safety abstraction itself is 5%.

**Where it's worse than JDBC:**
- Sequential write transactions: ~20-30% slower
- Single inserts/updates: ~1.5-2x slower
- Tail latency under high concurrency

These tradeoffs make sense. What doesn't make sense to us is how large the read gains are. We've tested this extensively but haven't deployed it in production. If you know the PostgreSQL wire protocol, we'd genuinely appreciate a review of the implementation. Either we're missing something, or this has been sitting in plain sight in the protocol spec for years and JDBC just never exposed it.

This is a module on top of Foundations JDBC (our type-safe JDBC library that launched last week). The existing API — Fragment, RowCodec, Operation, combine() — works unchanged. You swap the executor.

Blog post with full architecture: https://foundations.typr.dev/blog/faster-postgresql-jvm-pipelining
Source (foundations-jdbc-pg-pipeline module): https://github.com/typr-dev/foundations-jdbc
Benchmarks: https://foundations.typr.dev/docs/benchmarks

---

## r/PostgreSQL

**Title:** We bypassed JDBC and implemented the PostgreSQL wire protocol directly in Java — pipelining gives 15x throughput over JDBC under real network latency. Looking for review from people who know the protocol.

**Body:**

We wrote a PostgreSQL client in 3,400 lines of Java that speaks the wire protocol directly over raw sockets. The point: wire-protocol pipelining. Send Parse/Bind/Execute messages back-to-back without waiting for responses. JDBC drivers don't expose this.

**What we implemented:**
- Full extended query protocol (Parse, Bind, Describe, Execute, Sync)
- SCRAM-SHA-256, MD5, cleartext, trust authentication
- SSL/TLS (DISABLE, REQUIRE, VERIFY_CA, VERIFY_FULL) with JDK-only crypto
- Transactions (BEGIN/COMMIT/ROLLBACK) over the pipeline
- COPY FROM STDIN streaming
- Server-side cursors with deep prefetching
- Prepared statement cache (LRU per connection)
- Connection recovery with exponential backoff
- Pool management with health checks and keepalives

**The numbers at 10ms RTT (TCP proxy simulating cloud latency):**

| Scenario | PgPipe | Raw JDBC + HikariCP | Speedup |
|---|---:|---:|---:|
| Point reads, 10 conns | 9,890 ops/s | 744 ops/s | **13x** |
| Point reads, 2 conns | 9,419 ops/s | 142 ops/s | **66x** |
| Fan-out (5 queries combined) | 581 ops/s | 64 ops/s | **9x** |
| COPY FROM (batch 100) | 303K rows/s | 55K rows/s | **5.5x** |
| Cursor streaming, 100k rows | 959ms | 2,088ms | **2.2x** |
| Mixed 80/20 r/w | 15,046 ops/s | 10,983 ops/s | **1.4x** |

**Where JDBC wins:**
- Sequential mutable transactions: PgPipe is ~20-30% slower (coordination overhead)
- Single INSERT/UPDATE: ~1.5-2x slower
- These make sense — pipelining adds overhead when there's nothing to overlap

The key optimization: `combine()` tells the executor that queries are independent. It batches them into a single TCP flush — N queries become ~1 round-trip. `readonlyTransact()` skips BEGIN/COMMIT entirely and doesn't reserve a connection — queries fan out across all connections via round-robin.

**This is experimental and has not been deployed in production.** The benchmarks are reproducible, but we're genuinely looking for review from people who know the PostgreSQL wire protocol well. The throughput gains seem almost too good, and we want to know if we're missing something — edge cases in the protocol, assumptions that break under real workloads, or correctness issues we haven't found in testing.

If you're interested in helping battle-test this, we'd be hugely grateful. File issues at the repo, or just tell us what we got wrong.

Source: https://github.com/typr-dev/foundations-jdbc
PgPipe docs: https://foundations.typr.dev/docs/pgpipe
Blog post: https://foundations.typr.dev/blog/faster-postgresql-jvm-pipelining

---

## r/java

**Title:** We implemented PostgreSQL's wire protocol from scratch in 3,400 lines of Java — zero dependencies, 15x throughput over JDBC. Looking for review.

**Body:**

Last week we released Foundations JDBC, a type-safe JDBC library. This is the experimental companion: a PostgreSQL wire-protocol client that bypasses JDBC entirely to enable pipelining.

**3,400 lines, zero dependencies.** The entire dependency list:
- `java.net.Socket` — TCP
- `java.security.MessageDigest` — SCRAM-SHA-256
- `javax.crypto.Mac` — HMAC
- `foundations-jdbc` — the type system

No Netty, no Reactor, no R2DBC, no Vert.x. No JDBC driver.

**What it does:** Standard JDBC sends a query, waits for the response, then sends the next. The PostgreSQL wire protocol supports sending multiple queries back-to-back — pipelining. With 10 connections and 10ms latency (typical cloud), this gets 15x throughput over JDBC+HikariCP. With 2 connections, 70x.

**The Java-specific things that made this possible:**

Virtual threads (Java 21) — each connection has dedicated sender and receiver threads. Blocking reads on Socket + OutputStream writes are clean and debuggable. No callback hell, no reactive streams, no async/await.

Sealed interfaces — `Operation<T>` (reads) and `MutableOperation<T>` (writes) are sealed. The pipeline optimizer pattern-matches on them: `combine()` batches independent queries into a single flush, `then()` forces sequential execution. The type system literally tells the optimizer what it can parallelize.

Records everywhere — `PgPipelineConfig`, `PgProtocol.Message`, benchmark results. Immutable by default.

**Same API as JDBC path:**

```java
// Both implement SqlExecutor — same types, same code
var pool = PgPipelinePool.create(config);

// Reads just work — no transaction overhead
var products = pool.execute(selectProducts.on("electronics"));

// combine() sends all queries in one TCP flush
var dashboard = pool.execute(
    findUser.on(userId)
        .combineWith(getOrders.on(userId), getPrefs.on(userId),
            Dashboard::new)
);
```

**Honest tradeoffs:**
- Sequential write transactions: ~20-30% slower than JDBC
- Single mutations: ~1.5-2x slower
- Tail latency high under concurrency (p99 can be 50-100x p50)

**This is experimental.** The benchmarks are solid and reproducible, but it hasn't seen production yet. We're looking for Java developers who run PostgreSQL in anger to review the implementation and tell us what we're missing. The numbers feel almost too good — 15x doesn't usually happen without a catch we haven't found yet.

Source: https://github.com/typr-dev/foundations-jdbc
Blog post with architecture: https://foundations.typr.dev/blog/faster-postgresql-jvm-pipelining

---

## r/Kotlin

**Title:** PgPipe: A PostgreSQL wire-protocol client for Kotlin — bypass JDBC, get 15x throughput with combine() and readonlyTransact()

**Body:**

Last week we released Foundations JDBC with Kotlin support (T?, sql {} interpolation). This is the experimental next step: a PostgreSQL client that speaks the wire protocol directly, enabling pipelining.

**What Kotlin developers get:**

```kotlin
val pool = PgPipelinePool.create(config)
val tx = Transactor(pool)

// Reads: no transaction overhead, queries fan out across connections
val products = tx.readonlyTransact { conn ->
    conn.query(sql { "SELECT * FROM products" }, productCodec)
}

// combine(): all three queries in one TCP flush
val dashboard = tx.execute(
    findUser.on(userId)
        .combineWith(getOrders.on(userId), getPrefs.on(userId)) { u, o, p ->
            Dashboard(u, o, p)
        }
)

// Writes need transact() — compile-time enforced
tx.transact { conn ->
    conn.execute(insertAudit.on(userId, "login"))
    // conn is MutableConnection — reads AND writes allowed
}
```

`readonlyTransact` gives you a `ReadonlyConnection` — try to execute a `MutableOperation` and the compiler rejects it. The pool trusts the type system: no BEGIN/COMMIT, no connection reservation, queries fan out via round-robin. 6x faster than `transact()` for reads.

**The numbers at 10ms RTT (cloud-realistic latency):**
- 15x throughput over JDBC for concurrent reads
- 70x with only 2 connections
- 9.6x for combine() fan-out (5 queries → 1 round-trip)
- 2.2x faster cursor streaming

3,400 lines of Java, zero dependencies beyond JDK. Same Fragment, RowCodec, Operation, sql {} — you swap the executor, not your code.

**This is experimental and not production-tested yet.** The numbers seem almost too good. We'd appreciate Kotlin developers with PostgreSQL workloads giving it a spin and telling us what breaks. 

Source: https://github.com/typr-dev/foundations-jdbc
Docs: https://foundations.typr.dev/docs/pgpipe

---

## r/scala

**Title:** PgPipe: A PostgreSQL wire-protocol client for Scala — combine() as applicative product, readonlyTransact() as free optimization, 15x over JDBC

**Body:**

Last week we released Foundations JDBC with Scala 3 support. This is the experimental companion: a PostgreSQL client that bypasses JDBC and speaks the wire protocol directly.

The design should be familiar to Scala developers: `combine()` is the applicative product — it declares independence between operations. `then()` (Scala: `andThen`) is the monadic bind — it declares a dependency. The pipeline optimizer uses this distinction to batch independent queries into a single TCP flush.

```scala
val pool = PgPipelinePool.create(config)
val tx = Transactor(pool)

// Applicative: all three fire in parallel over the pipeline
val dashboard = tx.execute(
  findUser.on(userId)
    .combineWith(getOrders.on(userId), getPrefs.on(userId))(
      Dashboard.apply
    )
)

// Monadic: sequential, because the dependency is real
val result = findUser.on(userId).andThen { user =>
  getOrdersForUser.on(user.id)
}
```

`Operation[T]` vs `MutableOperation[T]` — reads vs writes at the type level. `readonlyTransact()` exploits this: skip BEGIN/COMMIT, don't reserve a connection, fan queries across all connections. The type safety overhead is 5%.

If you've used doobie's `ConnectionIO` or skunk's `Session`, the mental model is similar — but the execution engine does pipelining under the hood. And unlike R2DBC or Vert.x, it's blocking I/O with virtual threads. No Cats Effect, no ZIO, no reactive streams.

**Numbers at 10ms RTT:** 15x throughput over JDBC, 70x with 2 connections, 9.6x for combine() fan-out.

3,400 lines of Java, zero dependencies. Same Fragment, RowCodec, Operation, sql"" — swap the executor.

**This is experimental.** Not production-tested. The numbers seem too good and we genuinely want people to find the holes. If you know the PostgreSQL wire protocol or have strong opinions about connection pool design, we'd love your review.

Source: https://github.com/typr-dev/foundations-jdbc
Docs: https://foundations.typr.dev/docs/pgpipe

---

## r/duckdb

*Skip — PgPipe is PostgreSQL-only, not relevant to r/duckdb.*

---

## r/jvm

**Title:** 3,400 lines of Java, zero dependencies: a PostgreSQL wire-protocol client with pipelining that gets 15x over JDBC. Looking for review.

**Body:**

We implemented the PostgreSQL wire protocol directly on `java.net.Socket` — no Netty, no Reactor, no JDBC driver. The goal: wire-protocol pipelining, where you send multiple queries before waiting for responses.

**Why this is a JVM story:**

Java 21 virtual threads make this architecture simple. Each of the 10 connections has a dedicated sender thread and receiver thread — all virtual. Blocking socket reads are clean and debuggable. The connection pool is a fixed-size array with round-robin dispatch. No callback chains, no reactive backpressure machinery.

The type system is the optimizer. `Operation<T>` is sealed with two branches: read-only operations and mutable operations. `combine()` is the applicative product — it tells the optimizer "these are independent, batch them." `then()` is the monadic bind — "I need the first result before computing the second." The pipeline optimizer pattern-matches on the operation tree to determine what can overlap.

**The numbers at 10ms RTT:** 15x throughput over JDBC+HikariCP. 70x with only 2 connections. combine() fan-out is 9.6x. COPY bulk loading is 5.5x.

**Dependency list:**
- `java.net.Socket`
- `java.security.MessageDigest`
- `javax.crypto.Mac`
- `foundations-jdbc` (our type system)

Full SCRAM-SHA-256 auth, SSL/TLS, transactions, COPY streaming, server-side cursors with prefetch, connection recovery, pool metrics. 3,400 lines total.

**This is experimental.** Not deployed in production. The read throughput gains seem almost too good. We're looking for JVM developers who know connection pools, wire protocols, or PostgreSQL internals to review the implementation and tell us what we're missing.

Source: https://github.com/typr-dev/foundations-jdbc
Blog post: https://foundations.typr.dev/blog/faster-postgresql-jvm-pipelining

---

## r/database

**Title:** We implemented PostgreSQL's wire protocol from scratch to bypass JDBC's one-query-at-a-time limitation — 15x throughput under real network latency. Looking for feedback.

**Body:**

The fundamental limitation of JDBC: one query at a time per connection. Send, wait for the full response, send next. Every query pays a network round-trip. With 10ms latency (typical cloud cross-AZ) and 10 connections: ceiling of ~1,000 queries/second.

PostgreSQL's wire protocol supports pipelining — send multiple Parse/Bind/Execute sequences without waiting. The server processes them in order, streams results back. JDBC drivers never expose this.

We implemented it. 3,400 lines of Java, speaking directly to PostgreSQL over TCP. The results:

| Scenario (10ms RTT) | PgPipe | JDBC + HikariCP | Factor |
|---|---:|---:|---:|
| Concurrent reads, 10 conns | 8,980 | 598 | **15x** |
| Concurrent reads, 2 conns | 8,195 | 116 | **70x** |
| 5-query fan-out (combined) | 130 | 14 | **9.6x** |
| COPY FROM bulk load | 303K rows/s | 55K rows/s | **5.5x** |
| Cursor streaming, 100k rows | 959ms | 2,088ms | **2.2x** |
| Mixed 80/20 r/w | 15,046 | 10,983 | **1.4x** |

**Where JDBC is faster:**
- Sequential write transactions: ~20-30% (pipelining coordination overhead)
- Single INSERT/UPDATE: ~1.5-2x

The architecture exploits a distinction between read and write operations. `combine(query1, query2)` declares independence — the engine batches both into a single TCP flush. Read-only transactions skip BEGIN/COMMIT entirely and don't reserve a connection — queries fan out across all connections.

**This is experimental and not production-tested.** The numbers are reproducible (TCP proxy with real 10ms latency), but we haven't deployed this anywhere real. We would genuinely appreciate feedback from database engineers and PostgreSQL experts. Are we missing something? Do these numbers hold up under real workloads? What edge cases in the wire protocol will bite us?

This is a module on top of Foundations JDBC (our type-safe database library). Same types, same API — swap the executor.

Source: https://github.com/typr-dev/foundations-jdbc
Docs: https://foundations.typr.dev/docs/pgpipe

---

## r/graalvm

*Skip — PgPipe is interesting for GraalVM (zero deps = easy native-image) but the angle is thin for a standalone post. Mention in the r/jvm post if someone asks.*

---

## X/Twitter — Thread

**Post 1 (hook):**

We implemented PostgreSQL's wire protocol from scratch. 3,400 lines of Java, zero dependencies.

Why? JDBC does one query at a time per connection. Pipelining sends them all at once.

Result: 15x throughput over JDBC under real network latency. 70x with only 2 connections.

#java #postgresql #opensource

**Post 2 (the trick):**

The type system IS the optimizer.

combine(a, b) = "these are independent" → batch into one TCP flush
a.then(template) = "I need a first" → sequential

The pipeline engine pattern-matches your operation tree to decide what can overlap.

**Post 3 (the number):**

10 connections, 10ms latency (typical cloud):

JDBC: 598 ops/sec (hard ceiling)
PgPipe: 8,980 ops/sec

Same PostgreSQL. Same queries. 15x.

**Post 4 (honesty):**

Honest tradeoffs:
- Sequential writes: ~20-30% slower than JDBC
- Single inserts: ~1.5-2x slower
- Tail latency high under concurrency

This is experimental and not production-tested. The read numbers seem too good. We need people to tell us what we're getting wrong.

**Post 5 (CTA):**

Blog: foundations.typr.dev/blog/faster-postgresql-jvm-pipelining
Source: github.com/typr-dev/foundations-jdbc
Benchmarks: foundations.typr.dev/docs/benchmarks

3,400 lines. Run the benchmarks yourself.

#postgresql #java

**Standalone posts:**

**Angle 1 — The bottleneck:**

JDBC can only do one query at a time per connection. 10 connections at 10ms latency = 1,000 queries/sec ceiling. Add more virtual threads? They just queue.

We bypassed JDBC entirely. PostgreSQL wire protocol, raw sockets. Same 10 connections now do 15,000 queries/sec.

github.com/typr-dev/foundations-jdbc

#java #postgresql

**Angle 2 — Zero deps:**

The entire dependency list of our PostgreSQL wire-protocol client:

- java.net.Socket
- java.security.MessageDigest
- javax.crypto.Mac

That's it. 3,400 lines. Full SCRAM auth, SSL/TLS, transactions, COPY streaming, cursors, connection recovery.

#java #postgresql

**Angle 3 — Applicative optimization:**

In our PostgreSQL client, combine() is the applicative product and then() is the monadic bind.

The pipeline optimizer uses this: independent queries batch into one TCP flush, dependent queries execute sequentially.

The type system tells the optimizer what it can parallelize. You never need to know the category theory.

#functionalprogramming #java

---

## Bluesky — Thread

**Post 1:**

We implemented PostgreSQL's wire protocol from scratch. 3,400 lines of Java, zero dependencies.

JDBC does one query at a time. Pipelining sends them all at once.

15x throughput over JDBC under real network latency. 70x with only 2 connections.

github.com/typr-dev/foundations-jdbc

**Post 2:**

The type system is the optimizer.

combine(a, b) = independent → batch into one TCP flush
a.then(b) = dependent → sequential

The engine pattern-matches your operation tree to decide what overlaps.

**Post 3:**

This is experimental. Not production-tested. The read throughput gains seem almost too good.

We need people who know PostgreSQL's wire protocol to tell us what we're getting wrong. Or confirm we're not dreaming.

**Post 4:**

Honest tradeoffs:
- Sequential writes ~20-30% slower than JDBC
- Single inserts ~1.5-2x slower  
- Tail latency high under concurrency

Blog: foundations.typr.dev/blog/faster-postgresql-jvm-pipelining

**Standalone:**

JDBC ceiling: 10 connections at 10ms latency = 1,000 queries/sec. Period.

We bypassed JDBC, spoke PostgreSQL's wire protocol directly. Same 10 connections: 8,980 queries/sec.

3,400 lines of Java. Zero dependencies. Experimental — we need help verifying the results.

github.com/typr-dev/foundations-jdbc
