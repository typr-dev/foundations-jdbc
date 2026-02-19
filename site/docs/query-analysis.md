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
2. **Column types don't match** — Your RowParser expects a timestamp but the column is a date
3. **Nullability is wrong** — The column is nullable but your type isn't Optional
4. **Counts are off** — Your RowParser expects 5 columns but the query returns 4

## Basic Usage

<Snippet file="analysis/QueryAnalysisBasic" />

## Named Queries

Give your queries names for clearer error reports:

<Snippet file="analysis/QueryAnalysisNamed" />

Named queries show the name in the report header, making it easy to find which query failed in a large test suite.

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

  2. Column 4 'status' is returned by query (varchar) but not declared in RowParser
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

When your RowParser expects a different type than the database returns:

```
Column 2 'price': type mismatch
  │ Declared: int4 (accepts: int4)
  │ Returned: numeric
  └ The declared type does not match the returned vendor type "numeric"
```

**Fix:** Use the correct DbType in your RowParser. Here, use `PgTypes.numeric` instead of `PgTypes.int4`.

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

When your RowParser expects more columns than the query returns:

```
Column 5 is declared in RowParser (boolean) but not returned by query
```

**Fix:** Either add the missing column to your SELECT, or remove it from your RowParser.

### Extra Column

When the query returns more columns than your RowParser expects:

```
Column 4 'updated_at' is returned by query (timestamptz) but not declared in RowParser
```

**Fix:** Either add the column to your RowParser, or remove it from your SELECT.

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

## Test Suite Integration

The recommended pattern is to analyze all your queries in a dedicated test.

[SQL Templates](./sql-templates) are ideal for this — since their parameters are unbound, you can analyze them without providing concrete values:

<Snippet file="analysis/QueryAnalysisTestSuite" />

## Analyzing Composed Operations

When you compose operations with `.with()`, `.then()`, or `Operation.ifEmpty()`, you can verify every SQL statement in the tree with a single call:

<Snippet file="analysis/QueryAnalysisAll" />

This walks the entire operation tree and returns one `QueryAnalysis` per SQL statement found.

## Dynamic SQL Analysis

When a template uses `.optionally()`, analysis automatically expands all 2^N structural variants and verifies each one against the database. See [Dynamic SQL](./optional-queries) for details.

## What Gets Analyzed

### Query Operations

```java
// Full query with parameters and result parser
QueryAnalyzer.analyze(fragment.query(rowParser.all()), conn).getFirst();

// Named query
QueryAnalyzer.analyze("findUsers", fragment.query(rowParser.all()), conn).getFirst();

// Update-returning operations
QueryAnalyzer.analyze(fragment.updateReturning(rowParser), conn).getFirst();
```

### Update Operations (Parameters Only)

```java
// Updates have no result columns, only parameters
QueryAnalyzer.analyze(fragment.update(), conn).getFirst();

// Named update
QueryAnalyzer.analyze("updateUser", fragment.update(), conn).getFirst();
```

### Low-Level Analysis

```java
// Analyze a fragment + parser directly
QueryAnalyzer.analyzeFragmentAndParser(fragment, resultSetParser, conn);
```

## How It Works

1. **Extract declared types** — The Fragment knows the DbType of each parameter. The RowParser knows the DbType of each column.

2. **Prepare the statement** — We call `connection.prepareStatement(sql)` to get JDBC metadata.

3. **Extract vendor type names** — ParameterMetaData and ResultSetMetaData provide vendor-specific type names (e.g., `int4`, `varchar`, `timestamptz`).

4. **Normalize and compare** — Type names are normalized (lowercased, precision stripped) and compared against the declared type's vendor type names. For example, `VARCHAR(255)` and `VARCHAR` both normalize to `varchar`.

5. **Report errors** — Any mismatches become detailed error messages explaining exactly what's wrong and how to fix it.

## Database Support

Query Analysis works with all supported databases, with some caveats:

| Database | Parameter Metadata | Column Metadata | Nullability |
|----------|-------------------|-----------------|-------------|
| PostgreSQL | Full | Full | Reliable |
| DuckDB | Limited | Full | All nullable* |
| Oracle | Full | Full | Reliable |
| SQL Server | Full | Full | Reliable |
| MariaDB/MySQL | Limited** | Full | Reliable |
| DB2 | Full | Full | Reliable |

