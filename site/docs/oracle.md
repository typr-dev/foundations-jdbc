---
title: Oracle Types
---

import Snippet from '@site/src/components/Snippet';

# Oracle type support

Foundations JDBC supports Oracle data types, including OBJECT types, nested tables, intervals, and LOB types.

## Numeric types

### Universal NUMBER type

| Oracle Type | Java Type | Notes |
|-------------|-----------|-------|
| `NUMBER` | `BigDecimal` | Arbitrary precision |
| `NUMBER(p,0)` where p &lt;= 9 | `Integer` | 32-bit integer |
| `NUMBER(p,0)` where 9 &lt; p &lt;= 18 | `Long` | 64-bit integer |
| `NUMBER(p,s)` | `BigDecimal` | Fixed precision/scale |

<Snippet file="oracle/NumericTypes" />

### IEEE 754 floating point

| Oracle Type | Java Type | Notes |
|-------------|-----------|-------|
| `BINARY_FLOAT` | `Float` | 32-bit IEEE 754 |
| `BINARY_DOUBLE` | `Double` | 64-bit IEEE 754 |
| `FLOAT(p)` | `Double` | Maps to NUMBER internally |

<Snippet file="oracle/FloatTypes" />

## Boolean type

| Oracle Type | Java Type | Notes |
|-------------|-----------|-------|
| `BOOLEAN` | `Boolean` | Oracle 23c+ native |
| `NUMBER(1)` | `Boolean` | Traditional 0/1 convention |

<Snippet file="oracle/BoolType" />

## Character types

| Oracle Type | Java Type | Max Length | Notes |
|-------------|-----------|------------|-------|
| `VARCHAR2(n)` | `String` | 4000 bytes | Variable-length |
| `CHAR(n)` | `String` | 2000 bytes | Fixed-length, blank-padded |
| `NVARCHAR2(n)` | `String` | 4000 bytes | National character set |
| `NCHAR(n)` | `String` | 2000 bytes | National fixed-length |
| `LONG` | `String` | 2 GB | Deprecated, use CLOB |

<Snippet file="oracle/StringTypes" />

### Non-empty string variants

For NOT NULL columns, use `NonEmptyString` to guarantee non-empty values:

<Snippet file="oracle/NonEmptyStringTypes" />

### Padded string for CHAR

For CHAR columns preserving padding:

<Snippet file="oracle/PaddedStringTypes" />

## Large object (LOB) types

| Oracle Type | Java Type | Max Size | Notes |
|-------------|-----------|----------|-------|
| `CLOB` | `String` | 4 GB | Character LOB |
| `NCLOB` | `String` | 4 GB | National character LOB |
| `BLOB` | `byte[]` | 4 GB | Binary LOB |

<Snippet file="oracle/LobTypes" />

## Binary types

| Oracle Type | Java Type | Max Length | Notes |
|-------------|-----------|------------|-------|
| `RAW(n)` | `byte[]` | 2000 bytes | Variable-length binary |
| `LONG RAW` | `byte[]` | 2 GB | Deprecated, use BLOB |

<Snippet file="oracle/BinaryTypes" />

## Date/time types

| Oracle Type | Java Type | Notes |
|-------------|-----------|-------|
| `DATE` | `LocalDateTime` | Date + time, second precision — Oracle's DATE always has a time component |
| `TIMESTAMP` | `LocalDateTime` | Naive timestamp, no zone (default precision 6) |
| `TIMESTAMP WITH TIME ZONE` | `ZonedDateTime` | **Preserves offset or zone region** — see note below |
| `TIMESTAMP WITH LOCAL TIME ZONE` | `Instant` | UTC instant, presented in session zone — see note below |

<Snippet file="oracle/DateTimeTypes" />

**Note:** Oracle `DATE` includes time (unlike SQL standard), so it maps to `LocalDateTime`, not `LocalDate`.

:::note `TIMESTAMP WITH TIME ZONE` → `ZonedDateTime` (not `OffsetDateTime`)
Oracle's TSTZ column uses a 13-byte on-disk format that can hold either a fixed offset (`-08:00`) or a named zone region (`America/Los_Angeles`). Region names are DST-aware. The same `America/Los_Angeles` column value renders as `-08:00` in January and `-07:00` in July.

`OffsetDateTime` cannot represent named zones; it holds only a numeric offset. Mapping Oracle TSTZ to `OffsetDateTime` would silently collapse every region to its current offset. A round-trip of `2024-01-15T10:00 America/Los_Angeles` would come back as `2024-01-15T10:00-08:00` with the region erased, and the DST rule lost for any future reads.

`ZonedDateTime` covers both cases without information loss:
- Fixed-offset input (`ZonedDateTime.of(..., ZoneOffset.UTC)`) round-trips as a `ZonedDateTime` whose zone is a `ZoneOffset`.
- Named-region input (`ZonedDateTime.of(..., ZoneId.of("America/Los_Angeles"))`) round-trips as a `ZonedDateTime` whose zone is a `ZoneRegion`.

If you don't need the region (you're modelling "a moment in time with whatever zone the user was in"), prefer `TIMESTAMP WITH LOCAL TIME ZONE` (see below) and get an `Instant`, which is simpler.
:::

:::note `TIMESTAMP WITH LOCAL TIME ZONE` → `Instant` (not `LocalDateTime`)
Despite the "LOCAL TIME ZONE" name, this column stores a **universal instant**, not a naive wall-clock. Oracle's documentation: "data stored in the database is normalized to the database time zone, and the time zone offset is not stored as part of the column data. When users retrieve the data, Oracle Database returns it in the users' local session time zone."

