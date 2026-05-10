---
title: Testing
---

import Snippet from '@site/src/components/Snippet';

# Testing

Test foundations-jdbc code against a real database. Use `.rollbackOnly()` for data isolation, `QueryChecker` to verify types match the schema, and `AnalyzableScanner` to discover queries automatically.

## Rollback isolation

`.rollbackOnly()` wraps each call in a transaction and rolls back instead of committing. Tests run against real SQL without leaving data behind:

```kotlin
val tx = SingleConnectionDataSource.create(DuckDbConfig.inMemory().build())
    .transactor().rollbackOnly()
```

Each test gets a clean slate. No teardown scripts, no truncation, no data leaking between tests.

## Setting up a test database

### DuckDB (no Docker)

DuckDB runs in-memory with no setup, which is convenient for fast unit tests:

```kotlin
class MyRepoTest {
    companion object {
        private val tx = SingleConnectionDataSource.create(
            DuckDbConfig.inMemory().build()
        ).transactor().rollbackOnly()

        @BeforeAll @JvmStatic
        fun setup() {
            // Apply schema once — rollback strategy doesn't affect DDL in DuckDB
            tx.execute(Fragment.of("CREATE TABLE users (id INTEGER, name VARCHAR NOT NULL)").execute())
        }
    }

    @Test
    fun `find user by id`() {
        val insertAndFind = sql { "INSERT INTO users VALUES (1, 'Alice')" }
            .execute()
            .productL(UserRepo.findById.on(1))

        val user = tx.execute(insertAndFind)
        assertEquals("Alice", user?.name)
        // Rolled back — next test starts clean
    }
}
```

### PostgreSQL / MariaDB / others (Docker)

For databases that need a server, use [Testcontainers](https://testcontainers.com/) or a shared test instance:

```kotlin
companion object {
    private val tx = SimpleDataSource.create(
        PgConfig.builder("localhost", 5432, "testdb", "test", "test").build()
    ).transactor().rollbackOnly()
}
```

## Query analysis in tests

[Query Analysis](./query-analysis) verifies that your SQL matches the database schema — parameter types, column types, nullability, and counts. Run it as a test to catch drift between your code and the database.

### One test for all queries

`AnalyzableScanner` discovers every `OperationRead` and `Operation` in a package. `QueryChecker` verifies them all:

```kotlin
@Test
fun `all queries type-check`() {
    val analyzables = AnalyzableScanner.scan("com.myapp.db")
    val checker = QueryChecker.create(tx)
    checker.checkAll(analyzables)  // throws AssertionError if any query fails
}
```

For detailed output showing each query, use `analyzeAll`:

```kotlin
val report = checker.analyzeAll(analyzables)
println(report.summary(colored = true))   // prints each query with ✓/✗ (verbose box for failures)
report.assertAllSucceeded()        // throws if any failed
```

Add a new query anywhere in the package and it's picked up on the next test run. No manual list to maintain. See [Query Analysis](./query-analysis) for scanner configuration, directives, and the full report format.

### Checking individual queries

For queries that the scanner can't discover (dynamic SQL, special constructors), check them directly:

```kotlin
@Test
fun `search query type-checks`() {
    val checker = QueryChecker.create(tx)
    checker.check(UserRepo.findById)
    checker.check(UserRepo.searchByName.on("test"))
}
```

## Patterns

### Repository tests

Test repository operations against a real database with rollback isolation:

```kotlin
@Test
fun `insert and retrieve`() {
    tx.transact { mc ->
        val created = mc.execute(UserRepo.create.on(User(0, "Bob")))
        val found = mc.execute(UserRepo.findById.on(created.id))
        assertEquals("Bob", found?.name)
    }
    // Transaction rolled back — no cleanup needed
}
```

### Composed operation tests

Test multi-step operations that run in a single transaction:

```kotlin
@Test
fun `transfer between accounts`() {
    tx.transact { mc ->
        // Setup
        mc.execute(sql { "INSERT INTO account VALUES (1, 100.00), (2, 50.00)" }.execute())

        // Operation under test
        mc.execute(AccountRepo.transfer(fromId = 1, toId = 2, amount = 25.00))
    }
    // Both inserts and the transfer are rolled back
}
```

### Service layer tests

Pass a test transactor to your service:

```kotlin
@Test
fun `publish event changes status`() {
    val service = EventService(tx)

    // Setup via the service itself
    val (_, event) = service.createVenueWithEvent(/* ... */)

    val published = service.publishEvent(event.id)
    assertEquals(EventStatus.PUBLISHED, published.status)
    // Everything rolled back
}
```
