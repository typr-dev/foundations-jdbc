---
title: Virtual Threads
---

# Virtual Threads

**The simplest path to high-concurrency database access on the JVM.**

foundations-jdbc's blocking API is designed to work naturally with virtual threads (JDK 21+). No suspend wrappers, no reactive adapters, no new dependencies. Every blocking JDBC call — getting a connection, executing a query, committing a transaction — automatically yields its carrier thread when run on a virtual thread.

## Why Virtual Threads (Not Coroutines or Reactive)

JDBC is a blocking API. Every `executeQuery()` blocks the calling thread until the database responds. Historically, this meant one platform thread per concurrent database operation — expensive at scale.

The traditional workarounds each add complexity:

- **Kotlin coroutines** require `suspend` wrappers, dispatcher configuration, and a `kotlinx-coroutines` dependency — all to wrap calls that are still blocking underneath
- **Reactive streams (R2DBC)** require a completely different driver ecosystem — with no drivers for DuckDB or DB2
- **Effect systems** (Cats Effect, ZIO) need explicit `IO.blocking` / `ZIO.attemptBlocking` wrappers

Virtual threads solve this at the JVM level. A blocking call on a virtual thread automatically unmounts from the carrier thread, freeing it for other work. No wrappers needed. The blocking API *is* the async API.

This means foundations-jdbc's API works identically across Java, Kotlin, and Scala with no language-specific async modules:

```java
// Java — same API, same code, regardless of thread type
myQuery.transact(tx);
```

```kotlin
// Kotlin — no suspend, no withContext, no dispatcher
myQuery.transact(tx)
```

```scala
// Scala — no IO.blocking, no effect wrapper
myQuery.transact(tx)
```

## Framework Configuration

Most frameworks handle virtual thread configuration for you. Once enabled, all request-handling code (including foundations-jdbc calls) runs on virtual threads automatically.

### Spring Boot 3.2+

```properties
# application.properties
spring.threads.virtual.enabled=true
```

All request threads become virtual. No code changes needed — your existing `Transactor` calls just work.

### Quarkus

```java
@RunOnVirtualThread
@GET
public List<Product> findAll() {
    return selectAll.transact(tx);
}
```

### Ktor

```kotlin
embeddedServer(Netty, port = 8080) {
    routing {
        get("/products") {
            // Already on a virtual thread if using virtual thread executor
            call.respond(selectAll.transact(tx))
        }
    }
}.start()
```

### Plain Java / Manual Setup

```java
var executor = Executors.newVirtualThreadPerTaskExecutor();

executor.submit(() -> {
    var result = myQuery.transact(tx);
    // ...
});
```

## Effect System Integration

If you use an effect system, foundations-jdbc's blocking API integrates through the standard "run blocking code" primitive. On virtual threads, these become zero-overhead wrappers.

### Cats Effect (3.6+)

```scala
IO.blocking { myQuery.transact(tx) }
```

Cats Effect 3.6+ detects when `IO.blocking` is already running on a virtual thread and blocks in-place — no thread shift, no overhead.

### ZIO (2.1+)

```scala
ZIO.attemptBlocking { myQuery.transact(tx) }
```

Enable the virtual thread executor for blocking operations:

```scala
Runtime.default.withRuntimeFlags(
  RuntimeFlags.enable(RuntimeFlag.enableLoomBasedBlockingExecutor)
)
```

### Kotlin Coroutines

If your application already uses coroutines, use a virtual thread dispatcher:

```kotlin
val vtDispatcher = Executors.newVirtualThreadPerTaskExecutor()
    .asCoroutineDispatcher()

withContext(vtDispatcher) {
    myQuery.transact(tx)
}
```

Note: adding `suspend` wrappers around foundations-jdbc calls provides no benefit on virtual threads. The coroutine suspend machinery (state machines, dispatcher scheduling) adds overhead to wrap calls that are already non-blocking from the carrier thread's perspective.

## Connection Pool Sizing

With virtual threads, the bottleneck shifts from thread count to connection count. You can have thousands of virtual threads, but only N simultaneous database operations where N is your connection pool size.

Size the pool based on your database's capacity, not your thread count:

```java
var ds = HikariDataSourceFactory.create(
    config,
    PoolConfig.builder()
        .maximumPoolSize(20)  // based on database capacity, not thread count
        .build()
);
```

The [HikariCP pool sizing formula](https://github.com/brettwooldridge/HikariCP/wiki/About-Pool-Sizing) still applies: `connections = (core_count * 2) + effective_spindle_count`. A 4-core server with SSD needs roughly 8-10 connections. More connections does not mean more throughput — PostgreSQL benchmarks show TPS flattening well before 50 connections.

## JDK Compatibility

### JDK 25 LTS / JDK 24+ (Recommended)

[JEP 491](https://openjdk.org/jeps/491) eliminates virtual thread pinning caused by `synchronized` blocks. Delivered in JDK 24 and included in the **Java 25 LTS** release, this fixes pinning in JDBC drivers, connection pools, and any other library code using `synchronized`.

On JDK 24+, HikariCP and all major JDBC drivers work with virtual threads without any issues.

### JDK 21-23

Virtual threads work, but `synchronized` blocks in library code can *pin* a virtual thread to its carrier thread, temporarily reducing concurrency. Known sources of pinning:

- **HikariCP** — uses `synchronized` internally ([issue #1463](https://github.com/brettwooldridge/HikariCP/issues/1463)). The `synchronized` blocks guard short operations, so pinning impact is typically small.
- **JDBC drivers** — some PostgreSQL JDBC driver versions have `synchronized` in hot paths. Version 42.6.0+ improved this.

If pinning is a concern on JDK 21-23, [Agroal](https://agroal.github.io/docs.html) (Quarkus's default pool) is a virtual-thread-friendly alternative to HikariCP.

Upgrading to JDK 25 LTS is the recommended path — it eliminates all `synchronized`-related pinning across the entire dependency tree.

### Detecting Pinning

On JDK 21-23, use `-Djdk.tracePinnedThreads=short` to log when virtual threads are pinned:

```
java -Djdk.tracePinnedThreads=short -jar myapp.jar
```

This prints a stack trace whenever a virtual thread is pinned, helping identify problematic `synchronized` blocks. On JDK 24+ this flag is no longer needed.
