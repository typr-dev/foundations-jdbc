---
title: Testing
---

import Snippet from '@site/src/components/Snippet';

# Testing

foundations-jdbc is designed to be tested against a real database. Use `testStrategy()` for data isolation, `QueryChecker` for type verification, and `AnalyzableScanner` for automatic query discovery. Together, they give you a test suite that catches SQL bugs before production.

## Test Strategy (Rollback Isolation)

`Transactor.testStrategy()` wraps each call in a transaction and **rolls back** instead of committing. Your tests run against real SQL without leaving data behind:

```kotlin
val tx = SingleConnectionDataSource.create(DuckDbConfig.inMemory().build())
    .transactor(Transactor.testStrategy())
```

| Hook | Behavior |
|------|----------|
| `onBegin` | `setAutoCommit(false)` |
| `onSuccess` | **rollback** (not commit) |
| `onComplete` | close connection |

Each test gets a clean slate. No teardown scripts, no truncation, no data leaking between tests.

## Setting Up a Test Database

### DuckDB (No Docker)

DuckDB runs in-memory with zero setup — ideal for fast unit tests:

```kotlin
class MyRepoTest {
    companion object {
        private val tx = SingleConnectionDataSource.create(
            DuckDbConfig.inMemory().build()
        ).transactor(Transactor.testStrategy())

        @BeforeAll @JvmStatic
        fun setup() {
            // Apply schema once — rollback strategy doesn't affect DDL in DuckDB
            tx.transact { conn ->
                Fragment.of("CREATE TABLE users (id INTEGER, name VARCHAR NOT NULL)").execute().run(conn)
            }
        }
    }

    @Test
    fun `find user by id`() {
        val insertAndFind = sql { "INSERT INTO users VALUES (1, 'Alice')" }
            .execute()
            .thenIgnore(UserRepo.findById.on(1))

        val user = insertAndFind.transact(tx)
        assertEquals("Alice", user?.name)
        // Rolled back — next test starts clean
    }
}
```

### PostgreSQL / MariaDB / Others (Docker)

For databases that need a server, use [Testcontainers](https://testcontainers.com/) or a shared test instance:

```kotlin
companion object {
    private val tx = SimpleDataSource.create(
        PostgresConfig.builder("localhost", 5432, "testdb", "test", "test").build()
    ).transactor(Transactor.testStrategy())
}
```

## Query Analysis in Tests

[Query Analysis](./query-analysis) verifies that your SQL matches the database schema — parameter types, column types, nullability, and counts. Run it as a test to catch drift between your code and the database.

### One Test for All Queries

`AnalyzableScanner` discovers every `Operation` and `Template` in a package. `QueryChecker` verifies them all:

```kotlin
@Test
fun `all queries type-check`() {
    val analyzables = AnalyzableScanner.scan("com.myapp.db")
    val checker = QueryChecker.create(tx)
    checker.checkAll(analyzables)
}
```

Add a new query anywhere in the package, and it's automatically included in the next test run. No manual list maintenance. See [Query Analysis](./query-analysis) for scanner configuration, directives, and the full report format.

### Checking Individual Queries

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

### Repository Tests

Test repository operations against a real database with rollback isolation:

```kotlin
@Test
fun `insert and retrieve`() {
    tx.transact { conn ->
        val created = UserRepo.create.on(User(0, "Bob")).run(conn)
        val found = UserRepo.findById.on(created.id).run(conn)
        assertEquals("Bob", found?.name)
    }
    // Transaction rolled back — no cleanup needed
}
```

### Composed Operation Tests

Test multi-step operations that run in a single transaction:

```kotlin
@Test
fun `transfer between accounts`() {
    val result = tx.transact { conn ->
        // Setup
        sql { "INSERT INTO account VALUES (1, 100.00), (2, 50.00)" }.execute().run(conn)

        // Operation under test
        AccountRepo.transfer(fromId = 1, toId = 2, amount = 25.00).run(conn)
    }
    // Both inserts and the transfer are rolled back
}
```

### Service Layer Tests

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
