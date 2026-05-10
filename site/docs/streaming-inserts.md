---
title: Streaming Inserts
---

import Snippet from '@site/src/components/Snippet';

# Streaming inserts

:::info PostgreSQL only
Streaming inserts use PostgreSQL's `COPY FROM STDIN` protocol. The feature is not available on other databases.
:::

Streaming inserts use PostgreSQL's COPY protocol to load data much faster than individual INSERT statements. Rows are text-encoded in batches and streamed to the server, bypassing prepared-statement overhead.

`StreamingInsert.of()` returns an `Operation<Long>` that can be transacted like any other operation. The COPY participates in the current transaction, so you can compose it atomically with other operations.

## Single column

For one-column tables, use the `PgText` encoder from the type directly:

<Snippet file="postgresql/StreamingInsertSingle" />

| Parameter | Description |
|-----------|-------------|
| `copyCommand` | A PostgreSQL `COPY ... FROM STDIN` command |
| `batchSize` | Number of rows to buffer before flushing to the server |
| `rows` | An `Iterator` over your data |
| `text` | A `PgText<T>` encoder for your row type |

## Multi-column rows

For rows with multiple columns, derive a `PgText` encoder from a `RowCodec`:

<Snippet file="postgresql/StreamingInsertMulti" />

`PgText.from(rowCodec)` uses each column's text encoder to produce tab-delimited COPY format. The same `RowCodec` you use for reading rows can drive bulk loading.

## Supported types

Most PostgreSQL types support text encoding for COPY. Types that don't (such as `jsonb`) will throw `UnsupportedOperationException` at encode time.

## Batch size

The `batchSize` parameter controls how many rows are buffered in memory before flushing to PostgreSQL. A larger batch means fewer network round-trips but more memory. Try a value between 1000 and 10000 as a starting point.
