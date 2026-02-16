---
title: SQL Server Types
---

import Snippet from '@site/src/components/Snippet';

# SQL Server Type Support

Foundations JDBC provides comprehensive support for SQL Server data types, including geography, geometry, hierarchyid, and Unicode types.

## Key Differences

- **TINYINT is UNSIGNED** in SQL Server (0-255), unlike most other databases
- **Separate Unicode types** (NCHAR, NVARCHAR, NTEXT) vs non-Unicode
- **DATETIMEOFFSET** for timezone-aware timestamps
- **UNIQUEIDENTIFIER** for UUIDs/GUIDs
- **No native array support** - use table-valued parameters instead

## Integer Types

| SQL Server Type | Java Type | Range | Notes |
|-----------------|-----------|-------|-------|
| `TINYINT` | `Uint1` | 0-255 | **Unsigned!** |
| `SMALLINT` | `Short` | -32,768 to 32,767 | |
| `INT` | `Integer` | -2^31 to 2^31-1 | |
| `BIGINT` | `Long` | -2^63 to 2^63-1 | |

<Snippet file="sqlserver/IntegerTypes" />

## Fixed-Point Types

| SQL Server Type | Java Type | Notes |
|-----------------|-----------|-------|
| `DECIMAL(p,s)` | `BigDecimal` | Exact numeric |
| `NUMERIC(p,s)` | `BigDecimal` | Alias for DECIMAL |
| `MONEY` | `BigDecimal` | Currency (4 decimal places) |
| `SMALLMONEY` | `BigDecimal` | Smaller currency range |

<Snippet file="sqlserver/FixedPointTypes" />

## Floating-Point Types

| SQL Server Type | Java Type | Notes |
|-----------------|-----------|-------|
| `REAL` | `Float` | 32-bit IEEE 754 |
| `FLOAT` | `Double` | 64-bit IEEE 754 |

<Snippet file="sqlserver/FloatingPointTypes" />

## Boolean Type

| SQL Server Type | Java Type |
|-----------------|-----------|
| `BIT` | `Boolean` |

<Snippet file="sqlserver/BoolType" />

## String Types (Non-Unicode)

| SQL Server Type | Java Type | Max Length | Notes |
|-----------------|-----------|------------|-------|
| `CHAR(n)` | `String` | 8,000 chars | Fixed-length |
| `VARCHAR(n)` | `String` | 8,000 chars | Variable-length |
| `VARCHAR(MAX)` | `String` | 2 GB | Large variable-length |
| `TEXT` | `String` | 2 GB | Deprecated, use VARCHAR(MAX) |

<Snippet file="sqlserver/StringTypes" />

## String Types (Unicode)

| SQL Server Type | Java Type | Max Length | Notes |
|-----------------|-----------|------------|-------|
| `NCHAR(n)` | `String` | 4,000 chars | Fixed-length Unicode |
| `NVARCHAR(n)` | `String` | 4,000 chars | Variable-length Unicode |
| `NVARCHAR(MAX)` | `String` | 2 GB | Large Unicode |
| `NTEXT` | `String` | 2 GB | Deprecated |

<Snippet file="sqlserver/UnicodeStringTypes" />

## Binary Types

| SQL Server Type | Java Type | Max Length |
|-----------------|-----------|------------|
| `BINARY(n)` | `byte[]` | 8,000 bytes |
| `VARBINARY(n)` | `byte[]` | 8,000 bytes |
| `VARBINARY(MAX)` | `byte[]` | 2 GB |
| `IMAGE` | `byte[]` | 2 GB (deprecated) |

<Snippet file="sqlserver/BinaryTypes" />

## Date/Time Types

| SQL Server Type | Java Type | Precision | Notes |
|-----------------|-----------|-----------|-------|
| `DATE` | `LocalDate` | Day | Date only |
| `TIME` | `LocalTime` | 100ns | Time only |
| `DATETIME` | `LocalDateTime` | 3.33ms | Legacy |
| `SMALLDATETIME` | `LocalDateTime` | Minute | Legacy |
| `DATETIME2` | `LocalDateTime` | 100ns | Modern |
| `DATETIMEOFFSET` | `OffsetDateTime` | 100ns | With timezone |

<Snippet file="sqlserver/DateTimeTypes" />

## UNIQUEIDENTIFIER (UUID/GUID)

| SQL Server Type | Java Type |
|-----------------|-----------|
| `UNIQUEIDENTIFIER` | `java.util.UUID` |

<Snippet file="sqlserver/UuidType" />

## XML Type

| SQL Server Type | Java Type |
|-----------------|-----------|
| `XML` | `Xml` |

<Snippet file="sqlserver/XmlType" />

## JSON Type

SQL Server 2016+ stores JSON as NVARCHAR(MAX):

| SQL Server Type | Java Type | Notes |
|-----------------|-----------|-------|
| `NVARCHAR(MAX)` | `Json` | JSON stored as Unicode string |

<Snippet file="sqlserver/JsonType" />

## Spatial Types

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

## Nullable Types

Any type can be made nullable using `.opt()`:

<Snippet file="sqlserver/NullableTypes" />

## Custom Domain Types

Wrap base types with custom Java types using `transform`:

<Snippet file="sqlserver/DomainType" />
