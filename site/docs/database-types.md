---
title: Supported Databases
---

import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

# Supported Databases

Each database has its own type hierarchy that knows exactly how to read and write values. Full roundtrip fidelity for every type — read a value from the database and write it back without loss or corruption.

## Databases

- **[PostgreSQL](./postgresql)** — arrays, ranges, geometric types, network types, JSON, text search, and all the exotic ones
- **[MariaDB / MySQL](./mariadb)** — unsigned integers, spatial types, sets, JSON, and year types
- **[DuckDB](./duckdb)** — lists, structs, maps, unions, enums, and 128-bit integers
- **[Oracle](./oracle)** — OBJECT types, nested tables, VARRAYs, intervals, and LOB types
- **[SQL Server](./sqlserver)** — geography, geometry, hierarchyid, and Unicode types
- **[DB2](./db2)** — standard SQL types with DB2-specific handling

## Type-Safe Database Types

Each database has its own typed hierarchy:

<Tabs groupId="language">
<TabItem value="java" label="Java">

```java
// PostgreSQL types
PgType<int[]> intArray = PgTypes.int4.array();
PgType<Range<LocalDate>> dateRange = PgTypes.daterange;
PgType<PGpoint> point = PgTypes.point;

// MariaDB types
MariaType<JsonNode> json = MariaTypes.json(mapper);

// Oracle types
OracleType<List<MyObject>> nested = OracleTypes.nestedTable("MY_TYPE", myObjectType);

// DuckDB types
DuckDbType<Map<String, Integer>> map = DuckDbTypes.map(DuckDbTypes.varchar, DuckDbTypes.integer);
```

</TabItem>
<TabItem value="kotlin" label="Kotlin">

```kotlin
// PostgreSQL types
val intArray: PgType<IntArray> = PgTypes.int4.array()
val dateRange: PgType<Range<LocalDate>> = PgTypes.daterange

// MariaDB types
val json: MariaType<JsonNode> = MariaTypes.json(mapper)

// DuckDB types
val map: DuckDbType<Map<String, Int>> = DuckDbTypes.map(DuckDbTypes.varchar, DuckDbTypes.integer)
```

</TabItem>
<TabItem value="scala" label="Scala">

```scala
// PostgreSQL types
val intArray: PgType[Array[Int]] = PgTypes.int4.array()
val dateRange: PgType[Range[LocalDate]] = PgTypes.daterange

// MariaDB types
val json: MariaType[JsonNode] = MariaTypes.json(mapper)

// DuckDB types
val map: DuckDbType[Map[String, Int]] = DuckDbTypes.map(DuckDbTypes.varchar, DuckDbTypes.integer)
```

</TabItem>
</Tabs>
