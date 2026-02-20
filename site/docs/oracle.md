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

## OBJECT Types

Oracle OBJECT types (user-defined types) are supported via generated code:

```java
// Generated code creates OracleType for your OBJECT type
// Example for ADDRESS_T type:
OracleType<AddressT> addressType = AddressT.oracleType;

// Insert using the generated type
AddressT addr = new AddressT("123 Main St", "City", "12345");
```

## Nested Tables and VARRAYs

Oracle collection types are fully supported:

```java
// Nested tables - generated as List<Element>
OracleType<List<String>> stringTable = // generated

// VARRAYs - generated as arrays
OracleType<String[]> stringVarray = // generated
```

## Nullable Types

Any type can be made nullable using `.opt()`:

<Snippet file="oracle/NullableType" />

## Custom Domain Types

Wrap base types with custom Java types using `transform`:

<Snippet file="oracle/DomainType" />
