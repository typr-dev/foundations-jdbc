---
title: MariaDB/MySQL Types
---

import Snippet from '@site/src/components/Snippet';

# MariaDB/MySQL type support

Coverage includes unsigned integers, spatial types, the `SET` and `ENUM` column types, MariaDB's `INET4`/`INET6`/`UUID`/`VECTOR` types, and JSON.

## Integer types (signed)

| MariaDB Type | Java Type | Range |
|--------------|-----------|-------|
| `TINYINT` | `Byte` | -128 to 127 |
| `SMALLINT` | `Short` | -32,768 to 32,767 |
| `MEDIUMINT` | `Integer` | -8,388,608 to 8,388,607 |
| `INT` | `Integer` | -2,147,483,648 to 2,147,483,647 |
| `BIGINT` | `Long` | -2^63 to 2^63-1 |

<Snippet file="mariadb/IntegerTypesSigned" />

## Integer types (unsigned)

MariaDB unsigned integers map to dedicated `Uint1`/`Uint2`/`Uint4`/`Uint8` wrapper types:

| MariaDB Type | Java Type | Range |
|--------------|-----------|-------|
| `TINYINT UNSIGNED` | `Uint1` | 0 to 255 |
| `SMALLINT UNSIGNED` | `Uint2` | 0 to 65,535 |
| `MEDIUMINT UNSIGNED` | `Uint4` | 0 to 16,777,215 |
| `INT UNSIGNED` | `Uint4` | 0 to 4,294,967,295 |
| `BIGINT UNSIGNED` | `Uint8` | 0 to 2^64-1 |

<Snippet file="mariadb/IntegerTypesUnsigned" />

## Fixed-point types

| MariaDB Type | Java Type | Notes |
|--------------|-----------|-------|
| `DECIMAL(p,s)` | `BigDecimal` | Exact numeric |
| `NUMERIC(p,s)` | `BigDecimal` | Alias for DECIMAL |

<Snippet file="mariadb/FixedPointTypes" />

## Floating-point types

| MariaDB Type | Java Type | Notes |
|--------------|-----------|-------|
| `FLOAT` | `Float` | 32-bit IEEE 754 |
| `DOUBLE` | `Double` | 64-bit IEEE 754 |

<Snippet file="mariadb/FloatingPointTypes" />

## Boolean type

| MariaDB Type | Java Type | Notes |
|--------------|-----------|-------|
| `BOOLEAN` / `BOOL` | `Boolean` | Alias for TINYINT(1) |
| `BIT(1)` | `Boolean` | Single bit as boolean |

<Snippet file="mariadb/BooleanType" />

## Bit types

| MariaDB Type | Java Type | Notes |
|--------------|-----------|-------|
| `BIT(n)` | `byte[]` | Bit field (n > 1) |

<Snippet file="mariadb/BitTypes" />

## String types

| MariaDB Type | Java Type | Max Length |
|--------------|-----------|------------|
| `CHAR(n)` | `String` | 255 chars |
| `VARCHAR(n)` | `String` | 65,535 bytes |
| `TINYTEXT` | `String` | 255 bytes |
| `TEXT` | `String` | 65,535 bytes |
| `MEDIUMTEXT` | `String` | 16 MB |
| `LONGTEXT` | `String` | 4 GB |

<Snippet file="mariadb/StringTypes" />

## Binary types

| MariaDB Type | Java Type | Max Length |
|--------------|-----------|------------|
| `BINARY(n)` | `byte[]` | Fixed n bytes |
| `VARBINARY(n)` | `byte[]` | Up to n bytes |
| `TINYBLOB` | `byte[]` | 255 bytes |
| `BLOB` | `byte[]` | 65,535 bytes |
| `MEDIUMBLOB` | `byte[]` | 16 MB |
| `LONGBLOB` | `byte[]` | 4 GB |

<Snippet file="mariadb/BinaryTypes" />

## Date/time types

| MariaDB Type | Java Type | Notes |
|--------------|-----------|-------|
| `DATE` | `LocalDate` | Naive date, no zone |
| `TIME` | `LocalTime` | Naive time, no zone |
| `DATETIME` | `LocalDateTime` | Naive timestamp, no zone |
| `TIMESTAMP` | `LocalDateTime` | Session-TZ converted — see note below |
| `YEAR` | `Year` | 4-digit year |

