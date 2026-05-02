---
title: PostgreSQL Types
---

import Snippet from '@site/src/components/Snippet';

# PostgreSQL type support

Full support for all PostgreSQL data types, including arrays, ranges, geometric types, network types, JSON, composites, and enums.

## Setting `search_path`

Use `PgConfig.Builder.currentSchema(...)` to set a comma-separated `search_path` at connection time — handy when your composite types, enums, or tables live in a non-`public` schema and you want to reference them unqualified in SQL:

```java
var config = PgConfig.builder("host", 5432, "db", "user", "pw")
    .currentSchema("app,public")   // search_path = app, public
    .build();
```

This maps to PostgreSQL's `currentSchema` connection property. For single-schema use, [`ConnectionSettings.schema(...)`](./transactors#connection-settings) also works but only accepts one name.

## Numeric types

| PostgreSQL Type | Java Type | Notes |
|-----------------|-----------|-------|
| `int2` / `smallint` | `Short` | 16-bit signed integer |
| `int4` / `integer` | `Integer` | 32-bit signed integer |
| `int8` / `bigint` | `Long` | 64-bit signed integer |
| `float4` / `real` | `Float` | 32-bit IEEE 754 |
| `float8` / `double precision` | `Double` | 64-bit IEEE 754 |
| `numeric` / `decimal` | `BigDecimal` | Arbitrary precision |
| `money` | `Money` | Currency with 2 decimal places |

<Snippet file="postgresql/NumericTypes" />

## Boolean type

| PostgreSQL Type | Java Type |
|-----------------|-----------|
| `bool` / `boolean` | `Boolean` |

<Snippet file="postgresql/BoolType" />

## String types

| PostgreSQL Type | Java Type | Notes |
|-----------------|-----------|-------|
| `text` | `String` | Variable unlimited length |
| `varchar(n)` | `String` | Variable length with limit |
| `bpchar` / `char(n)` | `String` | Fixed-length, blank-padded |
| `name` | `String` | 63-character identifier |

<Snippet file="postgresql/StringTypes" />

## Binary types

| PostgreSQL Type | Java Type | Notes |
|-----------------|-----------|-------|
| `bytea` | `byte[]` | Variable-length binary |

<Snippet file="postgresql/BinaryTypes" />

## Date/Time Types {#datetime-rationale}

| PostgreSQL Type | Java Type | Notes |
|-----------------|-----------|-------|
| `date` | `LocalDate` | Naive date, no zone |
| `time` | `LocalTime` | Naive time, no zone |
| `timetz` | `OffsetTime` | Time with offset (rarely used in practice) |
| `timestamp` | `LocalDateTime` | Naive timestamp, no zone |
| `timestamptz` | `Instant` | **UTC instant** — see note below |
| `interval` | `PGInterval` | Time duration |

<Snippet file="postgresql/DateTimeTypes" />

