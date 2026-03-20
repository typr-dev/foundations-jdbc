---
title: "Query Analysis: Database Behavior"
---

# Query Analysis: Database Behavior

Query Analysis relies on JDBC metadata APIs (`ResultSetMetaData` and `ParameterMetaData`) to verify your queries. These APIs are implemented differently by each database's JDBC driver, which affects what the analyzer can check. This page documents the exact behavior for each supported database.

## Summary

| Capability | PostgreSQL | MariaDB | DuckDB | SQL Server | Oracle | DB2 |
|---|---|---|---|---|---|---|
| Column types | ✅ Full | ✅ Full | ✅ Full | ✅ Full | ✅ Full | ✅ Full |
| Column nullability | 🟡 Partial | ✅ Full | ❌ None | 🟡 Partial | 🟡 Partial | ✅ Full |
| Outer join nullability | ❌ No | ✅ Full | ❌ No | ✅ Full | ✅ Full | ✅ Full |
| Parameter types | ✅ Full | ❌ None | ❌ None | ✅ Full | ✅ Full | ✅ Full |

:::info Legend
- ✅ **Full** — The JDBC driver reports accurate information in all cases. The analyzer can catch all relevant errors.
- 🟡 **Partial** — Accurate for base table columns, but expressions, aggregates, CAST, and UNION results are reported as nullable or unknown regardless of actual nullability.
- ❌ **None / No** — The JDBC driver provides no useful information. The analyzer skips this category of checks entirely.
:::

## Column Type Checking

All six databases report column type names accurately. The analyzer can always verify that your `DbType` matches the actual column type, regardless of database.

Type names vary in case and format between databases, but the analyzer normalizes them before comparison (`VARCHAR(255)` becomes `varchar`, `DECIMAL(10,2)` becomes `decimal`).

## Column Nullability

The analyzer checks whether your code handles nullable columns correctly — if the database says a column is nullable, your type should use `.opt()`. However, the reliability of this information varies significantly.

### Full nullability (MariaDB, DB2)

MariaDB and DB2 report accurate nullability in all contexts:

- Base table columns: `NOT NULL` is reported as non-nullable, nullable columns as nullable
- Expressions (`id + 1`, `COALESCE(...)`, `CASE ... END`): correctly reported
- Aggregates: `COUNT(*)` correctly reported as non-nullable, `SUM`/`AVG`/`MAX` as nullable
- `CAST` expressions: correctly reported
- Subqueries and CTEs: nullability preserved from source columns

### Partial nullability (PostgreSQL, SQL Server, Oracle)

These databases report accurate nullability for base table columns and subqueries, but expressions, aggregates, and computed columns are reported as nullable or unknown even when they can never be null.

**PostgreSQL** reports expressions and aggregates as `columnNullableUnknown`. The analyzer treats this as "don't know" and skips the nullability check for those columns — no false positives, but no protection either.

**SQL Server** and **Oracle** report expressions, aggregates, `CAST`, and `COUNT(*)` as nullable. The analyzer's `isNullabilityReliable()` heuristic detects when all columns are reported as nullable and suppresses nullability checking for the entire query to avoid false positives. This means if you SELECT only computed columns, nullability mismatches won't be caught.

### No nullability (DuckDB)

DuckDB reports **every column as nullable**, including `NOT NULL` columns. The analyzer detects this and disables nullability checking entirely for DuckDB queries.

This means the analyzer cannot catch missing `.opt()` calls on DuckDB. If you declare `DuckDbTypes.bigint` for a nullable column, the analyzer will not flag it — you'll only discover the issue at runtime if the column contains a `NULL` value.

## Outer Join Nullability

When you write a `LEFT JOIN`, columns from the right table can be `NULL` even if they're declared `NOT NULL` in the schema. Ideally, the JDBC driver adjusts its nullability reporting to reflect this.

### Correctly adjusted (MariaDB, SQL Server, Oracle, DB2)

These four databases correctly report that `NOT NULL` columns on the outer side of a join become nullable:

```sql
-- p.id stays NOT NULL, c.id becomes nullable
SELECT p.id, c.id
FROM parent p LEFT JOIN child c ON p.id = c.parent_id
```

The analyzer will correctly require `.opt()` on `c.id` even though the column is `NOT NULL` in the `child` table.

### Not adjusted (PostgreSQL)

PostgreSQL does **not** adjust nullability for outer joins. A `NOT NULL` column on the outer side of a `LEFT JOIN` is still reported as `NOT NULL`:

```sql
-- PostgreSQL reports c.id as NOT NULL — incorrect for LEFT JOIN
SELECT p.id, c.id
FROM parent p LEFT JOIN child c ON p.id = c.parent_id
```

This means the analyzer **will not catch** a missing `.opt()` on the outer side of a join in PostgreSQL. Your code will compile and pass analysis, but fail at runtime with a `NullPointerException` if the join doesn't match.

**Workaround:** When using outer joins with PostgreSQL, always use the joined codec APIs (`.leftJoined()`, `.rightJoined()`, `.fullJoined()`) which produce `Optional`-wrapped types regardless of what the database reports.

### Irrelevant (DuckDB)

Since DuckDB reports all columns as nullable, outer join nullability adjustments are moot — everything is already nullable.

## Parameter Type Checking

The analyzer compares the types you declare for query parameters against what the database expects. This requires the JDBC driver to support `ParameterMetaData`.

### Full parameter types (PostgreSQL, SQL Server, Oracle, DB2)

These databases provide full parameter type information. The analyzer can detect when you pass a parameter of the wrong type:

```java
// If 'id' column is BIGINT, the analyzer catches this:
Fragment.of("SELECT * FROM users WHERE id = ?", PgTypes.text.encode("42"))
// Error: Parameter 1 type mismatch — declared: text, expected: int8
```

