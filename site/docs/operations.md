---
title: Operations
---

import Snippet from '@site/src/components/Snippet';

# Operations

Call `.query()` or `.update()` on a Fragment to get an `Operation<T>` — a database action that produces a value of type `T`. It doesn't run until you call `.transact(tx)` or `.run(conn)`.

## Queries

Read rows from the database. Pass a `RowCodec` with a result mode to control how the ResultSet is consumed. For single-column results, shorthand methods skip the codec:

<Snippet file="core/OperationQueries" />

## Updates

Write to the database — INSERT, UPDATE, DELETE, or DDL:

<Snippet file="core/OperationUpdates" />

## Returning Rows from Updates

`INSERT ... RETURNING` or `UPDATE ... RETURNING` — run a write and read back the affected rows:

<Snippet file="core/OperationReturning" />

## Execute (No Result)

When you don't need the row count — DDL statements, fire-and-forget DML — use `.execute()` instead of `.update()`. It returns `Operation<Void>` (Java) / `Operation<Unit>` (Kotlin/Scala):

```java
Fragment.of("CREATE TABLE users (id INT, name VARCHAR)").execute()
```

This is equivalent to `.update().voided()`.

## Running Operations

Use a [Transactor](./transactors) to obtain a connection, run the operation, and handle commit/rollback automatically.

For a **single operation**, call `.transact(tx)` on it directly — the Transactor borrows a connection, runs the operation, commits on success, rolls back on failure, and closes the connection.

For **multiple operations in one transaction**, call `.run(conn)` on each inside a `tx.execute(conn -> …)` (Java) / `tx.transact { conn -> … }` (Kotlin/Scala) block — all `.run` calls share the same connection and therefore the same transaction:

<Snippet file="core/ExecuteTransact" />

If you need even finer control — running a composed operation tree inside a broader block — see [Composing Operations](./composing-operations).

For void operations — DDL, schema setup, or any connection-consuming function that doesn't return a value — use `executeVoid`:

<Snippet file="core/ExecuteVoid" />

## Operation Modifiers

Every operation supports these modifiers before execution:

| Modifier | Effect |
|----------|--------|
| `.named("findUser")` | Prepends `/* findUser */` to the SQL — visible in `pg_stat_activity`, slow query logs, and [listener callbacks](./observability) |
| `.timeout(Duration.ofSeconds(5))` | Sets a query timeout via `Statement.setQueryTimeout()` |
| `.withListener(listener)` | Attaches a [QueryListener](./observability) to this specific operation |
| `.map(row -> transform(row))` | Transforms the result after execution |
| `.voided()` | Discards the result, returning `Operation<Void>` |

## Composing Operations

Operations can be composed as values — combined, sequenced, and chained — so that multiple database actions run in a single transaction without manual connection handling. For the full set of combinators (`.combineWith()`, `.then()`, `Operation.sequence()`, `Operation.ifEmpty()`, and more), see [Composing Operations](./composing-operations).

Here's a quick taste — `.combineWith()` combines two independent operations:

<Snippet file="core/ExecuteComposed" />

[Query Analysis](./query-analysis) can walk an entire composed operation tree and verify every SQL statement in one call.
