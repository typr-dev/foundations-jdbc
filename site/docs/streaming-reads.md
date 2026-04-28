---
title: Streaming Reads
---

import Snippet from '@site/src/components/Snippet';

# Streaming Reads

For large result sets, collecting all rows into a `List` can cause out-of-memory errors. Streaming reads return a `Cursor`, a lazy iterator that fetches rows from the database in batches while the connection stays open.

`streamingQuery` returns an `OperationRead<Cursor<Row>>`. The cursor is live during the `map` callback, where you process rows incrementally. All the usual combinators (`map`, `combine`, `transactRead`) work as expected.

## Basic usage

The simplest pattern: stream rows and collect them with `toList()`.

<Snippet file="core/StreamingReadBasic" />

| Parameter | Description |
|-----------|-------------|
| `codec` / `type` | A `RowCodec` or `DbType` describing the row shape |
| `fetchSize` | Number of rows the JDBC driver fetches per network round-trip |

## Processing rows lazily

The main benefit is processing rows one at a time inside `map`, without holding the full result set in memory:

<Snippet file="core/StreamingReadProcess" />

## Combining cursors

Multiple streaming operations compose with `combine`. Both cursors are open simultaneously on the same connection:

<Snippet file="core/StreamingReadCombine" />

You can also combine streaming operations with regular (non-streaming) operations. Just `map` the cursor first:

```java
streaming.map(Cursor::toList).combine(countOp).transactRead(tx);
```

## Cursor lifecycle

:::danger Don't return a Cursor from transactRead
The cursor borrows the connection. When `transactRead` returns, the connection is closed and the cursor becomes unusable. Always process the cursor inside `map`:
:::

<Snippet file="core/StreamingReadFootgun" />

## Fetch size

The `fetchSize` parameter controls how many rows the JDBC driver buffers per network round-trip. A larger fetch size means fewer round-trips but more memory per batch.

- **PostgreSQL**: requires `autoCommit=false` (the default `Transactor` strategy already sets this). Rows are fetched from the server in batches of `fetchSize`.
- **MySQL/MariaDB**: use `Integer.MIN_VALUE` for true row-by-row streaming. Note: only one open cursor per connection.
- **Oracle / SQL Server / DB2**: standard `setFetchSize()` works as expected.
- **DuckDB**: `setFetchSize()` is ignored; all rows are loaded regardless. The API still works, but there is no memory benefit.

A value between 256–2048 is a reasonable starting point for most databases.