\* DuckDB reports all columns as nullable; nullability checks are skipped.

\*\* MariaDB/MySQL parameter metadata is not always reliable; parameter type checks may be skipped.

## Tips

### Use Meaningful Test Data

Analysis only checks types, not data. You don't need real data in your tables — just the schema:

```java
conn.createStatement().execute("""
    CREATE TABLE users (
        id SERIAL PRIMARY KEY,
        name TEXT NOT NULL,
        email TEXT,
        created_at TIMESTAMPTZ DEFAULT now()
    )
""");
```

### Check During Development

Run query analysis as you develop, not just in CI. Catch errors early:

```java
// Add a quick check in your main during development
public static void main(String[] args) throws SQLException {
    try (var conn = getConnection()) {
        var analysis = QueryAnalyzer.analyze(myQuery, conn).getFirst();
        System.out.println(analysis.report());
    }
}
```

### Analysis is Cheap

Preparing a statement and reading metadata is fast — milliseconds per query. You can check hundreds of queries in a single test.

## API Reference

### QueryAnalyzer

```java
// Analyze a query operation
static <T> List<QueryAnalysis> analyze(Operation.Query<T> query, Connection conn)

// Analyze a named query operation
static <T> List<QueryAnalysis> analyze(String name, Operation.Query<T> query, Connection conn)

// Analyze an update-returning operation
static <T> List<QueryAnalysis> analyze(Operation.UpdateReturning<T> op, Connection conn)
static <T> List<QueryAnalysis> analyze(String name, Operation.UpdateReturning<T> op, Connection conn)

// Analyze an update operation (parameters only)
static List<QueryAnalysis> analyze(Operation.Update update, Connection conn)
static List<QueryAnalysis> analyze(String name, Operation.Update update, Connection conn)

// Analyze all SQL in a composed operation tree
static List<QueryAnalysis> analyze(Operation<?> operation, Connection conn)

// Low-level: analyze fragment + parser directly
static QueryAnalysis analyzeFragmentAndParser(
    Fragment fragment,
    ResultSetParser<?> parser,
    Connection conn)
```

### QueryAnalysis

```java
// Did the analysis pass?
boolean succeeded()

// Get all errors
List<AlignmentError> allErrors()
List<AlignmentError> parameterErrors()
List<AlignmentError> columnErrors()

// Generate human-readable report
String report()          // plain text
String reportColored()   // with ANSI color codes

// Access raw alignment data
List<Alignment<DbType<?>, JdbcMeta.ParameterMeta>> parameterAlignment()
List<Alignment<DbType<?>, JdbcMeta.ColumnMeta>> columnAlignment()
```

### QueryChecker (Test Interface)

```java
interface QueryChecker {
    Transactor transactor();

    // Check any operation
    void check(Operation<?> op)
    void check(String name, Operation<?> op)

    // Check fragments with parsers
    void check(Fragment fragment, ResultSetParser<?> parser)
    void check(Fragment fragment, RowParser<?> parser)

    // Batch check
    void checkAll(Operation<?>... operations)

    // Routine analysis
    void checkRoutine(Procedure<?> procedure)
}
```

### AnalysisOptions

```java
// Escape hatches on any DbType
PgTypes.text.unchecked()     // skip all type checking for this column/parameter
PgTypes.text.nullableOk()    // suppress nullability mismatch warnings
```

### AlignmentError Types

```java
sealed interface AlignmentError {
    int position();
    String message();
    Str styledMessage();

    // Parameter errors
    record ExtraParameter(int position, DbType<?> type)
    record MissingParameter(int position, JdbcMeta.ParameterMeta meta)
    record ParameterTypeMismatch(int position, DbType<?> declared,
        JdbcMeta.ParameterMeta expected, Set<String> declaredTypeNames, String reason)

    // Column errors
    record ExtraColumn(int position, DbType<?> type)
    record MissingColumn(int position, JdbcMeta.ColumnMeta meta)
    record ColumnTypeMismatch(int position, String columnName, DbType<?> declared,
        JdbcMeta.ColumnMeta returned, Set<String> declaredTypeNames, String reason)
    record NullabilityMismatch(int position, String columnName, DbType<?> type)
}
```
