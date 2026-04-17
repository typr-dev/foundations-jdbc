---
title: DuckDB Types
---

import Snippet from '@site/src/components/Snippet';

# DuckDB Type Support

Foundations JDBC provides comprehensive support for DuckDB's rich type system, including nested types (LIST, STRUCT, MAP, UNION) and extended integer types.

:::warning Query Analysis on DuckDB is column-type-only
DuckDB's JDBC driver doesn't report column nullability or parameter metadata, so `QueryChecker`'s `.opt()` and wrong-parameter-type checks silently pass on DuckDB. Column type mismatches are still caught. If you test against DuckDB in-memory and deploy against PostgreSQL or another dialect, run analysis against the production dialect too — it will find issues DuckDB can't. See [Query Analysis: Database Behavior](./query-analysis-database-behavior) for the full matrix.
:::

## Integer Types (Signed)

| DuckDB Type | Java Type | Range |
|-------------|-----------|-------|
| `TINYINT` | `Byte` | -128 to 127 |
| `SMALLINT` | `Short` | -32,768 to 32,767 |
| `INTEGER` / `INT` | `Integer` | -2^31 to 2^31-1 |
| `BIGINT` | `Long` | -2^63 to 2^63-1 |
| `HUGEINT` | `BigInteger` | -2^127 to 2^127-1 |

<Snippet file="duckdb/IntegerTypesSigned" />

## Integer Types (Unsigned)

| DuckDB Type | Java Type | Range |
|-------------|-----------|-------|
| `UTINYINT` | `Uint1` | 0 to 255 |
| `USMALLINT` | `Uint2` | 0 to 65,535 |
| `UINTEGER` | `Uint4` | 0 to 2^32-1 |
| `UBIGINT` | `Uint8` | 0 to 2^64-1 |
| `UHUGEINT` | `BigInteger` | 0 to 2^128-1 |

<Snippet file="duckdb/IntegerTypesUnsigned" />

## Floating-Point Types

| DuckDB Type | Java Type | Notes |
|-------------|-----------|-------|
| `FLOAT` / `FLOAT4` / `REAL` | `Float` | 32-bit IEEE 754 |
| `DOUBLE` / `FLOAT8` | `Double` | 64-bit IEEE 754 |

<Snippet file="duckdb/FloatingPointTypes" />

## Fixed-Point Types

| DuckDB Type | Java Type | Notes |
|-------------|-----------|-------|
| `DECIMAL(p,s)` | `BigDecimal` | Arbitrary precision |
| `NUMERIC(p,s)` | `BigDecimal` | Alias for DECIMAL |

<Snippet file="duckdb/FixedPointTypes" />

## Boolean Type

| DuckDB Type | Java Type |
|-------------|-----------|
| `BOOLEAN` / `BOOL` | `Boolean` |

<Snippet file="duckdb/BoolType" />

## String Types

| DuckDB Type | Java Type | Notes |
|-------------|-----------|-------|
| `VARCHAR` / `STRING` / `TEXT` | `String` | Variable length |
| `CHAR(n)` | `String` | Fixed length |

<Snippet file="duckdb/StringTypes" />

## Binary Types

| DuckDB Type | Java Type |
|-------------|-----------|
| `BLOB` / `BYTEA` / `BINARY` / `VARBINARY` | `byte[]` |

<Snippet file="duckdb/BinaryTypes" />

## Bit String Type

| DuckDB Type | Java Type | Notes |
|-------------|-----------|-------|
| `BIT` / `BITSTRING` | `String` | String of 0s and 1s |

<Snippet file="duckdb/BitStringType" />

## Date/Time Types

| DuckDB Type | Java Type | Notes |
|-------------|-----------|-------|
| `DATE` | `LocalDate` | Naive date, no zone |
| `TIME` | `LocalTime` | Naive time, no zone |
| `TIMESTAMP` / `DATETIME` | `LocalDateTime` | Naive timestamp, no zone |
| `TIMESTAMP WITH TIME ZONE` | `Instant` | **UTC instant** — see note below |
| `TIME WITH TIME ZONE` | `OffsetDateTime` | Time with offset |
| `INTERVAL` | `Duration` | Time duration |

<Snippet file="duckdb/DateTimeTypes" />

