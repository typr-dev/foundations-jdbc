---
title: Operations
---

import Snippet from '@site/src/components/Snippet';

# Operations

An `Operation<T>` is a database action that produces a value of type `T`. You create one by calling a terminal method on a [Fragment](./fragments):

| Method | Returns |
|--------|---------|
| `.query(parser)` | `Operation<T>` — a SELECT that reads rows using the given result set parser |
| `.update()` | `Operation<Int>` — an INSERT/UPDATE/DELETE returning the affected row count |
| `.execute()` | `Operation<Void>` — DDL or DML where the row count is irrelevant |

Operations are values — they describe *what* to do but don't run anything until you execute them on a connection.

## Result Set Parsers

A `ResultSetParser<T>` reads a complete ResultSet and produces a value of type `T`. You typically create one from a `RowParser`:

<Snippet file="core/ResultSetParserUsage" />

From any `RowParser<T>` you can create:

| Method | Returns | Description |
|--------|---------|-------------|
| `.all()` | `List<T>` | All rows as a list |
| `.maxOne()` | `Optional<T>` / `T?` / `Option[T]` | Zero or one row (throws if more than one) |
| `.exactlyOne()` | `T` | Exactly one row (throws otherwise) |

## Running Operations

The transactor manages connections and transactions. Call `.transact` to obtain a connection, run your code, and commit:

<Snippet file="core/ExecuteTransact" />

For multiple operations in a single transaction, call `.run(conn)` on each one inside the same block:

<Snippet file="core/ManualTransaction" />

## Composing Operations

Operations can be composed as values — combined, sequenced, and chained — so that multiple database actions run in a single transaction without manual connection handling. For the full set of combinators (`.with()`, `.then()`, `Operation.sequence()`, `Operation.ifEmpty()`, and more), see [Composing Operations](./composing-operations).

Here's a quick taste — `.with()` combines two independent operations:

<Snippet file="core/ExecuteComposed" />

[Query Analysis](./query-analysis) can walk an entire composed operation tree and verify every SQL statement in one call.