In other words: the column stores an instant, and Oracle applies session-TZ rendering at read-time as a display convenience. A value inserted from an Asia/Tokyo session reads back as the same instant from an America/Los_Angeles session, in LA's local time. Instant identity is preserved.

`Instant` is the Java type with this exact semantic: a point in time, zone-free. `LocalDateTime` would be wrong. It claims "naive wall-clock with no zone", but the data genuinely *is* a universal moment. Using `LocalDateTime` would mean two clients with different session-TZ settings would mint different `LocalDateTime` values for the same row, destroying round-trip stability.

Same mapping as PostgreSQL's `timestamptz` and DuckDB's `TIMESTAMPTZ`. All three store a universal instant and use `Instant` in Java.
:::

## Interval types

| Oracle Type | Java Type | Notes |
|-------------|-----------|-------|
| `INTERVAL YEAR TO MONTH` | `OracleIntervalYM` | Years and months |
| `INTERVAL DAY TO SECOND` | `OracleIntervalDS` | Days, hours, minutes, seconds |

<Snippet file="oracle/IntervalTypes" />

## ROWID types

| Oracle Type | Java Type | Notes |
|-------------|-----------|-------|
| `ROWID` | `String` | Physical row address (18 chars) |
| `UROWID` | `String` | Universal ROWID (max 4000 bytes) |

<Snippet file="oracle/RowIdTypes" />

## XML and JSON types

| Oracle Type | Java Type | Notes |
|-------------|-----------|-------|
| `XMLTYPE` | `String` | XML document storage |
| `JSON` | `Json` | Native JSON (Oracle 21c+) |

<Snippet file="oracle/XmlJsonTypes" />

## Object types

Oracle OBJECT types are built from a `RowCodecNamed` via `compositeOf`:

```java
OracleType<Address> addressType = OracleTypes.compositeOf("ADDRESS_T",
    RowCodec.<Address>namedBuilder()
        .field("STREET", OracleTypes.varchar2Of(200), Address::street)
        .field("CITY", OracleTypes.varchar2Of(100), Address::city)
        .field("ZIP", OracleTypes.varchar2Of(10), Address::zip)
        .build(Address::new));
```

The returned `OracleType` can be used with `OracleVArray` and `OracleNestedTable` for collection columns.

## VARRAYs

VARRAYs are fixed-maximum-size ordered collections (`CREATE TYPE ... AS VARRAY(n) OF ...`).
Mapped to `List<T>` in Java. The max size is enforced on write.

<Snippet file="oracle/VArrayTypes" />

## Nested tables

Nested tables are unbounded collections (`CREATE TYPE ... AS TABLE OF ...`).
Like VARRAYs, they map to `List<T>` but have no size limit.
Nested tables can hold OBJECT types for complex hierarchical data.

<Snippet file="oracle/NestedTableTypes" />

## Nullable types

Any type can be made nullable using `.opt()`:

<Snippet file="oracle/NullableType" />

### Oracle nullability behavior

Oracle treats empty strings as NULL: `INSERT INTO t (col) VALUES ('')` stores NULL.
`VARCHAR2` columns are effectively always nullable from Oracle's perspective,
even if the column has a `NOT NULL` constraint (an empty string insert will fail with a
constraint violation, not store an empty string).

When using query analysis, Oracle may report all `VARCHAR2`/`CHAR` columns as nullable.
Use `.nullableOk()` on the type if you want to suppress nullability warnings for columns
you know are `NOT NULL` in the schema:

```java
OracleType<String> name = OracleTypes.varchar2Of(100).nullableOk();
```

## Custom domain types

Wrap base types with custom Java types using `transform`:

<Snippet file="oracle/DomainType" />

## Required driver dependencies

The core driver is `com.oracle.database.jdbc:ojdbc11`. A second artifact is needed
when you bind OBJECT or VARRAY literals inline in SQL (e.g. `TABLE(?)` over a
VARRAY parameter, or `SELECT ?::my_t FROM dual`):

```
com.oracle.database.xml:xdb:23.6.0.24.10
```

Without `xdb` on the runtime classpath, the first inline OBJECT/VARRAY bind fails
with `NoClassDefFoundError: oracle/xdb/XMLType`. The driver itself compiles fine
without it; the error only surfaces at execute time.

## Reserved-word traps in OBJECT types

Oracle's reserved-word list applies inside `CREATE TYPE`. Attribute names like
`LEVEL`, `ROWID`, `DATE`, `NUMBER`, `USER` will *compile* the type but leave it in
state `INVALID`:

```sql
CREATE TYPE skill_t AS OBJECT (name VARCHAR2(100), LEVEL NUMBER(3));
-- reports "Type created" but SELECT status FROM user_objects WHERE object_name='SKILL_T' → INVALID
-- every downstream use then fails with ORA-00902 / ORA-03050
```

Rename the attribute (`LVL`, `ROW_ID`, etc.) or quote the identifier. The error
messages don't point back to the CREATE TYPE; they show up at SELECT/INSERT time.

## DDL inside a transaction

Oracle auto-commits DDL. Running several `CREATE TYPE` / `CREATE TABLE` statements
inside a single `tx.transact { conn -> ... }` block can produce surprising catalog
visibility issues. A subsequent `CREATE TABLE` referencing a freshly-declared
`TYPE` may fail with `ORA-00902` even though the type is valid. Split each DDL
into its own `tx.transact { }` call to keep each statement's auto-commit boundary
clean.

## Schema-qualified type names

When a connection's current schema owns the type, you can reference it bare
(`compositeOf("SKILL_T", ...)`). Cross-schema usage generally requires the
fully-qualified name (`compositeOf("TYPR.SKILL_T", ...)`). If in doubt, use the
qualified form. It works from either side.
