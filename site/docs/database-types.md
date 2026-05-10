---
title: Supported Databases
---

import Snippet from '@site/src/components/Snippet';

# Supported databases

Each database has its own type hierarchy that knows how to read and write values. The aim is roundtrip fidelity: read a value, write it back, get the same bytes.

## Databases

- **[PostgreSQL](./postgresql)** — arrays, ranges, geometric types, network types, JSON, text search, and the rest
- **[MariaDB / MySQL](./mariadb)** — unsigned integers, spatial types, sets, JSON, and year types
- **[DuckDB](./duckdb)** — lists, structs, maps, unions, enums, and 128-bit integers
- **[SQLite](./sqlite)** — five storage classes with affinity-based aliases; embedded, no procedures, no nested types
- **[Oracle](./oracle)** — OBJECT types, nested tables, VARRAYs, intervals, and LOB types
- **[SQL Server](./sqlserver)** — geography, geometry, hierarchyid, and Unicode types
- **[DB2](./db2)** — standard SQL types with DB2-specific handling

## Type-safe database types

Each database has its own typed hierarchy:

<Snippet file="dbtypes/TypeSafeDbTypes" />
