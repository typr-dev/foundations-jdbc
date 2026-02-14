---
title: Production Patterns
---

# Production Patterns

Guidance for using Foundations JDBC in production applications.

## Connection Pooling

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
implementation("dev.typr:foundations-jdbc-hikari:version")
```

## Spring Boot

The `foundations-jdbc-spring` module provides auto-configuration. Add it to your dependencies:

```kotlin
implementation("dev.typr:foundations-jdbc-spring:version")
```

A `Transactor` bean is automatically created when a `DataSource` is available. Simply inject it:

```java
@Service
public class ProductService {
    private final Transactor tx;

    public ProductService(Transactor tx) {
        this.tx = tx;
    }

    public List<Product> findAll() throws SQLException {
        return selectAll.transact(tx);
    }
}
```

The Spring transactor automatically adapts to the current transaction context:
- Inside `@Transactional`: joins the existing Spring-managed transaction
- Outside `@Transactional`: manages its own transaction (begin/commit/rollback)

If you need to configure the transactor manually:

```java
@Configuration
public class AppConfig {
    @Bean
    public Transactor transactor(DataSource dataSource) {
        return SpringTransactor.create(dataSource);
    }
}
```

## Migrations (Flyway / Liquibase)

Foundations JDBC works alongside any migration tool with no special integration needed. Run migrations before application startup as usual. Foundations reads from whatever schema your migrations create.

## Thread Safety

`Transactor` is stateless and thread-safe. Share a single instance across your application.

Each call to `transactor.execute(...)` obtains its own connection from the underlying `ConnectionSource`. Connections are not shared across operations or threads.

Do not share `Connection` objects across threads. If you need to run multiple operations on the same connection (e.g., within a transaction), do so within a single `execute` block:

```java
tx.execute(conn -> {
    var user = findUser.runChecked(conn);
    var orders = findOrders.runChecked(conn);
    return new Dashboard(user, orders);
});
```

## Bulk Operations

For PostgreSQL, use [streaming inserts](./streaming-inserts) with the COPY protocol for maximum throughput.

For other databases, use `Operation.sequence()` within a transaction:

```java
var inserts = items.stream()
    .map(item -> insertItem(item).update())
    .toList();
Operation.sequence(inserts).transact(tx);
```

All inserts run in a single transaction. For very large batches, consider chunking to avoid holding the transaction open too long.