:::note `timestamptz` does not store a time zone
PostgreSQL is explicit on this: for `timestamp with time zone`, "the value is stored internally as UTC, and the originally stated or assumed time zone is not retained" (from the [PostgreSQL docs](https://www.postgresql.org/docs/current/datatype-datetime.html)). The zone only affects how the value is rendered at read-time (always converted to the session `TimeZone` setting).

Because the column genuinely stores a universal instant — not a zoned value — the library maps it to `java.time.Instant`. Any zone information must travel alongside the value in a separate column if you need it (same data-modelling approach as Jira, GitHub, and most other systems). Using `OffsetDateTime` here would suggest the stored value carries an offset, which it does not.

This is the reference mapping for the whole library: DuckDB's `TIMESTAMPTZ` shares the same semantics and uses the same `Instant` mapping. SQL Server's `DATETIMEOFFSET` and Oracle's `TIMESTAMP WITH TIME ZONE` genuinely preserve offset/zone and therefore map differently — see each dialect's page for details.
:::

## UUID type

| PostgreSQL Type | Java Type |
|-----------------|-----------|
| `uuid` | `java.util.UUID` |

<Snippet file="postgresql/UuidType" />

## JSON types

| PostgreSQL Type | Java Type | Notes |
|-----------------|-----------|-------|
| `json` | `Json` | Stored as-is, validated on input |
| `jsonb` | `Jsonb` | Binary format, indexed, normalized |

:::tip Not String — wrapper types
`PgTypes.json` is `PgType<Json>` and `PgTypes.jsonb` is `PgType<Jsonb>`. These are **not** `String` — they are single-field wrapper records (`record Json(String value)`, `record Jsonb(String value)`) from `dev.typr.foundations.data`. This means your record fields must be typed `Jsonb`/`Json`, not `String`.
:::

`Json` and `Jsonb` are distinct wrapper records around a `String` payload, so a single row with both a `json` and a `jsonb` column keeps its types straight. Wrap the raw JSON text at the edges:

```java
import dev.typr.foundations.data.Jsonb;

new Jsonb("{\"ok\":true}")     // java
```
```kotlin
import dev.typr.foundations.data.Jsonb

Jsonb("""{"ok":true}""")        // kotlin
```

A common first-run surprise is declaring `val payload: String` on a Kotlin data class and getting `actual type is 'String', but 'Jsonb!' was expected` — the Kotlin `!` just marks a platform type, the fix is the wrap above.

<Snippet file="postgresql/JsonTypes" />

## Array types

Any PostgreSQL type can be used as an array — call `.array()` on the element type. The Java representation is always `List<T>`:

| PostgreSQL Type | Java Type |
|-----------------|-----------|
| `int4[]` | `List<Integer>` via `int4.array()` |
| `int8[]` | `List<Long>` via `int8.array()` |
| `float4[]` | `List<Float>` via `float4.array()` |
| `float8[]` | `List<Double>` via `float8.array()` |
| `bool[]` | `List<Boolean>` via `bool.array()` |
| `text[]` | `List<String>` via `text.array()` |
| `uuid[]` | `List<UUID>` via `uuid.array()` |

This works for all types — `numeric.array()`, `timestamptz.array()`, `jsonb.array()`, custom enum types, composite types, etc. Multi-dimensional arrays compose: `.array().array()` produces SQL `T[][]` with Java type `List<List<T>>`.

<Snippet file="postgresql/ArrayTypes" />

## Composite types

PostgreSQL composite types (row constructors and `CREATE TYPE` declarations) are built from a `RowCodecNamed` via `compositeOf`:

```java
// Ad-hoc composite — for row constructors like (a, b, c) in SQL
PgType<LineItem> lineItemType = PgTypes.compositeOf(
    RowCodec.<LineItem>namedBuilder()
        .field("product_name", PgTypes.text, LineItem::productName)
        .field("quantity", PgTypes.int4, LineItem::quantity)
        .field("unit_price", PgTypes.numeric, LineItem::unitPrice)
        .build(LineItem::new));

// Named composite — for CREATE TYPE declarations (supports writes)
PgType<Address> addressType = PgTypes.compositeOf("address", addressCodec);

// Array of composites — works like any other type
PgType<List<LineItem>> lineItemArrayType = lineItemType.array();
```

The same `RowCodecNamed` codec can be reused for flat row queries, composite types, JSON-encoded columns, and query analysis.

## Range types

PostgreSQL's range types represent intervals of values with inclusive/exclusive bounds:

| PostgreSQL Type | Java Type | Element Type |
|-----------------|-----------|--------------|
| `int4range` | `Range<Integer>` | Integer |
| `int8range` | `Range<Long>` | Long |
| `numrange` | `Range<BigDecimal>` | BigDecimal |
| `daterange` | `Range<LocalDate>` | LocalDate |
| `tsrange` | `Range<LocalDateTime>` | LocalDateTime |
| `tstzrange` | `Range<Instant>` | Instant |

<Snippet file="postgresql/RangeTypes" />

## Geometric types

PostgreSQL geometric types for 2D shapes:

| PostgreSQL Type | Java Type | Description |
|-----------------|-----------|-------------|
| `point` | `PGpoint` | (x, y) coordinate |
| `line` | `PGline` | Infinite line |
| `lseg` | `PGlseg` | Line segment |
| `box` | `PGbox` | Rectangular box |
| `path` | `PGpath` | Open or closed path |
| `polygon` | `PGpolygon` | Closed polygon |
| `circle` | `PGcircle` | Circle with center and radius |

<Snippet file="postgresql/GeometricTypes" />

## Network Types

Types for storing network addresses:

| PostgreSQL Type | Java Type | Description |
|-----------------|-----------|-------------|
| `inet` | `Inet` | IPv4 or IPv6 host address |
| `cidr` | `Cidr` | IPv4 or IPv6 network |
| `macaddr` | `Macaddr` | MAC address (6 bytes) |
| `macaddr8` | `Macaddr8` | MAC address (8 bytes, EUI-64) |

<Snippet file="postgresql/NetworkTypes" />

## Text Search Types

Full-text search types:

| PostgreSQL Type | Java Type | Description |
|-----------------|-----------|-------------|
| `tsvector` | `Tsvector` | Text search document |
| `tsquery` | `Tsquery` | Text search query |

<Snippet file="postgresql/TextSearchTypes" />

## XML Type

| PostgreSQL Type | Java Type |
|-----------------|-----------|
| `xml` | `Xml` |

<Snippet file="postgresql/XmlType" />

## Other Special Types

| PostgreSQL Type | Java Type | Description |
|-----------------|-----------|-------------|
| `hstore` | `Map<String, String>` | Key-value store |
| `vector` | `Vector` | pgvector extension |
| `record` | `Record` | Anonymous composite type |

<Snippet file="postgresql/SpecialTypes" />

## System Types

Types used internally by PostgreSQL:

| PostgreSQL Type | Java Type | Description |
|-----------------|-----------|-------------|
| `oid` | `Long` | Object identifier |
| `xid` | `Xid` | Transaction ID |
| `regclass` | `Regclass` | Relation name/OID |
| `regtype` | `Regtype` | Type name/OID |
| `regproc` | `Regproc` | Function name/OID |

## Enum Types

PostgreSQL enums are mapped to Java enums:

<Snippet file="postgresql/EnumType" />

:::note `sqlType` must match the `CREATE TYPE` name (schema-qualified if needed)
The first argument to `ofEnum(sqlType, ...)` is the PostgreSQL type name used to cast bound parameters. Pass exactly the name that appears in `CREATE TYPE schema.color AS ENUM(...)` — including the schema prefix if the type isn't in `search_path`. A mismatch produces `type "color" does not exist` on the first insert.
:::

## Custom Domain Types

Wrap base types with custom Java types using `transform`. Useful when you want a typed
wrapper on the application side without changing PG's schema:

<Snippet file="postgresql/DomainType" />

## PostgreSQL DOMAIN types

For an actual `CREATE DOMAIN dom AS underlying` schema-side type, use `asDomain`. The
two-arg form takes the domain name and a constructor / extractor for the wrapping value
type, so the entire DOMAIN-plus-wrapper declaration is one expression:

<Snippet file="postgresql/PgDomainTypeScalar" />

`asDomain` renames the typename for SQL rendering, registers the underlying typename as a
query-analyzer alias (PG JDBC resolves domains to their base type in `ResultSetMetaData`),
and configures the array codec to text-parse so domain arrays decode correctly. It also
covers domain over enum, domain over composite, etc. — the underlying codec is reused.

Arrays of a domain "just work" — wrap once at the scalar level and `.array()` carries the
wrapper through. No list-level bijection is needed:

<Snippet file="postgresql/PgDomainTypeArray" />

:::note Equality on domain-typed columns
PG does not always define operators on a domain in its own right (e.g. domain-over-enum has
no operator class — operators are bound to the enum's OID). For columns where you compare
on the domain, cast to the underlying: `WHERE v::underlying = $1::underlying`. Read/write
through the codec is unaffected.
:::

## Nullable Types

Any type can be made nullable using `.opt()`:

<Snippet file="postgresql/NullableTypes" />
