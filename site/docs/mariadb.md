---
title: MariaDB/MySQL Types
---

import Snippet from '@site/src/components/Snippet';

# MariaDB/MySQL Type Support

Foundations JDBC provides comprehensive support for MariaDB and MySQL data types, including unsigned integers, spatial types, and MySQL-specific features.

## Integer Types (Signed)

| MariaDB Type | Java Type | Range |
|--------------|-----------|-------|
| `TINYINT` | `Byte` | -128 to 127 |
| `SMALLINT` | `Short` | -32,768 to 32,767 |
| `MEDIUMINT` | `Integer` | -8,388,608 to 8,388,607 |
| `INT` | `Integer` | -2,147,483,648 to 2,147,483,647 |
| `BIGINT` | `Long` | -2^63 to 2^63-1 |

<Snippet file="mariadb/IntegerTypesSigned" />

## Integer Types (Unsigned)

MariaDB supports unsigned integers, which are wrapped in type-safe unsigned types:

| MariaDB Type | Java Type | Range |
|--------------|-----------|-------|
| `TINYINT UNSIGNED` | `Uint1` | 0 to 255 |
| `SMALLINT UNSIGNED` | `Uint2` | 0 to 65,535 |
| `MEDIUMINT UNSIGNED` | `Uint4` | 0 to 16,777,215 |
| `INT UNSIGNED` | `Uint4` | 0 to 4,294,967,295 |
| `BIGINT UNSIGNED` | `Uint8` | 0 to 2^64-1 |

<Snippet file="mariadb/IntegerTypesUnsigned" />

## Fixed-Point Types

| MariaDB Type | Java Type | Notes |
|--------------|-----------|-------|
| `DECIMAL(p,s)` | `BigDecimal` | Exact numeric |
| `NUMERIC(p,s)` | `BigDecimal` | Alias for DECIMAL |

<Snippet file="mariadb/FixedPointTypes" />

## Floating-Point Types

| MariaDB Type | Java Type | Notes |
|--------------|-----------|-------|
| `FLOAT` | `Float` | 32-bit IEEE 754 |
| `DOUBLE` | `Double` | 64-bit IEEE 754 |

<Snippet file="mariadb/FloatingPointTypes" />

## Boolean Type

| MariaDB Type | Java Type | Notes |
|--------------|-----------|-------|
| `BOOLEAN` / `BOOL` | `Boolean` | Alias for TINYINT(1) |
| `BIT(1)` | `Boolean` | Single bit as boolean |

<Snippet file="mariadb/BooleanType" />

## Bit Types

| MariaDB Type | Java Type | Notes |
|--------------|-----------|-------|
| `BIT(n)` | `byte[]` | Bit field (n > 1) |

<Snippet file="mariadb/BitTypes" />

## String Types

| MariaDB Type | Java Type | Max Length |
|--------------|-----------|------------|
| `CHAR(n)` | `String` | 255 chars |
| `VARCHAR(n)` | `String` | 65,535 bytes |
| `TINYTEXT` | `String` | 255 bytes |
| `TEXT` | `String` | 65,535 bytes |
| `MEDIUMTEXT` | `String` | 16 MB |
| `LONGTEXT` | `String` | 4 GB |

<Snippet file="mariadb/StringTypes" />

## Binary Types

| MariaDB Type | Java Type | Max Length |
|--------------|-----------|------------|
| `BINARY(n)` | `byte[]` | Fixed n bytes |
| `VARBINARY(n)` | `byte[]` | Up to n bytes |
| `TINYBLOB` | `byte[]` | 255 bytes |
| `BLOB` | `byte[]` | 65,535 bytes |
| `MEDIUMBLOB` | `byte[]` | 16 MB |
| `LONGBLOB` | `byte[]` | 4 GB |

<Snippet file="mariadb/BinaryTypes" />

## Date/Time Types

| MariaDB Type | Java Type | Notes |
|--------------|-----------|-------|
| `DATE` | `LocalDate` | Date only |
| `TIME` | `LocalTime` | Time only |
| `DATETIME` | `LocalDateTime` | Date and time |
| `TIMESTAMP` | `LocalDateTime` | With auto-update |
| `YEAR` | `Year` | 4-digit year |

<Snippet file="mariadb/DateTimeTypes" />

## ENUM Type

| MariaDB Type | Java Type |
|--------------|-----------|
| `ENUM('a','b','c')` | Java Enum |

<Snippet file="mariadb/EnumType" />

## SET Type

| MariaDB Type | Java Type |
|--------------|-----------|
| `SET('a','b','c')` | `MariaSet` |

<Snippet file="mariadb/SetType" />

## JSON Type

| MariaDB Type | Java Type |
|--------------|-----------|
| `JSON` | `Json` |

<Snippet file="mariadb/JsonType" />

## Network Types (MariaDB 10.10+)

| MariaDB Type | Java Type | Description |
|--------------|-----------|-------------|
| `INET4` | `Inet4` | IPv4 address |
| `INET6` | `Inet6` | IPv6 address |

<Snippet file="mariadb/NetworkTypes" />

## Spatial Types

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

## Nullable Types

Any type can be made nullable using `.opt()`:

<Snippet file="mariadb/NullableType" />

## Custom Domain Types

Wrap base types with custom Java types using `bimap`:

<Snippet file="mariadb/DomainType" />
