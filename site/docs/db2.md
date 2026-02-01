---
title: DB2 Types
---

# DB2 Type Support

Foundations JDBC provides support for IBM DB2's type system, including DECFLOAT, double-byte character types (GRAPHIC/VARGRAPHIC/DBCLOB), XML, and ROWID.

## Integer Types

| DB2 Type | Java Type | Notes |
|----------|-----------|-------|
| `SMALLINT` | `Short` | 16-bit integer |
| `INTEGER` / `INT` | `Integer` | 32-bit integer |
| `BIGINT` | `Long` | 64-bit integer |

```java
Db2Type<Short> smallType = Db2Types.smallint;
Db2Type<Integer> intType = Db2Types.integer;
Db2Type<Long> bigType = Db2Types.bigint;
```

## Fixed-Point Types

| DB2 Type | Java Type | Notes |
|----------|-----------|-------|
| `DECIMAL` / `NUMERIC` | `BigDecimal` | Exact numeric |
| `DECIMAL(p,s)` | `BigDecimal` | With precision and scale |
| `DECFLOAT` | `BigDecimal` | DB2-specific decimal floating point (16 or 34 digits) |

```java
Db2Type<BigDecimal> decType = Db2Types.decimal;
Db2Type<BigDecimal> preciseType = Db2Types.decimal(10, 2);
Db2Type<BigDecimal> decfloatType = Db2Types.decfloat;
Db2Type<BigDecimal> decfloat16 = Db2Types.decfloat(16);
```

## Floating-Point Types

| DB2 Type | Java Type | Notes |
|----------|-----------|-------|
| `REAL` | `Float` | Single precision |
| `DOUBLE` / `FLOAT` | `Double` | Double precision |

```java
Db2Type<Float> realType = Db2Types.real;
Db2Type<Double> doubleType = Db2Types.double_;
```

## Boolean

| DB2 Type | Java Type | Notes |
|----------|-----------|-------|
| `BOOLEAN` | `Boolean` | Native since DB2 11.1 |

```java
Db2Type<Boolean> boolType = Db2Types.boolean_;
```

## String Types (Single-Byte)

| DB2 Type | Java Type | Notes |
|----------|-----------|-------|
| `CHAR` / `CHARACTER` | `String` | Fixed-length |
| `CHAR(n)` | `String` | Fixed-length with size |
| `VARCHAR` | `String` | Variable-length |
| `VARCHAR(n)` | `String` | Variable-length with size |
| `CLOB` | `String` | Character Large Object |

```java
Db2Type<String> charType = Db2Types.char_;
Db2Type<String> char10 = Db2Types.char_(10);
Db2Type<String> varcharType = Db2Types.varchar;
Db2Type<String> varchar255 = Db2Types.varchar(255);
Db2Type<String> clobType = Db2Types.clob;
```

## String Types (Double-Byte)

DB2 has dedicated types for double-byte character set (DBCS) strings.

| DB2 Type | Java Type | Notes |
|----------|-----------|-------|
| `GRAPHIC` | `String` | Fixed-length DBCS |
| `GRAPHIC(n)` | `String` | Fixed-length DBCS with size |
| `VARGRAPHIC` | `String` | Variable-length DBCS |
| `VARGRAPHIC(n)` | `String` | Variable-length DBCS with size |
| `DBCLOB` | `String` | Double-byte CLOB |

```java
Db2Type<String> graphicType = Db2Types.graphic;
Db2Type<String> graphic10 = Db2Types.graphic(10);
Db2Type<String> vargraphicType = Db2Types.vargraphic;
Db2Type<String> dbclobType = Db2Types.dbclob;
```

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

```java
Db2Type<byte[]> binaryType = Db2Types.binary;
Db2Type<byte[]> binary16 = Db2Types.binary(16);
Db2Type<byte[]> varbinaryType = Db2Types.varbinary;
Db2Type<byte[]> blobType = Db2Types.blob;
```

:::note
DB2's `JSON_OBJECT` does not support BINARY, VARBINARY, or BLOB types. JSON serialization is not available for these types.
:::

## Date/Time Types

| DB2 Type | Java Type | Notes |
|----------|-----------|-------|
| `DATE` | `LocalDate` | Date without time |
| `TIME` | `LocalTime` | Time without date |
| `TIMESTAMP` | `LocalDateTime` | Timestamp without timezone |
| `TIMESTAMP(p)` | `LocalDateTime` | With fractional seconds precision |

```java
Db2Type<LocalDate> dateType = Db2Types.date;
Db2Type<LocalTime> timeType = Db2Types.time;
Db2Type<LocalDateTime> tsType = Db2Types.timestamp;
Db2Type<LocalDateTime> ts6 = Db2Types.timestamp(6);
```

## Special Types

| DB2 Type | Java Type | Notes |
|----------|-----------|-------|
| `XML` | `Xml` | Native XML support |
| `ROWID` | `byte[]` | DB2 row identifier |

```java
Db2Type<Xml> xmlType = Db2Types.xml;
Db2Type<byte[]> rowidType = Db2Types.rowid;
```

:::note
DB2's `JSON_OBJECT` does not support the XML type. JSON serialization is not available for XML columns.
:::
