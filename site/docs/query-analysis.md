---
title: Query Analysis
---

import Snippet from '@site/src/components/Snippet';
import ThemedImg from '@site/src/components/ThemedImg';

# Query Analysis

**Catch SQL type errors at test time, not runtime.**

Query Analysis verifies that your code's types match what the database expects, before your code runs in production. Inspired by [doobie's type checking](https://tpolecat.github.io/doobie/docs/17-Typechecking.html).

:::warning Database-specific limitations
Query Analysis relies on metadata reported by each database's JDBC driver, and not all drivers report equally well. Column type checking works everywhere, but parameter type checking and nullability checking vary. See [Database Behavior](./query-analysis-database-behavior) for the full breakdown.
:::

## The problem

JDBC gives you no compile-time or test-time feedback about your queries. Errors show up as:

- `ClassCastException` in production
- Silent data truncation
- `NullPointerException` from nullable columns you forgot about
- Mysterious "wrong number of parameters" errors

## The solution

Query Analysis uses JDBC metadata to verify your queries against the actual database schema. It compares vendor type names (`int4`, `varchar`, `timestamptz`) directly. Run it in your test suite to catch:

1. **Parameter types don't match** — You're passing a String where the database expects an Integer
2. **Column types don't match** — Your RowCodec expects a timestamp but the column is a date
3. **Nullability is wrong** — The column is nullable but your type isn't Optional
4. **Counts are off** — Your RowCodec expects 5 columns but the query returns 4

:::tip Dynamic queries are first-class
Queries built with the [`optionally` DSL](./dynamic-queries) are expanded into all 2<sup>N</sup> branch variants and verified individually. Every possible runtime SQL shape is checked, including ones your test never happened to construct. See [Dynamic Queries](./dynamic-queries) for the trade-offs against list-based composition.
:::

<div style={{display: 'flex', gap: '1rem', flexWrap: 'wrap', margin: '1.5rem 0'}}>
<ThemedImg light="/img/qa-type-mismatch-light.png" dark="/img/qa-type-mismatch-dark.png" alt="Query analysis: type mismatch detected" style={{maxWidth: '48%'}} />
<ThemedImg light="/img/qa-nullability-light.png" dark="/img/qa-nullability-dark.png" alt="Query analysis: nullability mismatch detected" style={{maxWidth: '48%'}} />
</div>

## Basic usage

`AnalyzableScanner` scans a package and discovers every query and operation. `QueryChecker` verifies them all against the database. One test covers your entire data layer:

<Snippet file="analysis/QueryAnalysisTestSuite" />

Add a new query anywhere in the package, and it's automatically included in the next test run. No manual list maintenance.

## What the scanner discovers

The scanner finds everything that returns an `Analyzable` type (`OperationRead` and `Operation`). It discovers both **fields** and **methods**:

<Snippet file="analysis/ScannerMethods" />

### Discovery rules

| What | How it's found |
|------|----------------|
| **Instance fields** | Any field whose type implements `Analyzable` |
| **Instance methods** (no args) | Called directly, return value collected |
| **Instance methods with parameters** | Dummy arguments constructed automatically, method invoked |
| **Static fields** | Discovered — useful for Kotlin top-level `val`, Java `static final OperationRead`, and static helper bags |
| **Static methods** (no args, with args) | Discovered — same dummy-argument construction as instance methods |
| **Private / protected / package-private members** | Discovered — the scanner uses `setAccessible(true)` since it's a test-scope tool |

### Class instantiation

The scanner handles all three JVM languages:

| Source | How it's found |
|--------|----------------|
| **Java classes** | Instantiated via no-arg constructor (or `Transactor` constructor). Fields and methods are scanned. |
| **Kotlin classes** | Same as Java — instantiated via no-arg constructor. Properties and methods are scanned. |
| **Kotlin objects** | Discovered via `INSTANCE` singleton. Properties and methods are scanned. |
| **Scala classes** | Same as Java — instantiated via no-arg constructor. Fields and methods are scanned. |
| **Scala objects** | Discovered via `MODULE$` singleton. Fields and methods are scanned. |

The scanner recurses into subpackages, so `scan("com.myapp")` finds queries in `com.myapp.users`, `com.myapp.orders`, etc.

Each discovered query is automatically named `ClassName.fieldName` or `ClassName.methodName` (e.g. `UserRepo.findById`), so error reports pinpoint exactly which query failed.

:::tip
For classes that need a database connection at construction time, pass a `Transactor` to the scanner:
```java
AnalyzableScanner.scan("com.myapp.db", transactor)
```
The scanner will try constructors that accept a `Transactor` parameter.
:::

### Dummy arguments

When the scanner encounters a method with parameters, it constructs dummy values to invoke the method. The actual argument values don't matter; the scanner only needs the method's return value (an `OperationRead` or `Operation`) to extract SQL and type information. If a method branches on its arguments and returns structurally different operations, use `manual()` directives to provide meaningful values.

The scanner can construct dummies for:

| Type | Dummy value |
|------|-------------|
| Primitives (`int`, `boolean`, etc.) | Default values (`0`, `false`, etc.) |
| `String` | `""` |
| `BigDecimal`, `BigInteger` | `ZERO` |
| `UUID` | `new UUID(0, 0)` |
| `LocalDate`, `Instant`, etc. | Epoch / 2000-01-01 |
| `Optional`, `List`, `Set`, `Map` | Empty |
| Arrays | Empty array |
| Enums | First constant |
| Records | Recursive construction of components |
| Classes with constructors | Tries shortest constructor first |

If a parameter type can't be constructed (e.g., an interface like `Runnable`, or an abstract class), the scanner will **fail with an error**. Handle these methods explicitly using [Scan Directives](#scan-directives): either `skip()` to exclude them or `manual()` to provide the arguments yourself.

### Getter deduplication

In Kotlin and Scala, properties generate both a backing field and a getter method. The scanner automatically deduplicates these: if a field named `query` exists, a no-arg method named `query()` (Scala-style) or `getQuery()` (Kotlin-style) is treated as a getter and skipped.

Methods with parameters are never treated as getters, even if they share a name with a field.

## Scan directives

When the scanner encounters a method it can't auto-invoke, it fails with an error telling you which method and why. **Scan directives** tell the scanner how to handle these methods.

### `skip()`: exclude a method

Use `skip()` when a method shouldn't be type-checked at all:

<Snippet file="analysis/ScannerDirectives" />

### `manual()`: provide specific arguments

Use `manual()` when you want a method to be type-checked but the scanner can't construct the right arguments. You provide a variant name, call the method yourself, and pass the result:

<Snippet file="analysis/ScannerDirectivesManual" />

You can provide multiple manual variants for the same method. Each gets its own type check:

```java
ScanDirective.manual(repo::search, "by-name", new Filter("alice", 10)),
ScanDirective.manual(repo::search, "all", new Filter("", 100))
```

Each variant appears in reports as `ClassName.methodName[variantName]`.

### `instance()`: add objects from outside the scan package

Use `instance()` to include objects that live outside the scanned package, or that need special construction:

<Snippet file="analysis/ScannerInstance" />

The `instance()` directive also supports per-instance overrides: skip or provide manual entries for specific methods on that instance.

## Manual check

Some queries can't be discovered by the scanner: queries built dynamically inside methods, or queries in classes that require constructor arguments the scanner can't provide. Use `checker.check()` to verify these individually:

<Snippet file="analysis/QueryAnalysisBasic" />

## Named queries

Give your queries names for clearer error reports:

<Snippet file="analysis/QueryAnalysisNamed" />

Named queries show the name in the report header, making it easy to find which query failed in a large test suite. The scanner names queries automatically (`ClassName.fieldName`), so naming is mainly useful for manual checks.

## Reading the report

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

## Error types

### Parameter type mismatch

When you pass a parameter of the wrong type:

```
Parameter 1: type mismatch
  │ Declared: text (accepts: text)
  │ Expected: int4
  └ The declared type does not match the expected vendor type "int4"
```

**Fix:** Change the parameter type to match what the database expects.

### Column type mismatch

When your RowCodec expects a different type than the database returns:

```
Column 2 'price': type mismatch
  │ Declared: int4 (accepts: int4)
  │ Returned: numeric
  └ The declared type does not match the returned vendor type "numeric"
```

**Fix:** Use the correct DbType in your RowCodec. Here, use `PgTypes.numeric` instead of `PgTypes.int4`.

### Nullability mismatch

When a nullable column isn't wrapped in Optional:

```
Column 3 'email': nullability mismatch
  │ The database says this column is nullable
  │ But the type text is not Optional
  └ Use .opt() to make the type nullable, .nullableOk() to suppress this warning,
    or ensure the column is NOT NULL
```

**Fix:** Use `.opt()` on the type: `PgTypes.text.opt()` instead of `PgTypes.text`. Or use `.nullableOk()` if you know it's safe (see [Escape Hatches](#escape-hatches)).

### Missing column

When your RowCodec expects more columns than the query returns:

```
Column 5 is declared in RowCodec (boolean) but not returned by query
```

**Fix:** Either add the missing column to your SELECT, or remove it from your RowCodec.

### Extra column

When the query returns more columns than your RowCodec expects:

```
Column 4 'updated_at' is returned by query (timestamptz) but not declared in RowCodec
```

**Fix:** Either add the column to your RowCodec, or remove it from your SELECT.

## Escape hatches

Two escape hatches let you selectively relax checking:

### `.nullableOk()`: suppress nullability warnings

Use when you know a column won't be null in practice, even though the database says it could be. Common with outer joins:

<Snippet file="analysis/QueryAnalysisNullableOk" />

### `.unchecked()`: skip type checking entirely

Use when you know the type is correct but the database metadata disagrees, or for computed columns with unpredictable types:

<Snippet file="analysis/QueryAnalysisUnchecked" />

## Routine analysis

Verify stored procedures and functions against the database:

<Snippet file="analysis/QueryAnalysisRoutine" />

Routine analysis checks:
- The routine exists in the database
- Parameter count matches
- Parameter types match (by vendor type name)
- Parameter modes match (IN, OUT, INOUT)
- Return type matches (for functions)

## Analyzing composed operations

When you compose operations with `.combine()`/`.combineWith()`, `.then()`, or `OperationRead.ifEmpty()`, the checker walks the entire operation tree and verifies every SQL statement:

<Snippet file="analysis/QueryAnalysisAll" />

This returns one `QueryAnalysis` per SQL statement found in the tree.

## Dynamic SQL analysis

When a fragment uses [`.optionally()`](./dynamic-queries), analysis automatically expands all 2^N structural variants. Each variant is prepared against the database and verified independently.

For example, an operation with 3 optional predicates produces 8 combinations — all checked with a single `checker.check()` call:

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

## Further reading

- [Query Analysis: Database Behavior](./query-analysis-database-behavior) — detailed breakdown of what each database's JDBC driver reports and how it affects analysis
