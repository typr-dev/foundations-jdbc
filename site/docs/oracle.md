---
title: Oracle Types
---

import Snippet from '@site/src/components/Snippet';

# Oracle Type Support

Foundations JDBC provides comprehensive support for Oracle data types, including OBJECT types, nested tables, intervals, and LOB types.

## Numeric Types

### Universal NUMBER Type

| Oracle Type | Java Type | Notes |
|-------------|-----------|-------|
| `NUMBER` | `BigDecimal` | Arbitrary precision |
| `NUMBER(p,0)` where p &lt;= 9 | `Integer` | 32-bit integer |
| `NUMBER(p,0)` where 9 &lt; p &lt;= 18 | `Long` | 64-bit integer |
| `NUMBER(p,s)` | `BigDecimal` | Fixed precision/scale |

<Snippet file="oracle/NumericTypes" />

### IEEE 754 Floating Point

| Oracle Type | Java Type | Notes |
|-------------|-----------|-------|
| `BINARY_FLOAT` | `Float` | 32-bit IEEE 754 |
| `BINARY_DOUBLE` | `Double` | 64-bit IEEE 754 |
| `FLOAT(p)` | `Double` | Maps to NUMBER internally |

<Snippet file="oracle/FloatTypes" />

## Boolean Type

| Oracle Type | Java Type | Notes |
|-------------|-----------|-------|
| `BOOLEAN` | `Boolean` | Oracle 23c+ native |
| `NUMBER(1)` | `Boolean` | Traditional 0/1 convention |

<Snippet file="oracle/BoolType" />

## Character Types

| Oracle Type | Java Type | Max Length | Notes |
|-------------|-----------|------------|-------|
| `VARCHAR2(n)` | `String` | 4000 bytes | Variable-length |
| `CHAR(n)` | `String` | 2000 bytes | Fixed-length, blank-padded |
| `NVARCHAR2(n)` | `String` | 4000 bytes | National character set |
| `NCHAR(n)` | `String` | 2000 bytes | National fixed-length |
| `LONG` | `String` | 2 GB | Deprecated, use CLOB |

<Snippet file="oracle/StringTypes" />

### Non-Empty String Variants

For NOT NULL columns, use `NonEmptyString` to guarantee non-empty values:

<Snippet file="oracle/NonEmptyStringTypes" />

### Padded String for CHAR

For CHAR columns preserving padding:

<Snippet file="oracle/PaddedStringTypes" />

## Large Object (LOB) Types

| Oracle Type | Java Type | Max Size | Notes |
|-------------|-----------|----------|-------|
| `CLOB` | `String` | 4 GB | Character LOB |
| `NCLOB` | `String` | 4 GB | National character LOB |
| `BLOB` | `byte[]` | 4 GB | Binary LOB |

<Snippet file="oracle/LobTypes" />

## Binary Types

| Oracle Type | Java Type | Max Length | Notes |
|-------------|-----------|------------|-------|
| `RAW(n)` | `byte[]` | 2000 bytes | Variable-length binary |
| `LONG RAW` | `byte[]` | 2 GB | Deprecated, use BLOB |

<Snippet file="oracle/BinaryTypes" />

## Date/Time Types

| Oracle Type | Java Type | Notes |
|-------------|-----------|-------|
| `DATE` | `LocalDateTime` | Date + time (second precision) |
| `TIMESTAMP` | `LocalDateTime` | Fractional seconds (default: 6) |
| `TIMESTAMP WITH TIME ZONE` | `OffsetDateTime` | Explicit timezone |
| `TIMESTAMP WITH LOCAL TIME ZONE` | `Instant` | Session timezone |

<Snippet file="oracle/DateTimeTypes" />

**Note:** Oracle `DATE` includes time (unlike SQL standard), so it maps to `LocalDateTime`, not `LocalDate`.

## Interval Types

| Oracle Type | Java Type | Notes |
|-------------|-----------|-------|
| `INTERVAL YEAR TO MONTH` | `OracleIntervalYM` | Years and months |
| `INTERVAL DAY TO SECOND` | `OracleIntervalDS` | Days, hours, minutes, seconds |

<Snippet file="oracle/IntervalTypes" />

## ROWID Types

| Oracle Type | Java Type | Notes |
|-------------|-----------|-------|
| `ROWID` | `String` | Physical row address (18 chars) |
| `UROWID` | `String` | Universal ROWID (max 4000 bytes) |

<Snippet file="oracle/RowIdTypes" />

## XML and JSON Types

| Oracle Type | Java Type | Notes |
|-------------|-----------|-------|
| `XMLTYPE` | `String` | XML document storage |
| `JSON` | `Json` | Native JSON (Oracle 21c+) |

<Snippet file="oracle/XmlJsonTypes" />

## Object Types

Oracle OBJECT types are built from a `RowCodecNamed` via `compositeOf`:

```java
OracleType<Address> addressType = OracleTypes.compositeOf("ADDRESS_T",
    RowCodec.<Address>namedBuilder()
        .field("STREET", OracleTypes.varchar2(200), Address::street)
        .field("CITY", OracleTypes.varchar2(100), Address::city)
        .field("ZIP", OracleTypes.varchar2(10), Address::zip)
        .build(Address::new));
```

The returned `OracleType` can be used with `OracleVArray` and `OracleNestedTable` for collection columns.

## VARRAYs

VARRAYs are fixed-maximum-size ordered collections (`CREATE TYPE ... AS VARRAY(n) OF ...`).
Mapped to `List<T>` in Java. The max size is enforced on write.

<Snippet file="oracle/VArrayTypes" />

## Nested Tables

Nested tables are unbounded collections (`CREATE TYPE ... AS TABLE OF ...`).
Like VARRAYs, they map to `List<T>` but have no size limit.
Nested tables can hold OBJECT types for complex hierarchical data.

<Snippet file="oracle/NestedTableTypes" />

## Nullable Types

Any type can be made nullable using `.opt()`:

<Snippet file="oracle/NullableType" />

### Oracle Nullability Behavior

Oracle treats empty strings as NULL — `INSERT INTO t (col) VALUES ('')` stores NULL.
This means `VARCHAR2` columns are effectively always nullable from Oracle's perspective,
even if the column has a `NOT NULL` constraint (an empty string insert will fail with a
constraint violation, not store an empty string).

When using query analysis, Oracle may report all `VARCHAR2`/`CHAR` columns as nullable.
Use `.nullableOk()` on the type if you want to suppress nullability warnings for columns
you know are `NOT NULL` in the schema:

```java
OracleType<String> name = OracleTypes.varchar2(100).nullableOk();
```

## Custom Domain Types

Wrap base types with custom Java types using `transform`:

<Snippet file="oracle/DomainType" />