:::note `TIMESTAMP WITH TIME ZONE` is not what the name suggests
Despite the SQL keyword, DuckDB does **not** store any zone or offset with a `TIMESTAMPTZ` value — it stores an `INT64` count of microseconds since the Unix epoch (see the [DuckDB timestamp docs](https://duckdb.org/docs/sql/data_types/timestamp)). The original offset or region is used only for parsing on input, then discarded. Reads render the instant in the session timezone, which is a display convenience, not persisted state.

Because the storage *is* a universal instant, the library maps this column to `java.time.Instant` — the Java type with the same semantics. Using `OffsetDateTime` would surface the JDBC driver's cosmetic "render in session offset" output as if it were data. `Instant` is identical in spirit to PostgreSQL's `timestamptz` mapping ([see `postgresql.md`](./postgresql#datetime-rationale)).

**Note:** `TIME WITH TIME ZONE` (TIMETZ) is a distinct case: DuckDB's JDBC driver *does* preserve the original offset on round-trip (verified empirically), so that column maps to `OffsetTime`, not `Instant`. The DuckDB CLI renders TIMETZ in session-TZ which can make it look normalized, but that's display-only.
:::

### Timestamp Precision Variants

| DuckDB Type | Java Type | Precision |
|-------------|-----------|-----------|
| `TIMESTAMP_S` | `LocalDateTime` | Seconds |
| `TIMESTAMP_MS` | `LocalDateTime` | Milliseconds |
| `TIMESTAMP` | `LocalDateTime` | Microseconds (default) |
| `TIMESTAMP_NS` | `LocalDateTime` | Nanoseconds |

<Snippet file="duckdb/TimestampPrecision" />

## UUID Type

| DuckDB Type | Java Type |
|-------------|-----------|
| `UUID` | `java.util.UUID` |

<Snippet file="duckdb/UuidType" />

## JSON Type

| DuckDB Type | Java Type |
|-------------|-----------|
| `JSON` | `Json` |

<Snippet file="duckdb/JsonType" />

## Enum Type

<Snippet file="duckdb/EnumType" />

:::note `sqlType` must match the declared type name
The first argument to `ofEnum(sqlType, ...)` is used to cast bound parameters (e.g. `CAST(? AS status)`) and must match the name used in `CREATE TYPE status AS ENUM(...)` and in the column's declared type. If the column is typed `status_t` but you call `ofEnum("status", ...)`, inserts fail with `Type with name status does not exist`.
:::

:::note Scala 3 enums need an explicit `extends`
The Scala wrapper's `ofEnum` method has the bound `[E <: java.lang.Enum[E]]`. Simple Scala 3 enums (no constructor parameters) extend `java.lang.Enum[T]` at the JVM level, but the Scala 3 type checker does not recognize this for the `ofEnum` bound unless you add the extension explicitly:

```scala
enum Status extends java.lang.Enum[Status]:
  case PENDING, ACTIVE, COMPLETED
```

Without the explicit `extends`, the call `DuckDbTypes.ofEnum[Status]("status", Status.valueOf)` fails with `Type argument Status does not conform to upper bound Enum[Status]`. Java enums and Kotlin `enum class` work without any extra clause.
:::

## LIST Types

Any type can be made into a variable-length list with `.list()`. DuckDB renders as `T[]` and the Java representation is `List<T>`:

| DuckDB Type | Java Type | Created via |
|-------------|-----------|-------------|
| `INTEGER[]` | `List<Integer>` | `integer.list()` |
| `VARCHAR[]` | `List<String>` | `varchar.list()` |
| `DATE[]` | `List<LocalDate>` | `date.list()` |
| ... | ... | `anyType.list()` |

<Snippet file="duckdb/ListTypes" />

## ARRAY Types

Fixed-size arrays use `.array(size)`. DuckDB enforces that every row has exactly `size` elements — ideal for embeddings, RGB colors, or any dense fixed-shape tensor. The Java representation is still `List<T>`:

| DuckDB Type | Java Type | Created via |
|-------------|-----------|-------------|
| `FLOAT[1536]` | `List<Float>` | `float_.array(1536)` |
| `INTEGER[3]` | `List<Integer>` | `integer.array(3)` |

<Snippet file="duckdb/ArrayTypes" />

## Nested Collections

LIST and ARRAY compose freely in any combination:

| SQL Type | Java Type | Created via |
|----------|-----------|-------------|
| `T[][]` | `List<List<T>>` | `t.list().list()` |
| `T[m][n]` | `List<List<T>>` | `t.array(n).array(m)` |
| `T[][n]` | `List<List<T>>` | `t.list().array(n)` |
| `T[m][]` | `List<List<T>>` | `t.array(m).list()` |

<Snippet file="duckdb/NestedCollections" />

## MAP Types

DuckDB's MAP type for key-value pairs:

| DuckDB Type | Java Type |
|-------------|-----------|
| `MAP(VARCHAR, INTEGER)` | `Map<String, Integer>` |
| `MAP(VARCHAR, VARCHAR)` | `Map<String, String>` |

<Snippet file="duckdb/MapTypes" />

## Struct Types

DuckDB STRUCT types are built from a `RowCodecNamed` via `compositeOf`:

```java
DuckDbType<Person> personType = DuckDbTypes.compositeOf("person",
    RowCodec.<Person>namedBuilder()
        .field("name", DuckDbTypes.varchar, Person::name)
        .field("age", DuckDbTypes.integer, Person::age)
        .build(Person::new));

// List of structs (variable-length)
DuckDbType<List<Person>> personListType = personType.list();

// Fixed-size array of structs (e.g. always 3 members)
DuckDbType<List<Person>> trioType = personType.array(3);
```

## UNION Types

DuckDB's UNION type for tagged unions:

```java
// Unions are typically handled via generated code
// The variants are defined by the table schema
```

## Nullable Types

Any type can be made nullable using `.opt()`:

<Snippet file="duckdb/NullableTypes" />

## Custom Domain Types

Wrap base types with custom Java types using `transform`:

<Snippet file="duckdb/DomainType" />
