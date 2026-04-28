---
title: Row Codecs
---

import Snippet from '@site/src/components/Snippet';

# Row Codecs

Reading rows from JDBC means calling `rs.getInt(1)`, `rs.getString(2)`, `rs.getTimestamp(3)` -- column by column, in the right order, with the right types. Get any of it wrong and you get a `ClassCastException` at runtime. Add a column to your query and you silently shift every index after it.

A `RowCodec<T>` replaces all of that with a single declaration: list the database types and a constructor, and the codec does the rest. The same codec drives everything the library does with your type:

- **Reading** — decodes rows from a `ResultSet` (queries) or a `CallableStatement` (stored procedures)
- **Writing** — encodes values into a `PreparedStatement` for inserts, updates, and batch operations
- **Streaming** — feeds rows into the PostgreSQL COPY protocol for [high-throughput inserts](./streaming-inserts)
- **JSON** — round-trips your type to and from [JSON objects](./json) using column names as keys
- **Analysis** — [Query Analysis](./query-analysis) inspects the codec's types to verify them against the database schema, including IN/OUT parameters of stored procedures and functions

You define the mapping once, and it propagates everywhere.

Foundations JDBC has two kinds of row codecs:

1. **Named codecs** (`RowCodecNamed`) — track column names, recommended for most use cases. Created via `RowCodec.namedBuilder()`.
2. **Positional codecs** (`RowCodecUnnamed`) — track only column positions, for quick single/multi-column reads. Created via `RowCodec.of(type)` or `RowCodec.builder()`.

## Named row codecs

A *named* row codec tracks both types and column names. This is the recommended default:

<Snippet file="core/NamedRowCodec" />

:::important Column names are NOT used for reading
Row codecs **always read by column index**, never by column name. The column names in a named codec exist for **SQL generation** (INSERT statements, column lists), **JSON encoding** (object keys), and **composite types** (field names). They are never passed to `ResultSet.getString("name")`. When the codec reads a row, it calls `rs.getXxx(1)`, `rs.getXxx(2)`, etc. in declaration order.

This is a deliberate design choice that will not change. Index-based reading is the only option because it composes safely: when you join two tables, both may have columns named `id` or `name`. Column-name-based reading would silently return the wrong value. Index-based reading makes composition safe -- each codec reads its own slice of columns in sequence, and name clashes are irrelevant.
:::

Having names lets you:

- **`columnList()`** — emit column names as a `Fragment` for SELECT clauses, so queries stay in sync with the codec
- **`columnNames()`** — get column names as a list
- **`Fragment.insertOne(table, codec, row)` / `insertMany(table, codec, rows)`** — execute INSERT directly from the codec's column metadata
- **`Fragment.insertOneReturning(table, codec, row)`** — same, with a `RETURNING` clause that parses the inserted row back
- **`fragment.row(codec, value)`** — emit an object's fields as comma-separated parameters for custom INSERT patterns
- **`DbJsonRow.jsonObject(codec)`** — build a [JSON object codec](./json) with column names as keys

## Single-column codec

For single-column queries, use the simpler `of()` factory:

<Snippet file="core/SingleColumnCodec" />

For a single named column (preserving the column name for joins), use `RowCodec.ofNamed("name", type)`.

## Nullable columns

Use `.opt()` to wrap a type for nullable columns:

<Snippet file="core/NullableColumns" />

## Composing codecs for joins

Row codecs compose for joins. Given a `productCodec` and a `categoryCodec`, combine them with `.join()` or `.leftJoin()`:

<Snippet file="core/ComposingCodecs" />

The result type is `Tuple2<A, B>` in Java (with `._1()` and `._2()` accessors), `Pair<A, B>` in Kotlin, and a tuple `(A, B)` in Scala. Left join wraps the right side in `Optional` (or nullable in Kotlin, `Option` in Scala).

Named codecs have `.join()` and `.leftJoin()` methods that preserve column names, so the combined codec still works with `columnList()`, `Fragment.insertOne()`, and JSON encoding.

The same `Tuple` types appear whenever the library needs to return multiple values without a dedicated record type — `RowCodec.of(type1, type2, ...)` for multi-column ad-hoc queries, `.combine()` for composed operations, and `.join()` for joins all return `TupleN`. Accessors are 1-based: `._1()`, `._2()`, `._3()`, etc.

### Disambiguating duplicate column names

If the two sides of a named join share any column name (the common `id`, `name`, `created_at` case), the joined codec's `columnList()` concatenates the names and throws:

```
RowCodecNamed.columnList() has duplicate column names [id, name]
(typically from a join of codecs sharing a column name). Call .aliased("x")
on each side before composing, ...
```

Call `.aliased("alias")` on each side *before* composing. Aliasing prefixes every column name with `alias.`, so the joined codec has unambiguous entries (`e.id, e.name, ..., d.id, d.name`) and `columnList()` is usable directly in the SELECT:

```java
var joined = empCodec.aliased("e").leftJoin(deptCodec.aliased("d"));
Fragment.of("SELECT ")
    .append(joined.columnList())
    .append(" FROM emp e LEFT JOIN dept d ON e.department = d.name")
    .query(joined.all());
```
```kotlin
val joined = empCodec.aliased("e").leftJoin(deptCodec.aliased("d"))
sql { "SELECT ${joined.columnList} FROM emp e LEFT JOIN dept d ON e.department = d.name" }
    .query(joined.all())
```

An aliased codec is SELECT-friendly but not INSERT-friendly — use the unaliased codec for `Fragment.insertOne(table, codec, row)`.

## Result modes

A codec defines the shape of a row. Result modes define how many rows the query returns:

| Mode | Returns | Behavior |
|------|---------|----------|
| `.all()` | `List<T>` | Collect all rows |
| `.exactlyOne()` | `T` | Expect exactly one row, throw if 0 or 2+ |
| `.maxOne()` | `Optional<T>` | Return 0 or 1 rows |

## Data-driven inserts

`Fragment.insertOneReturning()` executes a complete INSERT from a named codec. The column list, bound values, and RETURNING clause are all driven by the codec. Pass column names to `except` to skip columns with database defaults. Use `insertMany` for batch inserts.

<Snippet file="core/FragmentRow" />

## Generated keys inserts

For databases that don't support `RETURNING` (DB2, Oracle, SQL Server, MariaDB), use `Fragment.insertOneGenerated()`. It works like `insertOneReturning` but uses JDBC's `getGeneratedKeys()` API to read back the generated columns:

<Snippet file="core/FragmentRowGeneratedKeys" />

Pass the generated column names as the third argument, and the same column names to `except` so they're excluded from the INSERT's column list and VALUES clause.

## Positional codecs

Use positional codecs when column names aren't needed, such as single-use queries. Build a positional codec by listing `.field()` calls (one per column, in SELECT order) and finishing with `.build(constructor)`:

<Snippet file="core/RowCodecBasic" />

Each `.field()` takes a `DbType` that models the exact database column type. `DbType<A>` reads a value of type `A` from a ResultSet and writes it to a PreparedStatement. Each supported database has its own set (`PgTypes`, `DuckDbTypes`, `MariaDbTypes`, etc.) with mappings for every type. See [Database Types](./database-types) for the complete catalog.

The builder is fully type-safe: the constructor receives exactly the types you declared, with no casts. Columns are read by index — the order of `.field()` calls must match the column order in your SELECT.
