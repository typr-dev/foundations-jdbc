---
title: PostgreSQL Types
---

import Snippet from '@site/src/components/Snippet';

# PostgreSQL Type Support

Foundations JDBC provides comprehensive support for all PostgreSQL data types, including the many exotic types that make PostgreSQL unique.

## Numeric Types

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

## Boolean Type

| PostgreSQL Type | Java Type |
|-----------------|-----------|
| `bool` / `boolean` | `Boolean` |

<Snippet file="postgresql/BoolType" />

## String Types

| PostgreSQL Type | Java Type | Notes |
|-----------------|-----------|-------|
| `text` | `String` | Variable unlimited length |
| `varchar(n)` | `String` | Variable length with limit |
| `bpchar` / `char(n)` | `String` | Fixed-length, blank-padded |
| `name` | `String` | 63-character identifier |

<Snippet file="postgresql/StringTypes" />

## Binary Types

| PostgreSQL Type | Java Type | Notes |
|-----------------|-----------|-------|
| `bytea` | `byte[]` | Variable-length binary |

<Snippet file="postgresql/BinaryTypes" />

## Date/Time Types

| PostgreSQL Type | Java Type | Notes |
|-----------------|-----------|-------|
| `date` | `LocalDate` | Date without time |
| `time` | `LocalTime` | Time without timezone |
| `timetz` | `OffsetTime` | Time with timezone |
| `timestamp` | `LocalDateTime` | Date and time without timezone |
| `timestamptz` | `Instant` | Date and time with timezone (stored as UTC) |
| `interval` | `PGInterval` | Time duration |

<Snippet file="postgresql/DateTimeTypes" />

## UUID Type

| PostgreSQL Type | Java Type |
|-----------------|-----------|
| `uuid` | `java.util.UUID` |

<Snippet file="postgresql/UuidType" />

## JSON Types

| PostgreSQL Type | Java Type | Notes |
|-----------------|-----------|-------|
| `json` | `Json` | Stored as-is, validated on input |
| `jsonb` | `Jsonb` | Binary format, indexed, normalized |

<Snippet file="postgresql/JsonTypes" />

## Array Types

Any PostgreSQL type can be used as an array. Foundations JDBC supports both boxed and unboxed array variants:

| PostgreSQL Type | Java Type (Boxed) | Java Type (Unboxed) |
|-----------------|-------------------|---------------------|
| `int4[]` | `Integer[]` | `int[]` |
| `int8[]` | `Long[]` | `long[]` |
| `float4[]` | `Float[]` | `float[]` |
| `float8[]` | `Double[]` | `double[]` |
| `bool[]` | `Boolean[]` | `boolean[]` |
| `text[]` | `String[]` | - |
| `uuid[]` | `UUID[]` | - |

<Snippet file="postgresql/ArrayTypes" />

## Range Types

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

## Geometric Types

PostgreSQL's geometric types for 2D shapes:

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

## Custom Domain Types

Wrap base types with custom Java types using `transform`:

<Snippet file="postgresql/DomainType" />

## Nullable Types

Any type can be made nullable using `.opt()`:

<Snippet file="postgresql/NullableTypes" />
