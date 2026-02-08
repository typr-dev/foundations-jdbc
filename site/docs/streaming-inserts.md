---
title: Streaming Inserts
---

import Snippet from '@site/src/components/Snippet';

# Streaming Inserts

:::info PostgreSQL Only
Streaming inserts use PostgreSQL's `COPY FROM STDIN` protocol. This feature is not available for other databases.
:::

Streaming inserts use PostgreSQL's COPY protocol to load data significantly faster than individual INSERT statements. Data is text-encoded in batches and streamed directly to the server, bypassing the overhead of prepared statements.

`streamingInsert.of()` returns an `Operation<Long>` that can be transacted like any other operation. The COPY participates in the current transaction, so it can be composed with other operations atomically.

## Single Column

For simple cases, use the `PgText` encoder from the type directly:

<Snippet file="postgresql/StreamingInsertSingle" />

| Parameter | Description |
|-----------|-------------|
| `copyCommand` | A PostgreSQL `COPY ... FROM STDIN` command |
| `batchSize` | Number of rows to buffer before flushing to the server |
| `rows` | An `Iterator` over your data |
| `text` | A `PgText<T>` encoder for your row type |

## Multi-Column Rows

For rows with multiple columns, derive a `PgText` encoder from a `RowParser`:

<Snippet file="postgresql/StreamingInsertMulti" />

`PgText.from(rowParser)` uses each column's text encoder to produce tab-delimited COPY format. The same `RowParser` you use for reading rows can drive bulk loading.

## Supported Types

Most PostgreSQL types support text encoding for COPY. Types that don't (such as `jsonb`) will throw `UnsupportedOperationException` at encode time.

## Batch Size

The `batchSize` parameter controls how many rows are buffered in memory before being flushed to PostgreSQL. A larger batch size reduces network round-trips but uses more memory. A value between 1000-10000 is a reasonable starting point.
