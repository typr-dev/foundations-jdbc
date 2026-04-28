---
title: Virtual Threads
---

# Virtual Threads

foundations-jdbc's blocking API works with virtual threads (JDK 21+) out of the box. Every blocking JDBC call automatically yields its carrier thread when run on a virtual thread. No suspend wrappers, no reactive adapters, no new dependencies.

## Why virtual threads (not coroutines or reactive)

JDBC is a blocking API. Every `executeQuery()` blocks the calling thread until the database responds. Historically, this meant one platform thread per concurrent database operation — expensive at scale.

The usual workarounds each add complexity:

- **Kotlin coroutines** require `suspend` wrappers, dispatcher configuration, and a `kotlinx-coroutines` dependency, all to wrap calls that are still blocking underneath
- **Reactive streams (R2DBC)** require a different driver ecosystem, with no drivers for DuckDB or DB2
- **Effect systems** (Cats Effect, ZIO) need explicit `IO.blocking` / `ZIO.attemptBlocking` wrappers

Virtual threads solve this at the JVM level. A blocking call on a virtual thread automatically unmounts from the carrier thread, freeing it for other work. No wrappers needed. The blocking API *is* the async API.

This means foundations-jdbc's API works identically across Java, Kotlin, and Scala with no language-specific async modules:

```java
// Java — same API, same code, regardless of thread type
myQuery.transactRead(tx);
```

```kotlin
// Kotlin — no suspend, no withContext, no dispatcher
myQuery.transactRead(tx)
```

```scala
// Scala — no IO.blocking, no effect wrapper
myQuery.transactRead(tx)
```

## Framework configuration

Most frameworks handle virtual thread configuration for you. Once enabled, all request-handling code (including foundations-jdbc calls) runs on virtual threads automatically.

### Spring Boot 3.2+

```properties
# application.properties
spring.threads.virtual.enabled=true
```

All request threads become virtual. No code changes needed.

### Quarkus

```java
@RunOnVirtualThread
@GET
public List<Product> findAll() {
    return selectAll.transactRead(tx);
}
```

### Ktor

```kotlin
embeddedServer(Netty, port = 8080) {
    routing {
        get("/products") {
            // Already on a virtual thread if using virtual thread executor
            call.respond(selectAll.transactRead(tx))
        }
    }
}.start()
```

### Plain Java / manual setup

```java
var executor = Executors.newVirtualThreadPerTaskExecutor();

executor.submit(() -> {
    var result = myQuery.transactRead(tx);
    // ...
});
```

## Effect system integration

If you use an effect system, foundations-jdbc's blocking API integrates through the standard "run blocking code" primitive. On virtual threads, these wrappers add no overhead.

### Cats Effect (3.6+)

```scala
IO.blocking { myQuery.transactRead(tx) }
```

Cats Effect 3.6+ detects when `IO.blocking` is already running on a virtual thread and blocks in place, avoiding a thread shift.

### ZIO (2.1+)

```scala
ZIO.attemptBlocking { myQuery.transactRead(tx) }
```

Enable the virtual thread executor for blocking operations:

```scala
Runtime.default.withRuntimeFlags(
  RuntimeFlags.enable(RuntimeFlag.enableLoomBasedBlockingExecutor)
)
```

### Kotlin coroutines

If your application already uses coroutines, use a virtual thread dispatcher:

```kotlin
val vtDispatcher = Executors.newVirtualThreadPerTaskExecutor()
    .asCoroutineDispatcher()

withContext(vtDispatcher) {
    myQuery.transactRead(tx)
}
```

Note: adding `suspend` wrappers around foundations-jdbc calls provides no benefit on virtual threads. The coroutine suspend machinery (state machines, dispatcher scheduling) adds overhead to wrap calls that are already non-blocking from the carrier thread's perspective.

## Connection pool sizing

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

The [HikariCP pool sizing formula](https://github.com/brettwooldridge/HikariCP/wiki/About-Pool-Sizing) still applies: `connections = (core_count * 2) + effective_spindle_count`. A 4-core server with SSD needs roughly 8-10 connections. More connections does not mean more throughput: PostgreSQL benchmarks show TPS flattening well before 50 connections.

## JDK compatibility

### JDK 25 LTS / JDK 24+ (recommended)

[JEP 491](https://openjdk.org/jeps/491) eliminates virtual thread pinning caused by `synchronized` blocks. Delivered in JDK 24 and included in the **Java 25 LTS** release, this fixes pinning in JDBC drivers, connection pools, and any other library code using `synchronized`.

On JDK 24+, HikariCP and all major JDBC drivers work with virtual threads without pinning issues.

### JDK 21-23

Virtual threads work, but `synchronized` blocks in library code can *pin* a virtual thread to its carrier thread (preventing it from yielding), temporarily reducing concurrency. Known sources of pinning:

- **HikariCP** uses `synchronized` internally ([issue #1463](https://github.com/brettwooldridge/HikariCP/issues/1463)). The `synchronized` blocks guard short operations, so pinning impact is typically small.
- **JDBC drivers**: some PostgreSQL JDBC driver versions have `synchronized` in hot paths. Version 42.6.0+ improved this.

If pinning is a concern on JDK 21-23, [Agroal](https://agroal.github.io/docs.html) (Quarkus's default pool) is a virtual-thread-friendly alternative to HikariCP.

Upgrading to JDK 25 LTS is the recommended path. It eliminates all `synchronized`-related pinning across the entire dependency tree.

### Detecting pinning

On JDK 21-23, use `-Djdk.tracePinnedThreads=short` to log when virtual threads are pinned:

```
java -Djdk.tracePinnedThreads=short -jar myapp.jar
```

This prints a stack trace whenever a virtual thread is pinned, helping identify problematic `synchronized` blocks. On JDK 24+ this flag is no longer needed.
