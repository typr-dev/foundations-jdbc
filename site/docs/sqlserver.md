---
title: SQL Server Types
---

import Snippet from '@site/src/components/Snippet';

# SQL Server type support

Foundations JDBC supports SQL Server data types, including geography, geometry, hierarchyid, and Unicode types.

## Key differences

- **TINYINT is UNSIGNED** in SQL Server (0-255), unlike most other databases
- **Separate Unicode types** (NCHAR, NVARCHAR, NTEXT) vs non-Unicode
- **DATETIMEOFFSET** for timezone-aware timestamps
- **UNIQUEIDENTIFIER** for UUIDs/GUIDs
- **No native array support** - use table-valued parameters instead

## Integer types

| SQL Server Type | Java Type | Range | Notes |
|-----------------|-----------|-------|-------|
| `TINYINT` | `Uint1` | 0-255 | **Unsigned!** |
| `SMALLINT` | `Short` | -32,768 to 32,767 | |
| `INT` | `Integer` | -2^31 to 2^31-1 | |
| `BIGINT` | `Long` | -2^63 to 2^63-1 | |

<Snippet file="sqlserver/IntegerTypes" />

## Fixed-point types

| SQL Server Type | Java Type | Notes |
|-----------------|-----------|-------|
| `DECIMAL(p,s)` | `BigDecimal` | Exact numeric |
| `NUMERIC(p,s)` | `BigDecimal` | Alias for DECIMAL |
| `MONEY` | `BigDecimal` | Currency (4 decimal places) |
| `SMALLMONEY` | `BigDecimal` | Smaller currency range |

<Snippet file="sqlserver/FixedPointTypes" />

## Floating-point types

| SQL Server Type | Java Type | Notes |
|-----------------|-----------|-------|
| `REAL` | `Float` | 32-bit IEEE 754 |
| `FLOAT` | `Double` | 64-bit IEEE 754 |

<Snippet file="sqlserver/FloatingPointTypes" />

## Boolean type

| SQL Server Type | Java Type |
|-----------------|-----------|
| `BIT` | `Boolean` |

<Snippet file="sqlserver/BoolType" />

## String types (non-Unicode)

| SQL Server Type | Java Type | Max Length | Notes |
|-----------------|-----------|------------|-------|
| `CHAR(n)` | `String` | 8,000 chars | Fixed-length |
| `VARCHAR(n)` | `String` | 8,000 chars | Variable-length |
| `VARCHAR(MAX)` | `String` | 2 GB | Large variable-length |
| `TEXT` | `String` | 2 GB | Deprecated, use VARCHAR(MAX) |

<Snippet file="sqlserver/StringTypes" />

## String types (Unicode)

| SQL Server Type | Java Type | Max Length | Notes |
|-----------------|-----------|------------|-------|
| `NCHAR(n)` | `String` | 4,000 chars | Fixed-length Unicode |
| `NVARCHAR(n)` | `String` | 4,000 chars | Variable-length Unicode |
| `NVARCHAR(MAX)` | `String` | 2 GB | Large Unicode |
| `NTEXT` | `String` | 2 GB | Deprecated |

<Snippet file="sqlserver/UnicodeStringTypes" />

## Binary types

| SQL Server Type | Java Type | Max Length |
|-----------------|-----------|------------|
| `BINARY(n)` | `byte[]` | 8,000 bytes |
| `VARBINARY(n)` | `byte[]` | 8,000 bytes |
| `VARBINARY(MAX)` | `byte[]` | 2 GB |
| `IMAGE` | `byte[]` | 2 GB (deprecated) |

<Snippet file="sqlserver/BinaryTypes" />

## Date/time types

| SQL Server Type | Java Type | Precision | Notes |
|-----------------|-----------|-----------|-------|
| `DATE` | `LocalDate` | Day | Naive date, no zone |
| `TIME` | `LocalTime` | 100ns | Naive time, no zone |
| `DATETIME` | `LocalDateTime` | 3.33ms | Legacy naive timestamp |
| `SMALLDATETIME` | `LocalDateTime` | Minute | Legacy naive timestamp |
| `DATETIME2` | `LocalDateTime` | 100ns | Modern naive timestamp |
| `DATETIMEOFFSET` | `OffsetDateTime` | 100ns | **Preserves offset** — see note below |

<Snippet file="sqlserver/DateTimeTypes" />

:::note `DATETIMEOFFSET` → `OffsetDateTime` (genuinely stores the offset)
Unlike PostgreSQL's `timestamptz` or DuckDB's `TIMESTAMPTZ`, SQL Server's `DATETIMEOFFSET` really does store the offset value byte-for-byte (`-14:00` to `+14:00`). From Microsoft's docs: "Time zone offset aware and preservation: Yes… The time zone offset is preserved in the database for retrieval."

`OffsetDateTime` is the matching Java type: a timestamp plus a fixed numeric offset, no DST awareness (SQL Server's own docs: "Daylight saving aware: No"). SQL Server can only store an offset, not a named zone like `America/Los_Angeles`, so `OffsetDateTime` captures exactly what the column holds. Using `ZonedDateTime` would suggest the library preserves zone regions, which the storage cannot.

If you need UTC-only "instant" semantics instead, use `DATETIME2` + a separate offset column, or normalize client-side before insert.
:::

## UNIQUEIDENTIFIER (UUID/GUID)

| SQL Server Type | Java Type |
|-----------------|-----------|
| `UNIQUEIDENTIFIER` | `java.util.UUID` |

<Snippet file="sqlserver/UuidType" />

## XML type

| SQL Server Type | Java Type |
|-----------------|-----------|
| `XML` | `Xml` |

<Snippet file="sqlserver/XmlType" />

## JSON type

SQL Server 2016+ stores JSON as NVARCHAR(MAX):

| SQL Server Type | Java Type | Notes |
|-----------------|-----------|-------|
| `NVARCHAR(MAX)` | `Json` | JSON stored as Unicode string |

<Snippet file="sqlserver/JsonType" />

## Spatial types

SQL Server spatial types use the JDBC driver's native classes:

| SQL Server Type | Java Type | Notes |
|-----------------|-----------|-------|
| `GEOGRAPHY` | `Geography` | Geodetic (round earth) |
| `GEOMETRY` | `Geometry` | Planar (flat earth) |

<Snippet file="sqlserver/SpatialTypes" />

## HIERARCHYID

For hierarchical tree structures:

| SQL Server Type | Java Type | Notes |
|-----------------|-----------|-------|
| `HIERARCHYID` | `HierarchyId` | Path notation like `/1/2/3/` |

<Snippet file="sqlserver/HierarchyIdType" />

## ROWVERSION / TIMESTAMP

| SQL Server Type | Java Type | Notes |
|-----------------|-----------|-------|
| `ROWVERSION` | `byte[]` | 8-byte version number |
| `TIMESTAMP` | `byte[]` | Alias for ROWVERSION |

<Snippet file="sqlserver/RowversionType" />

## SQL_VARIANT

| SQL Server Type | Java Type | Notes |
|-----------------|-----------|-------|
| `SQL_VARIANT` | `Object` | Can store various types |

<Snippet file="sqlserver/SqlVariantType" />

## VECTOR (SQL Server 2025)

| SQL Server Type | Java Type | Notes |
|-----------------|-----------|-------|
| `VECTOR` | `byte[]` | For embeddings/ML |

<Snippet file="sqlserver/VectorType" />

## Nullable types

Any type can be made nullable using `.opt()`:

<Snippet file="sqlserver/NullableTypes" />

## Custom domain types

Wrap base types with custom Java types using `transform`:

<Snippet file="sqlserver/DomainType" />
