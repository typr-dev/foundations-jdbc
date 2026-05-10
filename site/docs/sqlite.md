---
title: SQLite Types
---

import Snippet from '@site/src/components/Snippet';

# SQLite type support

Foundations JDBC supports SQLite via the [xerial sqlite-jdbc](https://github.com/xerial/sqlite-jdbc) driver. SQLite's type system is small: five storage classes (NULL, INTEGER, REAL, TEXT, BLOB) plus declared-type "affinity" hints. This page covers a smaller surface than the other dialects.

:::warning SQLite is dynamically typed
By default, every column accepts every storage class regardless of the declared type. `INSERT` into an `INTEGER` column accepts a string, an `INTEGER PRIMARY KEY` accepts a float, and `VARCHAR(10)` accepts a 10MB string. The only exception is STRICT tables (`CREATE TABLE … STRICT`, since SQLite 3.37), which enforce the declared type. Use STRICT tables for any new schema you create with this library. It's the only way to get SQLite to enforce types the way the other supported databases do. See [STRICT Tables](https://www.sqlite.org/stricttables.html).
:::

:::note Date/time storage is text-based by default
The xerial driver writes `DATE` / `DATETIME` / `TIMESTAMP` columns as ISO-8601 TEXT (`yyyy-MM-dd HH:mm:ss.SSS`). Foundations JDBC follows that convention: `SqliteTypes.date`, `.datetime`, and `.instant` all read and write text. If you set `dateClass(INTEGER)` or `dateClass(REAL)` on `SqliteConfig`, you must supply your own date/time bindings.
:::

## Storage classes & type affinity

Per [SQLite docs §2–3](https://www.sqlite.org/datatype3.html), every value belongs to one of five storage classes (NULL, INTEGER, REAL, TEXT, BLOB) regardless of column declarations. The declared type only suggests an "affinity":

| Affinity | Triggered by declared text containing... |
|----------|------------------------------------------|
| INTEGER  | `INT` (matches `INTEGER`, `BIGINT`, `INT8`, `MEDIUMINT`, …) |
| TEXT     | `CHAR`, `CLOB`, or `TEXT` (matches `VARCHAR`, `NCHAR`, `CLOB`, …) |
| BLOB     | `BLOB`, or no declaration at all |
| REAL     | `REAL`, `FLOA`, or `DOUB` |
| NUMERIC  | anything else (default) |

Each `SqliteTypes` value below picks one canonical declared name and registers the common SQL aliases as vendor-type names so [query analysis](./query-analysis) accepts equivalent declarations (e.g. `BIGINT`, `INT2` → `integer`; `VARCHAR(100)`, `CLOB` → `text`).

## Integer types

| SQLite Type | Java Type | Aliases recognised by query analysis |
|-------------|-----------|--------------------------------------|
| `INTEGER` | `Long` | `int`, `int2`, `int4`, `int8`, `bigint`, `smallint`, `tinyint`, `mediumint`, `unsigned big int` |
| `BIGINT` | `Long` | (same set) |
| `INT` | `Integer` | (same set) |
| `SMALLINT` | `Short` | (same set) |
| `TINYINT` | `Byte` | (same set) |

<Snippet file="sqlite/IntegerTypes" />

## Boolean

| SQLite Type | Java Type | Notes |
|-------------|-----------|-------|
| `BOOLEAN` | `Boolean` | Stored as `INTEGER` 0/1; the `TRUE`/`FALSE` keywords work since SQLite 3.23.0 |

<Snippet file="sqlite/BoolType" />

## Floating-point types

| SQLite Type | Java Type | Notes |
|-------------|-----------|-------|
| `REAL` | `Double` | 8-byte IEEE 754 (canonical) |
| `DOUBLE` / `DOUBLE PRECISION` | `Double` | Aliases |
| `FLOAT` | `Float` | Stored as REAL — SQLite has no 32-bit float storage class |

<Snippet file="sqlite/RealTypes" />

## Numeric / decimal

| SQLite Type | Java Type | Notes |
|-------------|-----------|-------|
| `NUMERIC` | `BigDecimal` | Arbitrary precision; SQLite does not enforce `(p,s)` |
| `DECIMAL(p,s)` | `BigDecimal` | Same — `(p,s)` is a documentation label |

`BigDecimal` is bound and read as plain text. The xerial driver's `setBigDecimal`/`getBigDecimal` are unimplemented (they throw `column -1 out of bounds`), so the library falls back to `setString` + `BigDecimal::toPlainString` and reads via `getString`. Precision is preserved exactly.

<Snippet file="sqlite/NumericTypes" />

## String types

| SQLite Type | Java Type | Notes |
|-------------|-----------|-------|
| `TEXT` | `String` | UTF-8 (canonical) |
| `VARCHAR(n)` | `String` | `n` is a label; SQLite does not enforce length |
| `CHAR(n)` | `String` | Same — label only |
| `CLOB` | `String` | TEXT alias |

<Snippet file="sqlite/StringTypes" />

## Binary types

| SQLite Type | Java Type |
|-------------|-----------|
| `BLOB` / `BINARY` / `VARBINARY` | `byte[]` |

<Snippet file="sqlite/BinaryTypes" />

## Date/time types

SQLite has no date/time storage class. Foundations JDBC writes everything as ISO-8601 TEXT, the xerial driver default. Reads parse the text back into the Java type.

| SQLite Type | Java Type | Wire format |
|-------------|-----------|-------------|
| `DATE` | `LocalDate` | `yyyy-MM-dd` |
| `TIME` | `LocalTime` | `HH:mm[:ss[.fraction]]` |
| `DATETIME` / `TIMESTAMP` | `LocalDateTime` | `yyyy-MM-dd HH:mm:ss.SSS` (or `T` separator) |
| `TIMESTAMP` (UTC) | `Instant` | `yyyy-MM-ddTHH:mm:ss[.fraction]Z` |

<Snippet file="sqlite/DateTimeTypes" />

:::note Sub-millisecond precision is truncated
The default `LocalDateTime` writer formats with `.SSS` precision. Microseconds and nanoseconds are silently dropped on write. `Instant` round-trips at full nanosecond precision because `Instant.toString()` emits whatever precision the value carries.
:::

## UUID

| SQLite Type | Java Type | Storage |
|-------------|-----------|---------|
| `UUID` | `java.util.UUID` | Canonical 36-character TEXT |

<Snippet file="sqlite/UuidType" />

## JSON

| SQLite Type | Java Type | Notes |
|-------------|-----------|-------|
| `JSON` | `Json` | Stored as TEXT; use SQLite's built-in JSON1 functions (`json()`, `json_extract()`, `->`, `->>`) |

<Snippet file="sqlite/JsonType" />

SQLite's [JSON1 extension](https://www.sqlite.org/json1.html) is built into the engine since 3.38 (Feb 2022). A binary `JSONB` form exists since 3.45: it's stored as BLOB and accessed via `jsonb_*` functions. foundations-jdbc does not yet expose a separate type for it, so use `SqliteTypes.blob` if you need the raw binary form.

## Enums

SQLite has no native enum type. The library encodes Java enums as TEXT. Pair the column with a `CHECK (col IN (...))` constraint in DDL for static enforcement.

<Snippet file="sqlite/EnumType" />

## Nullable types

Any type can be made nullable via `.opt()`:

<Snippet file="sqlite/NullableTypes" />

## Custom domain types

Wrap base types with custom Java types using `transform`:

<Snippet file="sqlite/DomainType" />

## What SQLite doesn't have

For parity with the other dialect pages, here's what's deliberately absent:

| Feature | SQLite equivalent |
|---------|-------------------|
| Arrays (`int[]`, `text[]`) | None — use JSON arrays or junction tables |
| Composite/STRUCT/OBJECT types | None — use JSON or denormalised columns |
| Maps | None — use JSON objects |
| Ranges | None |
| Intervals | None — store as INTEGER seconds or TEXT ISO-8601 |
| Network types (`inet`, `cidr`, `macaddr`) | None — use TEXT |
| Geometry/GIS | Requires the SpatiaLite extension |
| Stored procedures / `CallableStatement` | None — `SqliteType.outParam()` always returns `Optional.empty()` |
| `COPY` / streaming bulk insert | None — use prepared `INSERT` in a transaction with `addBatch()` |
| Unsigned integers | None — INTEGER is signed 64-bit |

## Connection configuration

Build a config with `SqliteConfig.builder("path/to/file.db")`, `SqliteConfig.inMemory()` (one connection only; each `getConnection(":memory:")` opens an independent database), or `SqliteConfig.sharedInMemory()` for `file::memory:?cache=shared`.

Common options:

- `.foreignKeys(true)`: SQLite has foreign-key enforcement off by default. Turn it on per connection.
- `.journalMode("WAL")` + `.synchronous("NORMAL")`: recommended for concurrent readers.
- `.busyTimeoutMs(5000)`: wait this long when the database is locked instead of failing immediately.
- `.dateClass(DateClass.TEXT)`: keep the default (the codecs assume it). Change only if you supply your own bindings.

## Query analysis

[Query analysis](./query-analysis) works on SQLite. Column types are matched against the affinity-aware vendor-name aliases registered on each `SqliteType`. The xerial driver's `ResultSetMetaData.getColumnTypeName()` reports the literal text from `CREATE TABLE`, which the analyzer normalises (strips precision, lowercases) and compares against the type's alias set.

For empty result sets the driver may report `NULL` (0) as the column type. The analyzer treats that as "unknown" and skips the type assertion rather than failing. Pre-create some sample rows in your test fixture if you want type checking on every column.
