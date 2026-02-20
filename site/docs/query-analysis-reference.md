---
title: Query Analysis Reference
---

# Query Analysis Reference

This page covers the internals, database support, and API surface of [Query Analysis](./query-analysis).

## What Gets Analyzed

### Query Operations

```java
// Full query with parameters and result codec
QueryAnalyzer.analyze(fragment.query(rowCodec.all()), conn).getFirst();

// Named query
QueryAnalyzer.analyze("findUsers", fragment.query(rowCodec.all()), conn).getFirst();

// Update-returning operations
QueryAnalyzer.analyze(fragment.updateReturning(rowCodec), conn).getFirst();
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
// Analyze a fragment + codec directly
QueryAnalyzer.analyzeFragmentAndParser(fragment, resultSetParser, conn);
```

## How It Works

1. **Extract declared types** — The Fragment knows the DbType of each parameter. The RowCodec knows the DbType of each column.

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

    // Check fragments with codecs
    void check(Fragment fragment, ResultSetParser<?> parser)
    void check(Fragment fragment, RowCodec<?> codec)

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
