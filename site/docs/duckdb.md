---
title: DuckDB Types
---

import Snippet from '@site/src/components/Snippet';

# DuckDB Type Support

Foundations JDBC provides comprehensive support for DuckDB's rich type system, including nested types (LIST, STRUCT, MAP, UNION) and extended integer types.

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
| `DATE` | `LocalDate` | Date only |
| `TIME` | `LocalTime` | Time only |
| `TIMESTAMP` / `DATETIME` | `LocalDateTime` | Date and time |
| `TIMESTAMP WITH TIME ZONE` | `OffsetDateTime` | With timezone |
| `TIME WITH TIME ZONE` | `OffsetDateTime` | Time with timezone |
| `INTERVAL` | `Duration` | Time duration |

<Snippet file="duckdb/DateTimeTypes" />

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
