---
title: Production Patterns
---

import Snippet from '@site/src/components/Snippet';

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

## Structuring Repositories

A common pattern is to define queries as private fields on a repository object, with public methods that bind parameters and execute them. This separates query definition (validated once at startup) from query execution:

```kotlin
object UserRepo {
    // Fixed query — use Sql { } (Kotlin) or Fragment.of() (Java)
    private val selectAll = Sql { "SELECT ${userParser.columnList} FROM users ORDER BY name" }
        .query(userParser.all())

    // Parameterized query — use the builder to create a SqlTemplate
    private val selectByIdTemplate = Fragment.of("SELECT ")
        .append(userParser.columnList).append(" FROM users WHERE id = ")
        .param(PgTypes.int4)
        .query(userParser.maxOne())

    // Public methods bind parameters and return Operations or results
    fun findAll(conn: Connection): List<User> = selectAll.run(conn)
    fun findById(id: Int, conn: Connection): User? = selectByIdTemplate.on(id).run(conn)

    // Expose Operations for composition with .with(), .then(), etc.
    fun findAllOp(): Operation.Query<List<User>> = selectAll
    fun findByIdOp(id: Int): Operation.Query<User?> = selectByIdTemplate.on(id)

    // Collect all queries for batch analysis in tests
    fun analyzeQueries(conn: Connection): List<QueryAnalysis> = listOf(
        QueryAnalyzer.analyze(selectAll.named("UserRepo.selectAll"), conn),
        QueryAnalyzer.analyze(selectByIdTemplate, conn),
    ).flatten()
}
```

The `analyzeQueries` method lets you verify all queries against the database schema in a single test:

```kotlin
tx.transact { conn ->
    val analyses = UserRepo.analyzeQueries(conn) + OrderRepo.analyzeQueries(conn)
    for (a in analyses) {
        check(a.succeeded()) { a.reportColored() }
    }
}
```

For methods that callers use inside a `transact` block, accept a `Connection` parameter. For standalone operations, return an `Operation` that the caller runs with `.transact(tx)` or composes further. Offering both gives callers the most flexibility.

## Bulk Operations

Use `RowSqlTemplate.Update` with `.onMany()` to batch-insert or batch-update rows. The template defines the SQL once, and `.onMany()` executes it for each row using JDBC batch mode (`addBatch()` / `executeBatch()`):

<Snippet file="core/BatchOperations" />

Driver-level optimizations like `reWriteBatchedInserts` (PostgreSQL), `useBulkStmts` (MariaDB), and `useBulkCopyForBatchInsert` (SQL Server) are applied automatically when enabled in the connection URL.

For PostgreSQL high-throughput inserts, use [streaming inserts](./streaming-inserts) with the COPY protocol instead.