<Snippet file="mariadb/DateTimeTypes" />

:::note MariaDB has no zone-preserving timestamp type
None of MariaDB's temporal types store time-zone information in the column itself:
- `DATE`, `TIME`, `DATETIME` are naive — the value you write is the value you read back verbatim, whatever zone the writer was in.
- `TIMESTAMP` is stored as UTC seconds internally but is converted to and from the session's `time_zone` variable on every read/write. Two clients with different session timezones see different wall-clock values for the same row — round-trip stability depends on `time_zone` being set consistently.

All four map to `LocalDateTime` (or `LocalDate`/`LocalTime`) because `LocalDateTime` is the Java type that matches the naive-wall-clock semantic. `Instant`, `OffsetDateTime`, and `ZonedDateTime` would all misrepresent what the column holds.

For application-level "point in time" values, the idiomatic approach on MariaDB is either: (a) `DATETIME` with all writes normalized to UTC in application code, or (b) `BIGINT` epoch milliseconds. Neither matches a single built-in MariaDB type cleanly.
:::

## ENUM type

| MariaDB Type | Java Type |
|--------------|-----------|
| `ENUM('a','b','c')` | Java Enum |

<Snippet file="mariadb/EnumType" />

:::tip The `values()`-based factory derives everything
MariaDB/MySQL enums aren't named types — they're declared inline on the column. The `values()`-based factory derives the full `ENUM('A','B','C')` literal automatically:

```java
MariaTypes.ofEnum(State.values())          // Java
```
```kotlin
MariaTypes.ofEnum<State>()                 // Kotlin (reified)
```
```scala
MariaTypes.ofEnum(State.values)            // Scala 3
```

The column DDL must match the derived literal — `ENUM('PENDING','ACTIVE','COMPLETED')` in declaration order, using each constant's name.

Fall back to the string-based overload `ofEnum("ENUM('pending',…)", s -> State.valueOf(s.toUpperCase()))` when the database labels differ from the Java enum's `name()` values (e.g. lowercase labels in the DB).
:::

## SET type

| MariaDB Type | Java Type |
|--------------|-----------|
| `SET('a','b','c')` | `MariaSet` |

<Snippet file="mariadb/SetType" />

## JSON type

| MariaDB Type | Java Type |
|--------------|-----------|
| `JSON` | `Json` |

<Snippet file="mariadb/JsonType" />

## Network types (MariaDB 10.10+)

| MariaDB Type | Java Type | Description |
|--------------|-----------|-------------|
| `INET4` | `Inet4` | IPv4 address |
| `INET6` | `Inet6` | IPv6 address |

<Snippet file="mariadb/NetworkTypes" />

## UUID type (MariaDB 10.7+)

| MariaDB Type | Java Type | Description |
|--------------|-----------|-------------|
| `UUID` | `java.util.UUID` | 128-bit UUID |

<Snippet file="mariadb/UuidType" />

## VECTOR type (MariaDB 11.7+)

| MariaDB Type | Java Type | Description |
|--------------|-----------|-------------|
| `VECTOR(n)` | `Vector` | Fixed-dimension float vector for embeddings |

<Snippet file="mariadb/VectorType" />

## Spatial types

MariaDB spatial types use the MariaDB Connector/J geometry classes:

| MariaDB Type | Java Type | Description |
|--------------|-----------|-------------|
| `GEOMETRY` | `Geometry` | Any geometry |
| `POINT` | `Point` | Single point |
| `LINESTRING` | `LineString` | Line of points |
| `POLYGON` | `Polygon` | Closed polygon |
| `MULTIPOINT` | `MultiPoint` | Multiple points |
| `MULTILINESTRING` | `MultiLineString` | Multiple lines |
| `MULTIPOLYGON` | `MultiPolygon` | Multiple polygons |
| `GEOMETRYCOLLECTION` | `GeometryCollection` | Mixed geometries |

<Snippet file="mariadb/SpatialTypes" />

## Nullable types

Any type can be made nullable using `.opt()`:

<Snippet file="mariadb/NullableType" />

## Custom domain types

Wrap base types with custom Java types using `transform`:

<Snippet file="mariadb/DomainType" />