Parameter types are reported for all statement types: `SELECT`, `INSERT`, `UPDATE`, and within CTEs.

### No parameter types (MariaDB, DuckDB)

These databases do not provide usable parameter type metadata:

- **MariaDB**: The JDBC driver does not implement `ParameterMetaData`. The analyzer shows "(metadata not available)" in reports and skips parameter type checking entirely.
- **DuckDB**: The JDBC driver returns parameter count but reports all type names as `null` and JDBC type codes as `0`. The analyzer skips parameter type checking.

This means a type mismatch between your parameter and the column it's compared against **will not be caught** by the analyzer on these databases. It will only fail at runtime.

**Workaround:** There is no workaround — this is a JDBC driver limitation. Be extra careful with parameter types on MariaDB and DuckDB, and consider adding runtime tests with actual data for critical queries.

## SQL Construct Support

### CTEs (`WITH` clause)

All databases support CTE analysis. Column types and nullability are preserved through CTEs (subject to the database's general nullability behavior).

**SQL Server note:** SQL Server requires a semicolon before `WITH` if it's not the first statement. When using CTEs in your queries, ensure the SQL starts with `WITH` or is preceded by `;WITH`:

```sql
-- This works:
WITH cte AS (SELECT id FROM users) SELECT * FROM cte

-- This fails on SQL Server if preceded by another statement:
-- ...previous statement
-- WITH cte AS (...) SELECT * FROM cte  -- ERROR

-- Fix:
-- ...previous statement;
-- WITH cte AS (...) SELECT * FROM cte  -- OK
```

### Subqueries

All databases preserve column types and nullability through subqueries in the `FROM` clause.

### UNION ALL

All databases report correct column types for `UNION ALL` results. Nullability for `UNION ALL` results varies — some databases (PostgreSQL, Oracle) report unknown/nullable, while others (MariaDB, DB2) report the intersection of the source columns' nullability.

## Detailed Behavior Matrix

### Column Nullability by Context

| Context | PG | MariaDB | DuckDB | MSSQL | Oracle | DB2 |
|---|---|---|---|---|---|---|
| NOT NULL column | ✅ not null | ✅ not null | ❌ nullable | ✅ not null | ✅ not null | ✅ not null |
| Nullable column | ✅ nullable | ✅ nullable | ✅ nullable | ✅ nullable | ✅ nullable | ✅ nullable |
| `id + 1` | 🟡 unknown | ✅ not null | ❌ nullable | ❌ nullable | ❌ nullable | ✅ not null |
| `COALESCE(x, 0)` | 🟡 unknown | ✅ not null | ❌ nullable | ❌ nullable | ❌ nullable | ✅ not null |
| `CASE WHEN ...` | 🟡 unknown | ✅ not null | ❌ nullable | ✅ not null | ❌ nullable | ✅ not null |
| `COUNT(*)` | 🟡 unknown | ✅ not null | ❌ nullable | ❌ nullable | ❌ nullable | ✅ not null |
| `SUM(x)` | 🟡 unknown | ✅ nullable | ❌ nullable | ✅ nullable | ❌ nullable | ✅ nullable |
| `CAST(x AS type)` | 🟡 unknown | ❌ nullable | ❌ nullable | ❌ nullable | ❌ nullable | ✅ not null |
| Subquery column | ✅ preserved | ✅ preserved | ❌ nullable | ✅ preserved | ✅ preserved | ✅ preserved |
| CTE column | ✅ preserved | ✅ preserved | ❌ nullable | ✅ preserved | ✅ preserved | ✅ preserved |
| UNION ALL | 🟡 unknown | 🟡 mixed | ❌ nullable | 🟡 mixed | ❌ nullable | 🟡 mixed |
| LEFT JOIN outer | ❌ not null | ✅ nullable | ❌ nullable | ✅ nullable | ✅ nullable | ✅ nullable |

- ✅ = correct behavior
- 🟡 = safe but imprecise (unknown skips the check; mixed derives from sources)
- ❌ = incorrect or missing (may cause false negatives or false positives)

### Analyzer Behavior Consequences

| Scenario | PG | MariaDB | DuckDB | MSSQL | Oracle | DB2 |
|---|---|---|---|---|---|---|
| Missing `.opt()` on nullable column | ✅ | ✅ | ❌ | ✅ | ✅ | ✅ |
| Missing `.opt()` on LEFT JOIN outer side | ❌ | ✅ | ❌ | ✅ | ✅ | ✅ |
| Wrong parameter type | ✅ | ❌ | ❌ | ✅ | ✅ | ✅ |
| Wrong column type | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ |
| Wrong column count | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ |
| Aggregate type mismatch | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ |

## The `isNullabilityReliable()` Heuristic

The analyzer uses a heuristic to detect unreliable nullability metadata: if **every column** in a query result is reported as nullable, nullability checking is suppressed for that query. This prevents false positives on databases like DuckDB where all columns are always reported as nullable.

The heuristic works well for:
- **DuckDB**: All columns NULLABLE → heuristic triggers → nullability skipped (correct)
- **Oracle/MSSQL**: Expression-only queries report all NULLABLE → heuristic triggers → nullability skipped (correct)
- **PostgreSQL**: Expressions report UNKNOWN (not NULLABLE) → heuristic doesn't trigger → but UNKNOWN columns are individually skipped anyway (correct)

The heuristic can produce a false negative in one specific case: a query that selects **only nullable columns** from a table. In this case, all columns genuinely are nullable, the heuristic triggers, and it won't catch a missing `.opt()`. This is rare in practice and harmless — you'd need `.opt()` on all of them anyway.
