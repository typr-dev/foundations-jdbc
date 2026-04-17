---
title: DB2 Types
---

import Snippet from '@site/src/components/Snippet';

# DB2 Type Support

Foundations JDBC provides support for IBM DB2's type system, including DECFLOAT, double-byte character types (GRAPHIC/VARGRAPHIC/DBCLOB), XML, and ROWID.

## Integer Types

| DB2 Type | Java Type | Notes |
|----------|-----------|-------|
| `SMALLINT` | `Short` | 16-bit integer |
| `INTEGER` / `INT` | `Integer` | 32-bit integer |
| `BIGINT` | `Long` | 64-bit integer |

<Snippet file="db2/IntegerTypes" />

## Fixed-Point Types

| DB2 Type | Java Type | Notes |
|----------|-----------|-------|
| `DECIMAL` / `NUMERIC` | `BigDecimal` | Exact numeric |
| `DECIMAL(p,s)` | `BigDecimal` | With precision and scale |
| `DECFLOAT` | `BigDecimal` | DB2-specific decimal floating point (16 or 34 digits) |

<Snippet file="db2/FixedPointTypes" />

## Floating-Point Types

| DB2 Type | Java Type | Notes |
|----------|-----------|-------|
| `REAL` | `Float` | Single precision |
| `DOUBLE` / `FLOAT` | `Double` | Double precision |

<Snippet file="db2/FloatingPointTypes" />

## Boolean

| DB2 Type | Java Type | Notes |
|----------|-----------|-------|
| `BOOLEAN` | `Boolean` | Native since DB2 11.1 |

<Snippet file="db2/BoolType" />

## String Types (Single-Byte)

| DB2 Type | Java Type | Notes |
|----------|-----------|-------|
| `CHAR` / `CHARACTER` | `String` | Fixed-length |
| `CHAR(n)` | `String` | Fixed-length with size |
| `VARCHAR` | `String` | Variable-length |
| `VARCHAR(n)` | `String` | Variable-length with size |
| `CLOB` | `String` | Character Large Object |

<Snippet file="db2/StringTypesSingleByte" />

## String Types (Double-Byte)

DB2 has dedicated types for double-byte character set (DBCS) strings.

| DB2 Type | Java Type | Notes |
|----------|-----------|-------|
| `GRAPHIC` | `String` | Fixed-length DBCS |
| `GRAPHIC(n)` | `String` | Fixed-length DBCS with size |
| `VARGRAPHIC` | `String` | Variable-length DBCS |
| `VARGRAPHIC(n)` | `String` | Variable-length DBCS with size |
| `DBCLOB` | `String` | Double-byte CLOB |

<Snippet file="db2/StringTypesDoubleByte" />

:::note
DB2's `JSON_OBJECT` does not support GRAPHIC, VARGRAPHIC, or DBCLOB types. JSON serialization is not available for these types.
:::

## Binary Types

| DB2 Type | Java Type | Notes |
|----------|-----------|-------|
| `BINARY` | `byte[]` | Fixed-length binary |
| `BINARY(n)` | `byte[]` | Fixed-length binary with size |
| `VARBINARY` | `byte[]` | Variable-length binary |
| `VARBINARY(n)` | `byte[]` | Variable-length binary with size |
| `BLOB` | `byte[]` | Binary Large Object |

<Snippet file="db2/BinaryTypes" />

:::note
DB2's `JSON_OBJECT` does not support BINARY, VARBINARY, or BLOB types. JSON serialization is not available for these types.
:::

## Date/Time Types

| DB2 Type | Java Type | Notes |
|----------|-----------|-------|
| `DATE` | `LocalDate` | Naive date, no zone |
| `TIME` | `LocalTime` | Naive time, no zone |
| `TIMESTAMP` | `LocalDateTime` | Naive timestamp, no zone |
| `TIMESTAMP(p)` | `LocalDateTime` | Naive timestamp with fractional-second precision |

<Snippet file="db2/DateTimeTypes" />

:::note DB2 has no zone-preserving timestamp type
All of DB2's temporal types are naive wall-clock values — the stored bytes represent a date/time as written, with no associated zone or offset. `LocalDate`, `LocalTime`, and `LocalDateTime` are the Java types that match this "no-zone" semantic exactly.

DB2 does have a `TIMESTAMP WITH TIME ZONE` syntax available in some environments, but its support is limited (it is not supported on LUW common configurations without explicit setup) and the library does not expose a dedicated mapping for it. For application-level "point in time" values, normalize to UTC before writing to a `TIMESTAMP` column, or store `BIGINT` epoch milliseconds.

Using `Instant`, `OffsetDateTime`, or `ZonedDateTime` would misrepresent the stored value — none of those Java types' semantics match a naive wall-clock.
:::

## Special Types

| DB2 Type | Java Type | Notes |
|----------|-----------|-------|
| `XML` | `Xml` | Native XML support |
| `ROWID` | `byte[]` | DB2 row identifier |

<Snippet file="db2/SpecialTypes" />

:::note
DB2's `JSON_OBJECT` does not support the XML type. JSON serialization is not available for XML columns.
:::

## Nullable Types

Any type can be made nullable using `.opt()`:

<Snippet file="db2/NullableType" />

## Custom Domain Types

Wrap base types with custom Java types using `transform`:

<Snippet file="db2/DomainType" />
