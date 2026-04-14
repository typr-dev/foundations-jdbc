---
title: Connection Pooling
---

# Connection Pooling

For production use, the `foundations-jdbc-hikari` module provides HikariCP integration:

```java
var ds = HikariDataSourceFactory.create(
    PostgresConfig.builder("localhost", 5432, "mydb", "user", "pass").build(),
    PoolConfig.builder()
        .maximumPoolSize(20)
        .build()
);

var tx = ds.transactor();
```

The `PooledDataSource` returned by the factory implements `ConnectionSource`, so you can call `.transactor()` on it just like `SimpleDataSource`. It also implements `Closeable` — call `.close()` on application shutdown to release all pooled connections.

For connection-level settings (transaction isolation, auto-commit, read-only), use `ConnectionSettings`:

```java
var ds = HikariDataSourceFactory.create(
    PostgresConfig.builder("localhost", 5432, "mydb", "user", "pass").build(),
    ConnectionSettings.builder()
        .transactionIsolation(TransactionIsolation.READ_COMMITTED)
        .build(),
    PoolConfig.builder()
        .maximumPoolSize(20)
        .connectionTimeout(Duration.ofSeconds(5))
        .build()
);
```

Add the dependency:

```kotlin
implementation("dev.typr.foundations:foundations-jdbc-hikari:1.0.0-RC3")
```
