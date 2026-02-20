---
title: Row Parsers
---

import Snippet from '@site/src/components/Snippet';

# Row Parsers

Reading rows from JDBC means calling `rs.getInt(1)`, `rs.getString(2)`, `rs.getTimestamp(3)` — column by column, in the right order, with the right types. Get any of it wrong and you get a `ClassCastException` at runtime. Add a column to your query and you silently shift every index after it.

A `RowParser<T>` replaces all of that with a single declaration: you list the database types and a constructor, and the parser does the rest. Once defined, the same parser drives everything the library does with your type:

- **Reading** — decodes rows from a `ResultSet` (queries) or a `CallableStatement` (stored procedures)
- **Writing** — encodes values into a `PreparedStatement` for inserts, updates, and batch operations
- **Streaming** — feeds rows into the PostgreSQL COPY protocol for [high-throughput inserts](./streaming-inserts)
- **JSON** — round-trips your type to and from [JSON objects](./json) using column names as keys
- **Analysis** — [Query Analysis](./query-analysis) inspects the parser's types to verify them against the database schema

You define the mapping once, and it propagates everywhere.

You build a parser by listing `.field()` calls — one per column, in SELECT order — and finishing with `.build(constructor)`:

<Snippet file="core/RowParserBasic" />

Each `.field()` takes a `DbType` that models the exact database column type. `DbType<A>` knows how to read a value of type `A` from a ResultSet and write it to a PreparedStatement — no JDBC integer codes, no manual `rs.getX()` calls. Each supported database has its own set (`PgTypes`, `DuckDbTypes`, `MariaDbTypes`, etc.) with full-precision mappings for every type. See [Database Types](./database-types) for the complete catalog.

The builder is fully type-safe: the constructor receives exactly the types you declared, with no casts. Columns are read by index — the order of `.field()` calls must match the column order in your SELECT.

## Single-Column Parser

For single-column queries, use the simpler `of()` factory:

<Snippet file="core/SingleColumnParser" />

## Nullable Columns

Use `.opt()` to wrap a type for nullable columns:

<Snippet file="core/NullableColumns" />

## Composing Parsers for Joins

Row parsers compose for joins. Given a `productParser` and a `categoryParser`, combine them with `.joined()` or `.leftJoined()`:

<Snippet file="core/ComposingParsers" />

The result type is `And<A, B>` in Java (with `.left()` and `.right()` accessors), `Pair<A, B>` in Kotlin, and a tuple `(A, B)` in Scala. Left join wraps the right side in `Optional` (or nullable in Kotlin, `Option` in Scala).

This is why row parsers use index-based reading rather than column names. When you join two tables, both may have columns named `id` or `name`. Column-name-based reading would silently return the wrong value. Index-based reading makes composition safe — each parser reads its own slice of columns in sequence, and name clashes are irrelevant.

## Named Row Parsers

The parsers above only track types. A *named* row parser also tracks column names — same index-based reading, but with metadata that eliminates hand-written column strings throughout your code:

<Snippet file="core/NamedRowParser" />

Having names lets you:

- **`columnList()`** — emit column names as a `Fragment` for SELECT clauses, so queries stay in sync with the parser
- **`columnNames()`** — get column names as a list
- **`fragment.row(parser, value)`** — emit an object's fields as comma-separated parameters for INSERT
- **`DbJsonRow.jsonObject(parser)`** — build a [JSON object codec](./json) with column names as keys

Named parsers are the recommended default — the small overhead of naming fields pays for itself quickly.

## Data-Driven Inserts

Named parsers enable a pattern where `fragment.row()` emits an object's fields as parameters, driven by the parser's column and type metadata. Pass column names to `except` to skip columns handled by the database:

<Snippet file="core/FragmentRow" />
