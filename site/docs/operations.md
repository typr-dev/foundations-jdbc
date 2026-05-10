---
title: Operations
---

import Snippet from '@site/src/components/Snippet';

# Operations

Call `.query()` or `.update()` on a Fragment to get an `OperationRead<T>` or `Operation<T>` — a database action that produces a value of type `T`. It doesn't run until you call `.transactRead(tx)` / `.transact(tx)` or `tx.execute(op)`.

## Queries

Read rows from the database. Pass a `RowCodec` with a result mode to control how the ResultSet is consumed. For single-column results, shorthand methods skip the codec:

<Snippet file="core/OperationQueries" />

## Updates

Write to the database: INSERT, UPDATE, DELETE, or DDL.

<Snippet file="core/OperationUpdates" />

## Returning rows from updates

Use `INSERT ... RETURNING` or `UPDATE ... RETURNING` to run a write and read back the affected rows:

<Snippet file="core/OperationReturning" />

## Execute (no result)

When you don't need the row count — DDL statements, fire-and-forget DML — use `.execute()` instead of `.update()`. It returns `Operation<Void>` (Java) / `Operation<Unit>` (Kotlin) / `Operation[Unit]` (Scala):

```java
Fragment.of("CREATE TABLE users (id INT, name VARCHAR)").execute()
```

This is equivalent to `.update().voided()`.

## Running operations

Use a [Transactor](./transactors) to obtain a connection, run the operation, and handle commit/rollback automatically:

<Snippet file="core/ExecuteTransact" />

For multiple operations in a single transaction, execute each one inside a `transact` block:

<Snippet file="core/ManualTransaction" />

For void operations — DDL, schema setup — use `mc.update()` inside a `transact` block:

<Snippet file="core/ExecuteVoid" />

## Operation modifiers

Every operation supports these modifiers before execution:

| Modifier | Effect |
|----------|--------|
| `.named("findUser")` | Prepends `/* findUser */` to the SQL — visible in `pg_stat_activity`, slow query logs, and [listener callbacks](./observability) |
| `.timeout(Duration.ofSeconds(5))` | Sets a query timeout via `Statement.setQueryTimeout()` |
| `.withListener(listener)` | Attaches a [QueryListener](./observability) to this specific operation |
| `.map(row -> transform(row))` | Transforms the result after execution |
| `.voided()` | Discards the result |

## Composing operations

Operations compose as values: combine, sequence, and chain them so that multiple database actions run in a single transaction without manual connection handling. For the full set of combinators (`.combineWith()`, `.then()`, `OperationRead.sequence()`, `OperationRead.ifEmpty()`, and more), see [Composing Operations](./composing-operations).

`.combineWith()` combines two independent operations:

<Snippet file="core/ExecuteComposed" />

[Query Analysis](./query-analysis) can walk an entire composed operation tree and verify every SQL statement in one call.
