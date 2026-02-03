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

Query Analysis uses JDBC metadata to verify your queries against the actual database schema. Run it in your test suite, and you'll know immediately when:

1. **Parameter types don't match** — You're passing a String where the database expects an Integer
2. **Column types don't match** — Your RowParser expects a timestamp but the column is a date
3. **Nullability is wrong** — The column is nullable but your type isn't Optional
4. **Counts are off** — Your RowParser expects 5 columns but the query returns 4

## Basic Usage

<Snippet file="analysis/QueryAnalysisBasic" />

## Reading the Report

When analysis fails, you get a detailed report showing exactly what went wrong:

```
╔══════════════════════════════════════════════════════════════════════════════╗
║  Query Analysis Report                                                       ║
╚══════════════════════════════════════════════════════════════════════════════╝

SQL:
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
     │ Declared: int4 (JDBC: INTEGER)
     │ Returned: timestamptz (JDBC: TIMESTAMP_WITH_TIMEZONE)
     └ The declared type cannot read from TIMESTAMP_WITH_TIMEZONE

  2. Column 4 'status' is returned by query (varchar / VARCHAR) but not declared in RowParser
```

## Error Types

### Parameter Type Mismatch

When you pass a parameter of the wrong type:

```
Parameter 1: type mismatch
  │ Declared: text (JDBC: VARCHAR)
  │ Expected: int4 (JDBC: INTEGER)
  └ The declared type cannot write to INTEGER
```

**Fix:** Change the parameter type to match what the database expects.

### Column Type Mismatch

When your RowParser expects a different type than the database returns:

```
Column 2 'price': type mismatch
  │ Declared: int4 (JDBC: INTEGER)
  │ Returned: numeric (JDBC: DECIMAL)
  └ The declared type cannot read from DECIMAL
```

**Fix:** Use the correct DbType in your RowParser. Here, use `PgTypes.numeric` instead of `PgTypes.int4`.

### Nullability Mismatch

When a nullable column isn't wrapped in Optional:

```
Column 3 'email': nullability mismatch
  │ The database says this column is nullable
  │ But the type text is not Optional
  └ Use .opt() to make the type nullable, or ensure the column is NOT NULL
```

**Fix:** Use `.opt()` on the type: `PgTypes.text.opt()` instead of `PgTypes.text`.

### Missing Column

When your RowParser expects more columns than the query returns:

```
Column 5 is declared in RowParser (boolean) but not returned by query
```

**Fix:** Either add the missing column to your SELECT, or remove it from your RowParser.

### Extra Column

When the query returns more columns than your RowParser expects:

```
Column 4 'updated_at' is returned by query (timestamptz / TIMESTAMP_WITH_TIMEZONE)
but not declared in RowParser
```

**Fix:** Either add the column to your RowParser, or remove it from your SELECT.

## Test Suite Integration

The recommended pattern is to analyze all your queries in a dedicated test:

<Snippet file="analysis/QueryAnalysisTestSuite" />

## What Gets Analyzed

### Query Operations

```java
// Full query with parameters and result parser
QueryAnalyzer.analyze(fragment.query(rowParser.all()), conn);

// Update-returning operations
QueryAnalyzer.analyze(fragment.updateReturning(rowParser), conn);
```

### Update Operations (Parameters Only)

```java
// Updates have no result columns, only parameters
QueryAnalyzer.analyze(fragment.update(), conn);
```

### Low-Level Analysis

```java
// Analyze a fragment + parser directly
QueryAnalyzer.analyzeFragmentAndParser(fragment, resultSetParser, conn);
```

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

## How It Works

1. **Extract declared types** — The Fragment knows the DbType of each parameter. The RowParser knows the DbType of each column.

2. **Prepare the statement** — We call `connection.prepareStatement(sql)` to get JDBC metadata.

3. **Extract JDBC metadata** — ParameterMetaData tells us what types the database expects for parameters. ResultSetMetaData tells us what types the query returns.

4. **Align and compare** — We line up declared types with JDBC metadata by position and check for compatibility.

5. **Report errors** — Any mismatches become detailed error messages explaining exactly what's wrong and how to fix it.

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
        var analysis = QueryAnalyzer.analyze(myQuery, conn);
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
static <T> QueryAnalysis analyze(Operation.Query<T> query, Connection conn)

// Analyze an update-returning operation
static <T> QueryAnalysis analyze(Operation.UpdateReturning<T> op, Connection conn)

// Analyze an update operation (parameters only)
static QueryAnalysis analyze(Operation.Update update, Connection conn)

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
String report()

// Access raw alignment data
List<Alignment<DbType<?>, JdbcMeta.ParameterMeta>> parameterAlignment()
List<Alignment<DbType<?>, JdbcMeta.ColumnMeta>> columnAlignment()
```

### AlignmentError Types

```java
sealed interface AlignmentError {
    int position();
    String message();

    // Parameter errors
    record ExtraParameter(int position, DbType<?> type)
    record MissingParameter(int position, JdbcMeta.ParameterMeta meta)
    record ParameterTypeMismatch(int position, DbType<?> declared,
        JdbcMeta.ParameterMeta expected, Set<Integer> declaredJdbcTypes, String reason)

    // Column errors
    record ExtraColumn(int position, DbType<?> type)
    record MissingColumn(int position, JdbcMeta.ColumnMeta meta)
    record ColumnTypeMismatch(int position, String columnName, DbType<?> declared,
        JdbcMeta.ColumnMeta returned, Set<Integer> declaredJdbcTypes, String reason)
    record NullabilityMismatch(int position, String columnName, DbType<?> type)
}
```
