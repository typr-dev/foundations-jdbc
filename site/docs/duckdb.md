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

## Array Types

Any type can be converted to an array type using `.array()`:

| DuckDB Type | Java Type |
|-------------|-----------|
| `INTEGER[]` | `Integer[]` |
| `VARCHAR[]` | `String[]` |
| `BOOLEAN[]` | `Boolean[]` |
| ... | ... |

<Snippet file="duckdb/ArrayTypes" />

## LIST Types

DuckDB's LIST type is similar to arrays but with different semantics:

| DuckDB Type | Java Type |
|-------------|-----------|
| `LIST<INTEGER>` | `List<Integer>` |
| `LIST<VARCHAR>` | `List<String>` |
| `LIST<DATE>` | `List<LocalDate>` |
| ... | ... |

<Snippet file="duckdb/ListTypes" />

## MAP Types

DuckDB's MAP type for key-value pairs:

| DuckDB Type | Java Type |
|-------------|-----------|
| `MAP(VARCHAR, INTEGER)` | `Map<String, Integer>` |
| `MAP(VARCHAR, VARCHAR)` | `Map<String, String>` |

<Snippet file="duckdb/MapTypes" />

## STRUCT Types

DuckDB's STRUCT type for composite values:

```java
// Structs are typically handled via generated code
// The structure is defined by the table schema
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
