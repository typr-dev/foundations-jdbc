---
title: JSON
---

import Snippet from '@site/src/components/Snippet';

# JSON

Every supported database has a JSON type: PostgreSQL `json`/`jsonb`, MySQL/MariaDB `JSON`, DuckDB `JSON`, SQL Server `FOR JSON`, Oracle's own JSON type. Your `RowCodec` doubles as a JSON codec with no extra code.

## JSON-encoded column types

Pass a `RowCodec` to your database's `jsonArrayEncoded` or `jsonObjectEncoded` method to get a column type that reads and writes structured rows as JSON. An unnamed codec can produce positional JSON arrays (`[value1, value2, ...]`), while a [named codec](./row-codecs) can also produce keyed JSON objects (`{"column": value, ...}`):

<Snippet file="core/NamedJsonObject" />

Every database type palette has four methods:

| Method | Codec | JSON shape | Use case |
|--------|-------|-----------|----------|
| `jsonObjectEncoded(namedCodec)` | `DbType<Row>` | `{"col": val, ...}` | Single row as keyed object |
| `jsonArrayEncoded(codec)` | `DbType<Row>` | `[val, val, ...]` | Single row as positional array |
| `jsonObjectEncodedList(namedCodec)` | `DbType<List<Row>>` | `[{"col": val}, ...]` | List of rows as object array |
| `jsonArrayEncodedList(codec)` | `DbType<List<Row>>` | `[[val, val], ...]` | List of rows as nested arrays |

Available on `PgTypes`, `MariaTypes`, `DuckDbTypes`, `SqlServerTypes`, `OracleTypes`, and `Db2Types`. PostgreSQL also has `jsonb` variants (`jsonbObjectEncoded`, etc.).

## Aggregating child rows as JSON

JSON-encoded types are most useful when aggregating child rows in SQL. Instead of N+1 queries, use your database's JSON aggregation function and parse the result with the same type:

| Database | Aggregation function |
|----------|---------------------|
| PostgreSQL | `json_agg()` / `jsonb_agg()` |
| MariaDB/MySQL | `JSON_ARRAYAGG()` |
| DuckDB | `json_group_array()` |
| SQL Server | `FOR JSON PATH` |
| Oracle | `JSON_ARRAYAGG()` |
| DB2 | `JSON_ARRAYAGG()` |

<Snippet file="core/JsonAggregation" />

The type reads the same types your `RowCodec` defines. There is no separate deserialization layer and no drift between your SQL types and your JSON types.
