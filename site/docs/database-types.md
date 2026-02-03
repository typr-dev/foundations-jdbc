---
title: Supported Databases
---

import Snippet from '@site/src/components/Snippet';

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

<Snippet file="dbtypes/TypeSafeDbTypes" />
