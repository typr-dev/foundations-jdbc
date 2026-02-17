---
title: JSON
---

import Snippet from '@site/src/components/Snippet';

# JSON

Every database supports JSON — PostgreSQL has `json`/`jsonb`, MySQL and MariaDB have `JSON`, DuckDB has `JSON`, SQL Server has `FOR JSON`, and Oracle has its own JSON type. Foundations gives you a unified way to work with JSON across all of them: your `RowParser` doubles as a JSON codec with zero extra code.

## JSON-Encoded Column Types

Pass a `RowParser` to your database's `jsonArrayEncoded` or `jsonObjectEncoded` method to get a column type that reads and writes structured rows as JSON. An unnamed parser can produce positional JSON arrays (`[value1, value2, ...]`), while a [named parser](./named-row-parsers) can also produce keyed JSON objects (`{"column": value, ...}`):

<Snippet file="core/NamedJsonObject" />

Every database has these methods:

| Database | Methods |
|----------|---------|
| PostgreSQL | `PgTypes.jsonArrayEncoded` / `jsonbArrayEncoded` + object variants |
| MariaDB/MySQL | `MariaTypes.jsonArrayEncoded` + object variants |
| DuckDB | `DuckDbTypes.jsonArrayEncoded` + object variants |
| SQL Server | `SqlServerTypes.jsonArrayEncoded` + object variants |
| Oracle | `OracleTypes.jsonArrayEncoded` + object variants |
| DB2 | `Db2Types.jsonArrayEncoded` + object variants |

Each method also has a `List` variant (e.g. `jsonArrayEncodedList`) for columns that hold a JSON array of rows.

## Aggregating Child Rows as JSON

The real power of JSON-encoded types shows when you aggregate child rows directly in SQL. Instead of N+1 queries, use your database's JSON aggregation function and parse the result with the same type:

| Database | Aggregation function |
|----------|---------------------|
| PostgreSQL | `json_agg()` / `jsonb_agg()` |
| MariaDB/MySQL | `JSON_ARRAYAGG()` |
| DuckDB | `json_group_array()` |
| SQL Server | `FOR JSON PATH` |
| Oracle | `JSON_ARRAYAGG()` |
| DB2 | `JSON_ARRAYAGG()` |

<Snippet file="core/JsonAggregation" />

The type reads the same types your `RowParser` defines — no separate deserialization layer, no mapping code, no drift between your SQL types and your JSON types.
