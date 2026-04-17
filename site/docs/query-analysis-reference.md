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

Query Analysis works with all supported databases. The quality of type checking depends on what each database's JDBC driver reports:

| Capability | PostgreSQL | MariaDB | DuckDB | SQL Server | Oracle | DB2 |
|---|---|---|---|---|---|---|
| Column types | ✅ Full | ✅ Full | ✅ Full | ✅ Full | ✅ Full | ✅ Full |
| Column nullability | 🟡 Partial | ✅ Full | ❌ None | 🟡 Partial | 🟡 Partial | ✅ Full |
| Outer join nullability | ❌ No | ✅ Full | ❌ No | ✅ Full | ✅ Full | ✅ Full |
| Parameter types | ✅ Full | ❌ None | ❌ None | ✅ Full | ✅ Full | ✅ Full |

Key limitations:
- **DuckDB** reports all columns as nullable and provides no parameter type info — nullability and parameter type checks are skipped.
- **MariaDB** provides no parameter type metadata — parameter type mismatches are not caught.
- **PostgreSQL** does not adjust nullability for outer joins — missing `.opt()` on the outer side of a LEFT/RIGHT JOIN is not caught.

See [Query Analysis: Database Behavior](./query-analysis-database-behavior) for the full breakdown.

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
public static void main(String[] args) {
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
// Analyze any Analyzable (Operation, Template, or named wrapper)
static List<QueryAnalysis> analyze(Analyzable analyzable, Connection conn)

// Analyze a template — expands all dynamic variants from .optionally()
static List<QueryAnalysis> analyze(Template<?, ?> template, Connection conn)

// Analyze an operation tree (handles composed operations from .combine(), .then(), etc.)
static List<QueryAnalysis> analyze(Operation<?> operation, Connection conn)

// Low-level: analyze fragment + parser directly
static QueryAnalysis analyzeFragmentAndParser(
    Fragment fragment,
    ResultSetParser<?> parser,
    Connection conn)
```

To attach a name to an analyzed query (shown in reports), call `.named(name)` on the
operation before passing it to `analyze`:

```java
var named = Fragment.of("SELECT ...").query(codec.all()).named("findUser");
QueryAnalyzer.analyze(named, conn);
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

    // Check any analyzable — throws AssertionError on failure
    void check(Analyzable analyzable)

    // Check fragments with codecs — throws AssertionError on failure
    void check(Fragment fragment, ResultSetParser<?> parser)
    <T> void check(Fragment fragment, RowCodec<T> codec)

    // Batch check — throws AssertionError if any query fails
    void checkAll(Analyzable... analyzables)
    void checkAll(List<? extends Analyzable> analyzables)

    // Batch analyze — returns report with per-query results, never throws
    CheckReport analyzeAll(Analyzable... analyzables)
    CheckReport analyzeAll(List<? extends Analyzable> analyzables)

    // Routine analysis
    void checkRoutine(RoutineDef def)
    void checkRoutine(Procedure<?> procedure)
}
```

Use `checkAll` for the "throw on any failure" pattern in tests. Use `analyzeAll` when
you want to inspect or print the report without failing the test.

### AnalysisOptions

```java
// Escape hatches on any DbType
PgTypes.text.unchecked()     // skip all type checking for this column/parameter
PgTypes.text.nullableOk()    // suppress nullability mismatch warnings
```

### AnalyzableScanner

```java
// Scan a package and return all discovered analyzables
static List<Analyzable> scan(String packageName)
static List<Analyzable> scan(String packageName, Transactor transactor)
static List<Analyzable> scan(String packageName, ScanDirective... directives)
static List<Analyzable> scan(String packageName, Transactor transactor, ScanDirective... directives)

// Scan with full metadata (class name, field/method name)
static List<Result> scanDetailed(String packageName)
static List<Result> scanDetailed(String packageName, Transactor transactor)
static List<Result> scanDetailed(String packageName, ScanDirective... directives)
static List<Result> scanDetailed(String packageName, Transactor transactor, ScanDirective... directives)

// Describe an analyzable's operation structure
static String describe(Analyzable analyzable)

// Result record
record Result(String className, String fieldName, Analyzable analyzable)
```

### ScanDirective (Java)

```java
sealed interface ScanDirective {
    // Skip a method — pass a method reference
    static ScanDirective skip(Ref0<?> ref)
    static <A> ScanDirective skip(Ref1<A, ?> ref)

    // Provide manual invocation — pass a method reference, variant name, and arguments
    static <R> ScanDirective manual(Ref0<R> ref, String variantName)
    static <A, R> ScanDirective manual(Ref1<A, R> ref, String variantName, A a)
    static <A, B, R> ScanDirective manual(Ref2<A, B, R> ref, String variantName, A a, B b)

    // Add an external object to the scan
    static ScanDirective instance(Object obj)
    static <T> ScanDirective instance(T obj, BiConsumer<T, InstanceConfig<T>> config)
}
```

### ScanDirective (Kotlin)

```kotlin
// Top-level functions in dev.typr.foundationskt
fun skip(clazz: Class<*>, methodName: String): ScanDirective
fun manual(clazz: Class<*>, methodName: String, variantName: String, result: Analyzable): ScanDirective
fun instance(obj: Any): ScanDirective
fun <T : Any> instance(obj: T, block: InstanceScope<T>.() -> Unit): ScanDirective
```

### ScanDirective (Scala)

```scala
// Methods on AnalyzableScanner object
def skip(clazz: Class[?], methodName: String): ScanDirective
def instance(obj: Any): ScanDirective
def instance[T](obj: T)(config: InstanceScope[T] => Unit): ScanDirective
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
