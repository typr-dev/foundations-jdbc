---
title: Query Analysis
---

import Snippet from '@site/src/components/Snippet';

# Query Analysis

**Catch SQL type errors at test time, not runtime.**

Query Analysis is foundations-jdbc's answer to the question: "How do I know my SQL queries will actually work?" Inspired by [doobie's type checking](https://tpolecat.github.io/doobie/docs/17-Typechecking.html), it verifies that your code's types match what the database expects — before your code ever runs in production.

## The Problem

Traditional JDBC gives you no compile-time or test-time feedback about your queries. You write SQL, you guess at types, and you pray. Errors show up as:

- `ClassCastException` in production
- Silent data truncation
- `NullPointerException` from nullable columns you forgot about
- Mysterious "wrong number of parameters" errors

## The Solution

Query Analysis uses JDBC metadata to verify your queries against the actual database schema. It compares vendor type names (e.g., `int4`, `varchar`, `timestamptz`) directly — no JDBC integer code mapping needed. Run it in your test suite, and you'll know immediately when:

1. **Parameter types don't match** — You're passing a String where the database expects an Integer
2. **Column types don't match** — Your RowCodec expects a timestamp but the column is a date
3. **Nullability is wrong** — The column is nullable but your type isn't Optional
4. **Counts are off** — Your RowCodec expects 5 columns but the query returns 4

## Basic Usage

`AnalyzableScanner` scans a package and discovers every query, template, and operation. `QueryChecker` verifies them all against the database. Together, they give you a single test that covers your entire data layer:

<Snippet file="analysis/QueryAnalysisTestSuite" />

Add a new query anywhere in the package, and it's automatically included in the next test run. No manual list maintenance.

### What the Scanner Finds

The scanner discovers fields that implement `Analyzable` — this includes `Operation`, `Template`, and `RowTemplate`. It handles all three JVM languages:

| Source | How it's found |
|--------|----------------|
| **Java classes** | Instantiated via no-arg constructor. Instance fields are scanned. |
| **Kotlin objects** | Discovered via `INSTANCE` singleton. All `val` properties are scanned. |
| **Scala objects** | Discovered via `MODULE$` singleton. All `val` fields are scanned. |

The scanner recurses into subpackages, so `scan("com.myapp")` finds queries in `com.myapp.users`, `com.myapp.orders`, etc.

Each discovered query is automatically named `ClassName.fieldName` (e.g. `UserRepo.findById`), so error reports pinpoint exactly which query failed.

:::tip
For classes that need a database connection at construction time, pass a `Transactor` to the scanner:
```java
AnalyzableScanner.scan("com.myapp.db", transactor)
```
The scanner will try constructors that accept a `Transactor` parameter.
:::

### Manual Check

Some queries can't be discovered by the scanner — for example, queries built dynamically inside methods, or queries in classes that require constructor arguments the scanner can't provide. Use `checker.check()` to verify these individually:

<Snippet file="analysis/QueryAnalysisBasic" />

## Named Queries

Give your queries names for clearer error reports:

<Snippet file="analysis/QueryAnalysisNamed" />

Named queries show the name in the report header, making it easy to find which query failed in a large test suite. The scanner names queries automatically (`ClassName.fieldName`), so naming is mainly useful for manual checks.

## Reading the Report

When analysis fails, you get a detailed report showing exactly what went wrong:

```
╔══════════════════════════════════════════════════════════════════════════════╗
║  Query Analysis Report                                                       ║
╚══════════════════════════════════════════════════════════════════════════════╝

SQL (findUserById):
  SELECT id, name, created_at, status FROM users WHERE id = ?

┌─ Parameters ─────────────────────────────────────────────────────────────────┐
│  ✓ param[1]: int4                 → int4                                     │
└──────────────────────────────────────────────────────────────────────────────┘

┌─ Columns ────────────────────────────────────────────────────────────────────┐
│  ✓ col[1]: int4                   → id : int4                                │
│  ✓ col[2]: text                   → name : varchar                           │
│  ✗ col[3]: int4                   → created_at : timestamptz                 │
│  ✗ col[4]: (missing)              → status : varchar                         │
└──────────────────────────────────────────────────────────────────────────────┘

✗ 2 error(s) found:

  1. Column 3 'created_at': type mismatch
     │ Declared: int4 (accepts: int4)
     │ Returned: timestamptz
     └ The declared type does not match the returned vendor type "timestamptz"

  2. Column 4 'status' is returned by query (varchar) but not declared in RowCodec
```

## Error Types

### Parameter Type Mismatch

When you pass a parameter of the wrong type:

```
Parameter 1: type mismatch
  │ Declared: text (accepts: text)
  │ Expected: int4
  └ The declared type does not match the expected vendor type "int4"
```

**Fix:** Change the parameter type to match what the database expects.

### Column Type Mismatch

When your RowCodec expects a different type than the database returns:

```
Column 2 'price': type mismatch
  │ Declared: int4 (accepts: int4)
  │ Returned: numeric
  └ The declared type does not match the returned vendor type "numeric"
```

**Fix:** Use the correct DbType in your RowCodec. Here, use `PgTypes.numeric` instead of `PgTypes.int4`.

### Nullability Mismatch

When a nullable column isn't wrapped in Optional:

```
Column 3 'email': nullability mismatch
  │ The database says this column is nullable
  │ But the type text is not Optional
  └ Use .opt() to make the type nullable, .nullableOk() to suppress this warning,
    or ensure the column is NOT NULL
```

**Fix:** Use `.opt()` on the type: `PgTypes.text.opt()` instead of `PgTypes.text`. Or use `.nullableOk()` if you know it's safe (see [Escape Hatches](#escape-hatches)).

### Missing Column

When your RowCodec expects more columns than the query returns:

```
Column 5 is declared in RowCodec (boolean) but not returned by query
```

**Fix:** Either add the missing column to your SELECT, or remove it from your RowCodec.

### Extra Column

When the query returns more columns than your RowCodec expects:

```
Column 4 'updated_at' is returned by query (timestamptz) but not declared in RowCodec
```

**Fix:** Either add the column to your RowCodec, or remove it from your SELECT.

## Escape Hatches

Sometimes strict type checking is too strict. Two escape hatches let you selectively relax checking:

### `.nullableOk()` — Suppress Nullability Warnings

Use when you know a column won't be null in practice, even though the database says it could be. Common with outer joins:

<Snippet file="analysis/QueryAnalysisNullableOk" />

### `.unchecked()` — Skip Type Checking Entirely

Use when you know the type is correct but the database metadata disagrees, or for computed columns with unpredictable types:

<Snippet file="analysis/QueryAnalysisUnchecked" />

## Routine Analysis

Verify stored procedures and functions against the database:

<Snippet file="analysis/QueryAnalysisRoutine" />

Routine analysis checks:
- The routine exists in the database
- Parameter count matches
- Parameter types match (by vendor type name)
- Parameter modes match (IN, OUT, INOUT)
- Return type matches (for functions)

## Analyzing Composed Operations

When you compose operations with `.combine()`/`.combineWith()`, `.then()`, or `Operation.ifEmpty()`, the checker walks the entire operation tree and verifies every SQL statement:

<Snippet file="analysis/QueryAnalysisAll" />

This walks the entire operation tree and returns one `QueryAnalysis` per SQL statement found.

## Dynamic SQL Analysis

When a template uses [`.optionally()`](./templates#dynamic-templates), analysis automatically expands all 2^N structural variants. Each variant is prepared against the database and verified independently.

For example, a template with 3 optional predicates produces 8 combinations — all checked with a single `checker.check()` call:

| name filter | email filter | active flag | SQL WHERE clause |
|:-----------:|:------------:|:-----------:|:-----------------|
| absent | absent | absent | `WHERE 1=1 ORDER BY name` |
| present | absent | absent | `WHERE 1=1 AND name ILIKE ? ORDER BY name` |
| absent | present | absent | `WHERE 1=1 AND email ILIKE ? ORDER BY name` |
| present | present | absent | `WHERE 1=1 AND name ILIKE ? AND email ILIKE ? ORDER BY name` |
| absent | absent | present | `WHERE 1=1 AND active = TRUE ORDER BY name` |
| present | absent | present | `WHERE 1=1 AND name ILIKE ? AND active = TRUE ORDER BY name` |
| absent | present | present | `WHERE 1=1 AND email ILIKE ? AND active = TRUE ORDER BY name` |
| present | present | present | `WHERE 1=1 AND name ILIKE ? AND email ILIKE ? AND active = TRUE ORDER BY name` |

If any variant has a type error, the analysis report tells you exactly which combination failed and why.

## Further Reading

See [Query Analysis Reference](./query-analysis-reference) for internals, database support matrix, and API reference.
