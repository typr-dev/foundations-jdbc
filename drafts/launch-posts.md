# Launch Post Drafts

## Hacker News — Show HN

**Title:** Show HN: Foundations JDBC – Type-safe database access for Java, Kotlin, and Scala

**URL:** https://github.com/typr-dev/foundations-jdbc

**Comment (post immediately after submitting):**

I've been building this for a while and finally shipped RC2.

The core idea: every database type should be a real typed value in your code. Not Object, not getInt() with a column index. PostgreSQL arrays, ranges, composite types, enums, domains. DuckDB STRUCT, LIST, MAP. Oracle OBJECT types. MariaDB unsigned integers. SQL Server GEOGRAPHY. All first-class, with full roundtrip fidelity across 6 databases.

The feature I'm most proud of is Query Analysis — it verifies your SQL against a real database at test time. Column types, parameter types, nullability, column counts. One test covers every query in your codebase:

```java
@Test
void verifyAllQueries() {
    var analyzables = AnalyzableScanner.scan("com.myapp.repository");
    QueryChecker.create(transactor).checkAll(analyzables);
}
```

Rename a column in your schema? Tests fail, not production. Forget .opt() on a nullable column? The report tells you exactly which query and column.

jOOQ validates its DSL at compile time but can't check hand-written SQL. Hibernate validates annotations but not query correctness. This validates your actual SQL against your actual database.

Other things that might interest HN:

- Zero reflection, zero annotations, zero bytecode generation. Works with GraalVM native-image out of the box.
- Fragments and row codecs are immutable values. Queries compose like functions.
- Performance sits right next to raw JDBC (benchmarks in the docs).
- Core is Java 21. Kotlin gets nullable types + sql {} interpolation. Scala gets Option + sql"" interpolation.

MIT licensed. Docs at https://foundations.typr.dev

Happy to answer any questions about the design decisions.

---

## r/java

**Title:** Foundations JDBC: Type-safe database access with test-time query verification — no reflection, no annotations, no code generation

**Body:**

I've been working on a JDBC library that takes a different approach from the usual ORM vs raw JDBC tradeoff. The idea is simple: your database has a rich type system, so your library should too.

**What it does:**

Every database type is modeled exactly — PostgreSQL arrays, ranges, composite types, enums, domains. DuckDB STRUCT, LIST, MAP. Oracle OBJECT. MariaDB unsigned types. SQL Server GEOGRAPHY. Not mapped to a handful of Java primitives, but to real typed values with full roundtrip fidelity. 6 databases supported.

**The headline feature — Query Analysis:**

One test verifies every query in your codebase against a real database:

```java
@Test
void verifyAllQueries() {
    var analyzables = AnalyzableScanner.scan("com.myapp.repository");
    QueryChecker.create(transactor).checkAll(analyzables);
}
```

It checks column types, parameter types, nullability, and column counts. Rename a column? Tests fail. Forget `.opt()` on a nullable column? The error report tells you exactly where.

**What I think Java developers will care about:**

- Zero reflection, zero annotations, zero bytecode generation, zero annotation processing. GraalVM native-image just works.
- `RowCodec` maps columns to records. Named codecs generate INSERT/SELECT column lists for you.
- `Fragment` is an immutable SQL building block with typed, bound parameters. Compose them like functions.
- `Operation<T>` is a lazy, composable database action. Combine, chain, find-or-create — all type-safe.
- Templates separate SQL structure from parameter values — define the shape once, fill it many times.
- Performance: measured overhead vs raw JDBC, Hibernate, JDBI, JdbcTemplate. Foundations sits right next to raw JDBC.
- Spring Boot integration with auto-configured Transactor and @Transactional support.
- Java 21, virtual threads work naturally.

**Quick start** (DuckDB in-memory, no server needed):

```java
var tx = SingleConnectionDataSource.create(DuckDbConfig.inMemory().build()).transactor();
int answer = Fragment.of("SELECT 42").queryExactlyOne(DuckDbTypes.integer).transact(tx);
```

MIT licensed, open source: https://github.com/typr-dev/foundations-jdbc
Docs: https://foundations.typr.dev
Blog post with full overview: https://foundations.typr.dev/blog/introducing-foundations-jdbc

Would love feedback from the community, especially on the API design. This is RC2 — the core is stable but there may be rough edges.

---

## r/PostgreSQL

**Title:** Foundations JDBC: A Java library that actually models PostgreSQL's type system — arrays, ranges, composites, enums, domains, JSON, geometric types — with test-time SQL verification

**Body:**

Most JDBC libraries treat PostgreSQL like it's MySQL with extra syntax. They map everything to Java primitives and make you fight the driver for anything interesting.

I built a library where PostgreSQL's type system is first-class:

- **Arrays**: `int4[]`, `text[]`, `uuid[]` — pass and receive as typed Java arrays, no `createArrayOf()` gymnastics
- **Ranges**: `int4range`, `tstzrange`, `numrange` — real `Range<T>` types, not strings
- **Composite types**: your `CREATE TYPE dimensions AS (...)` becomes a Java record with typed fields via `PgStruct`
- **Enums**: PostgreSQL enums map to Java enums
- **Domains**: modeled as the underlying type
- **JSON/JSONB**: typed codecs, your RowCodec doubles as a JSON codec for `json_agg()` aggregation
- **Geometric types**: `point`, `polygon`, `circle`, `line`, `box`
- **Network types**: `inet`, `cidr`, `macaddr`
- **Text search**: `tsvector`, `tsquery`
- **Other**: `money`, `record`, `hstore`, `uuid`

**Query Analysis** verifies your SQL against the actual PostgreSQL schema at test time — column types, parameter types, nullability. Rename a column and your tests catch it:

```
✗ Column 3 'created_at': type mismatch
  │ Declared: int4 (JDBC: INTEGER)
  │ Returned: timestamptz (JDBC: TIMESTAMP_WITH_TIMEZONE)
  └ The declared type cannot read from TIMESTAMP_WITH_TIMEZONE
```

Also supports stored procedures/functions (parameter counts, types, modes verified against `pg_proc`), and PostgreSQL COPY protocol for bulk loading via `StreamingInsert`.

```java
PgType<BigDecimal> numeric = PgTypes.numeric;
PgType<Range<Instant>> tstzrange = PgTypes.tstzrange;
PgType<String[]> textArray = PgTypes.textArray;
```

Not an ORM. You write SQL, you get typed results.

Java 21, also has Kotlin and Scala wrappers. MIT licensed.

GitHub: https://github.com/typr-dev/foundations-jdbc
PostgreSQL type docs: https://foundations.typr.dev/docs/postgresql

---

## r/Kotlin

**Title:** Foundations JDBC: Type-safe database access for Kotlin — nullable types, sql {} interpolation, composable queries, 6 databases

**Body:**

I built a JDBC library with a dedicated Kotlin wrapper that uses Kotlin's strengths rather than fighting them.

**Kotlin-native nullable types:**

`.opt()` on a database type returns `DbType<T?>` — Kotlin nullable, not `Optional<T>`. The type system enforces nullability at compile time.

**sql {} string interpolation:**

```kotlin
fun byName(name: String): Fragment =
    sql { "name LIKE ${PgTypes.text(name)}" }

fun cheaperThan(max: BigDecimal): Fragment =
    sql { "price < ${PgTypes.numeric(max)}" }

val products = sql { "SELECT * FROM products ${Fragment.whereAnd(filters)}" }
    .query(productCodec.all())
    .transact(tx)
```

The `${}` syntax creates prepared statement parameters — never concatenated. Fragment references splice SQL directly. The library knows the difference.

**Quick start** (DuckDB in-memory, zero setup):

```kotlin
val tx = SingleConnectionDataSource.create(DuckDbConfig.inMemory().build()).transactor()
val answer: Int = sql { "SELECT 42" }
    .queryExactlyOne(DuckDbTypes.integer)
    .transact(tx)
```

**What Kotlin developers get:**

- `T?` instead of `Optional<T>` throughout — nullable columns, left joins, ifEmpty
- `Pair<A, B>` instead of `And<A, B>` for joins and combine
- `sql { }` interpolation with ThreadLocal-based context — safe for coroutines
- Dedicated wrapper classes (not typealiases) — Fragment, Operation, Transactor
- Query Analysis: one test verifies every query in your codebase against the real database schema
- 6 databases: PostgreSQL, DuckDB, MariaDB, Oracle, SQL Server, DB2 — each with full type fidelity
- Zero reflection, GraalVM native-image compatible
- Spring Boot integration

MIT licensed, open source.

GitHub: https://github.com/typr-dev/foundations-jdbc
Kotlin interpolation docs: https://foundations.typr.dev/docs/kotlin-interpolation
Full docs: https://foundations.typr.dev

---

## r/scala

**Title:** Foundations JDBC: Type-safe database access for Scala 3 — Option types, sql"" interpolation, composable operations, 6 databases

**Body:**

A JDBC library with a Scala 3 wrapper that respects Scala idioms.

**Scala-native types:**

- `Option[T]` instead of `Optional<T>` for nullable columns
- Tuples instead of `And<A, B>` for joins and combine
- `sql""` string interpolation

**sql"" interpolation:**

```scala
def byName(name: String): Fragment =
  sql"name LIKE ${PgTypes.text.apply(name)}"

val products = sql"SELECT * FROM products ${Fragment.whereAnd(filters)}"
  .query(productCodec.all())
  .transact(tx)
```

**Quick start:**

```scala
val tx = SimpleDataSource.create(DuckDbConfig.inMemory().build()).transactor()
val answer: Int = sql"SELECT 42"
  .queryExactlyOne(DuckDbTypes.integer)
  .transact(tx)
```

**What Scala developers get:**

- `Option[T]` throughout — no Java Optional wrapping
- Tuples for joins and combine — `(A, B)` not `And[A, B]`
- `andThen` instead of `then` (reserved word)
- `combine`/`combineWith` for composing independent operations
- Sealed types, immutable values, composable operations — functional programming without the ceremony
- Query Analysis: one test verifies all SQL against the real database schema at test time
- 6 databases with full type fidelity: PostgreSQL (arrays, ranges, composites, enums), DuckDB (STRUCT, LIST, MAP), Oracle (OBJECT, NESTED TABLE), MariaDB, SQL Server, DB2
- Zero reflection, no macros

If you've used doobie or skunk, the mental model is similar — SQL as values, typed codecs, composable operations — but on top of JDBC, so you get all 6 databases and blocking I/O that works naturally with virtual threads.

MIT licensed.

GitHub: https://github.com/typr-dev/foundations-jdbc
Docs: https://foundations.typr.dev

---

## r/programming

**Title:** Foundations JDBC: Type-safe database access with test-time SQL verification for Java, Kotlin, and Scala

**Body:**

I built a JDBC library around two ideas that existing libraries don't address:

**1. Your database has a rich type system — your library should too.**

PostgreSQL has arrays, ranges, composite types, enums, domains, geometric types, network types, JSON. DuckDB has STRUCT, LIST, MAP, UNION. Oracle has OBJECT and NESTED TABLE. MariaDB has unsigned integers and SET.

Every other JDBC library maps these to a handful of Java primitives or makes you fight raw JDBC to use them. Foundations JDBC models each database's full type system with dedicated type classes (`PgTypes`, `DuckDbTypes`, `OracleTypes`, etc.) and full roundtrip fidelity.

**2. Queries should be verified against the real database, not just the compiler.**

Query Analysis prepares your SQL against a real database at test time and verifies that column types, parameter types, nullability, and column counts all match your code:

```java
@Test
void verifyAllQueries() {
    var analyzables = AnalyzableScanner.scan("com.myapp.repository");
    QueryChecker.create(transactor).checkAll(analyzables);
}
```

Rename a column in your schema? Tests fail, not production. Forget to handle a nullable column? The error report tells you exactly which query and column. Templates with optional predicates produce 2^N query shapes — all verified automatically.

No other Java SQL library does this. jOOQ validates its DSL at compile time but can't check hand-written SQL. Hibernate validates annotations but not query correctness.

**The rest of the design:**

- Zero reflection, zero annotations, zero bytecode generation. GraalVM native-image just works.
- Fragments are immutable SQL building blocks with typed, bound parameters. Compose them like functions.
- Row codecs map columns to domain objects. They double as JSON codecs for json_agg() aggregation.
- Operations are lazy, composable database actions — combine, chain, find-or-create.
- Java 21 core. Kotlin wrapper with T? and sql {} interpolation. Scala 3 wrapper with Option and sql"" interpolation.
- Performance right next to raw JDBC (benchmarks in docs).
- 6 databases: PostgreSQL, DuckDB, MariaDB, Oracle, SQL Server, DB2.

MIT licensed: https://github.com/typr-dev/foundations-jdbc
Docs: https://foundations.typr.dev
Blog post: https://foundations.typr.dev/blog/introducing-foundations-jdbc

---

## r/duckdb

**Title:** Foundations JDBC: A Java/Kotlin/Scala library with first-class DuckDB type support — STRUCT, LIST, MAP, UNION, ENUM, HUGEINT, and more

**Body:**

Most JDBC libraries barely handle DuckDB beyond basic types. I built one where DuckDB's type system is fully modeled:

- **STRUCT**: typed fields, not shapeless Objects
- **LIST**: `LIST<VARCHAR>`, `LIST<INTEGER>`, nested lists — real typed Java lists
- **MAP**: `MAP<VARCHAR, INTEGER>` as typed Java maps
- **UNION / ENUM**: modeled as-is
- **Unsigned integers**: UTINYINT, USMALLINT, UINTEGER, UBIGINT, UHUGEINT — all with dedicated types that don't overflow
- **HUGEINT**: full 128-bit integer support
- **INTERVAL, TIMESTAMP_NS, BITSTRING, JSON, UUID, BLOB, DECIMAL**

**Zero-setup quickstart** (in-memory, no server):

```java
var tx = SingleConnectionDataSource.create(DuckDbConfig.inMemory().build()).transactor();
int answer = Fragment.of("SELECT 42").queryExactlyOne(DuckDbTypes.integer).transact(tx);
```

Or in Kotlin:

```kotlin
val tx = SingleConnectionDataSource.create(DuckDbConfig.inMemory().build()).transactor()
val answer: Int = sql { "SELECT 42" }
    .queryExactlyOne(DuckDbTypes.integer)
    .transact(tx)
```

**Query Analysis** verifies your SQL against the real DuckDB schema at test time — column types, nullability, parameter counts. One test covers every query in your codebase.

**Array example:**

```java
// Pass arrays directly — no createArrayOf(), no type name strings
Fragment.of("INSERT INTO posts (tags) VALUES (")
    .value(DuckDbTypes.varcharArray, new String[]{"java", "duckdb"})
    .append(")").update().transact(tx);
```

**JSON codecs** work uniformly — your RowCodec doubles as a JSON codec. Aggregate child rows with `json_group_array()` and parse with the same types.

Also supports PostgreSQL, MariaDB, Oracle, SQL Server, and DB2 — each with their own full type model.

Zero reflection, GraalVM native-image compatible. MIT licensed.

GitHub: https://github.com/typr-dev/foundations-jdbc
DuckDB type docs: https://foundations.typr.dev/docs/duckdb

---

## r/jvm

**Title:** Foundations JDBC: Type-safe database access across 6 databases — zero reflection, composable queries, test-time SQL verification

**Body:**

A JDBC library that takes the JVM's type system seriously.

**The problem:** JDBC libraries either abstract away database types (ORM) or leave you with raw getObject/getInt (low-level). Neither gives you the actual type system of your database.

**The approach:** Dedicated type classes for each database. `PgTypes.int4range` is a `PgType<Range<Integer>>`. `DuckDbTypes.struct(...)` builds a typed STRUCT codec. `OracleTypes.number(38,18)` is `OracleType<BigDecimal>`. Every type reads and writes with full roundtrip fidelity.

**Key features for JVM developers:**

- **Zero reflection, zero annotations, zero bytecode generation.** No runtime proxies, no annotation processors. GraalVM native-image works out of the box — no additional configuration.
- **Query Analysis:** one test verifies every query against the real database schema. Column types, nullability, parameter counts. The only Java SQL library that does this.
- **Virtual threads:** blocking API works naturally with JDK 21+ virtual threads. No suspend wrappers, no reactive adapters.
- **Composable:** Fragments, row codecs, and operations are immutable values. Row codecs compose for joins — inner join gives you Tuple2, left join wraps the right side in Optional.
- **Performance:** measured against raw JDBC, Hibernate, JDBI, JdbcTemplate. Sits right next to raw JDBC at scale.

**Language support:**
- Java 21 — Optional<T>, builder pattern, Tuple
- Kotlin — T?, sql {} interpolation, Pair
- Scala 3 — Option[T], sql"" interpolation, tuples

**6 databases:** PostgreSQL, DuckDB, MariaDB/MySQL, Oracle, SQL Server, DB2.

MIT licensed: https://github.com/typr-dev/foundations-jdbc
Docs: https://foundations.typr.dev
Benchmarks: https://foundations.typr.dev/docs/benchmarks

---

## r/database

**Title:** Foundations JDBC: A JVM library that actually models your database's type system — PostgreSQL arrays/ranges/composites, DuckDB STRUCT/LIST/MAP, Oracle OBJECT, and more

**Body:**

I got frustrated that every JDBC library treats databases as if they all have the same 10 types. PostgreSQL has arrays, ranges, composite types, enums, domains, geometric types, network types, text search. DuckDB has STRUCT, LIST, MAP, UNION. Oracle has OBJECT and NESTED TABLE. MariaDB has unsigned integers, SET, INET4/INET6.

So I built a library where each database's full type system is modeled exactly:

| Database | Types modeled |
|----------|--------------|
| PostgreSQL | int4[], text[], uuid[], int4range, tstzrange, numrange, jsonb, hstore, point, polygon, circle, inet, cidr, macaddr, tsvector, tsquery, composite types, enums, domains, money |
| DuckDB | LIST, MAP, STRUCT, UNION, ENUM, HUGEINT, UHUGEINT, unsigned integers, INTERVAL, TIMESTAMP_NS, JSON, UUID, BITSTRING |
| Oracle | OBJECT, NESTED TABLE, VARRAY, XMLTYPE, INTERVAL YM/DS, NUMBER(p,s), BINARY_FLOAT, CLOB, NCLOB, BLOB, RAW, ROWID |
| MariaDB | SET, ENUM, JSON, unsigned INT/BIGINT, GEOMETRY, POINT, POLYGON, INET4, INET6, YEAR, BIT(n) |
| SQL Server | GEOGRAPHY, GEOMETRY, HIERARCHYID, DATETIMEOFFSET, SQL_VARIANT, VECTOR, ROWVERSION, XML |
| DB2 | DECFLOAT, GRAPHIC, VARGRAPHIC, DBCLOB, XML, ROWID, TIMESTAMP(p) |

Every value round-trips correctly. Read a value from the database and write it back — you get the same value.

**The other feature I think database people will appreciate: Query Analysis.**

It verifies your SQL against the real database schema at test time. Column types, parameter types, nullability — all checked. The report shows exactly what went wrong:

```
✗ Column 3 'created_at': type mismatch
  │ Declared: int4 (JDBC: INTEGER)
  │ Returned: timestamptz (JDBC: TIMESTAMP_WITH_TIMEZONE)
  └ The declared type cannot read from TIMESTAMP_WITH_TIMEZONE
```

Stored procedures are verified too — parameter counts, types, and modes checked against the database catalog.

Java 21 with Kotlin and Scala wrappers. Not an ORM — you write SQL, you get typed results. MIT licensed.

GitHub: https://github.com/typr-dev/foundations-jdbc
Docs: https://foundations.typr.dev

---

## r/graalvm

**Title:** Foundations JDBC: A JDBC library with zero reflection, zero annotations, zero bytecode generation — native-image just works

**Body:**

If you've tried to use Hibernate or other JDBC libraries with GraalVM native-image, you know the pain — reflection configuration files, runtime proxies, annotation processing that doesn't survive AOT compilation.

Foundations JDBC takes a different approach: there is no reflection. At all.

- No runtime proxies
- No bytecode generation
- No annotation processing
- No `Class.forName()` tricks
- No reflection configuration needed for native-image

Row codecs are built with explicit builder calls. Types are explicit objects, not annotations. Everything is wired at compile time through plain Java constructors and method calls.

```java
record Product(int id, String name, BigDecimal price) {}

static final RowCodecNamed<Product> codec =
    RowCodec.<Product>namedBuilder()
        .field("id", PgTypes.int4, Product::id)
        .field("name", PgTypes.text, Product::name)
        .field("price", PgTypes.numeric, Product::price)
        .build(Product::new);
```

That's it. No `@Column`, no `@Entity`, no reflection metadata. `Product::new` is a method reference, not reflective access.

**Beyond native-image compatibility, the library also provides:**

- Full type fidelity across 6 databases (PostgreSQL, DuckDB, MariaDB, Oracle, SQL Server, DB2) — every database type modeled exactly
- Query Analysis: test-time verification of all SQL against the real database schema
- Composable fragments and operations — immutable values, functional style
- Java 21 core with Kotlin and Scala wrappers
- Performance right next to raw JDBC

MIT licensed: https://github.com/typr-dev/foundations-jdbc
Docs: https://foundations.typr.dev

---

## X/Twitter — Thread

**Post 1 (hook):**

Introducing Foundations JDBC — type-safe database access for Java, Kotlin, and Scala.

Every type your database has, as a real typed value. PostgreSQL arrays, ranges, composites. DuckDB STRUCT. Oracle OBJECT. 6 databases, full roundtrip fidelity.

Open source, MIT licensed.

https://github.com/typr-dev/foundations-jdbc

#java #opensource

**Post 2 (the differentiator):**

The feature no other Java SQL library has: Query Analysis.

One test verifies every query in your codebase against the real database. Column types, nullability, parameter counts — all checked.

Rename a column in your schema? Tests fail, not production.

**Post 3 (code):**

Zero reflection. Zero annotations. Zero bytecode generation. GraalVM native-image just works.

Fragments and row codecs are immutable values. Queries compose like functions.

Performance sits right next to raw JDBC.

**Post 4 (language support):**

Java 21 core. Kotlin gets T? and sql {} interpolation. Scala 3 gets Option and sql"" interpolation.

Same concepts, same capabilities. Each language's idioms respected.

#kotlin #scala

**Post 5 (CTA):**

Docs: https://foundations.typr.dev
GitHub: https://github.com/typr-dev/foundations-jdbc
Blog post: https://foundations.typr.dev/blog/introducing-foundations-jdbc

RC2 is out. Would love feedback.

---

## X/Twitter — Standalone posts (alternative angles)

---

## Bluesky — Thread

**Post 1 (hook):**

Introducing Foundations JDBC — type-safe database access for Java, Kotlin, and Scala.

Every type your database has, as a real typed value. PostgreSQL arrays, ranges, composites. DuckDB STRUCT. Oracle OBJECT. 6 databases, full roundtrip fidelity.

Open source, MIT licensed.
github.com/typr-dev/foundations-jdbc

**Post 2 (differentiator):**

The feature no other Java SQL library has: Query Analysis.

One test verifies every query in your codebase against the real database. Column types, nullability, parameter counts — all checked at test time.

Rename a column? Tests fail, not production.

**Post 3 (design):**

Zero reflection. Zero annotations. Zero bytecode generation. GraalVM native-image just works.

Queries compose like functions. Performance right next to raw JDBC.

**Post 4 (languages):**

Java 21 core. Kotlin gets T? and sql {} interpolation. Scala 3 gets Option and sql"" interpolation.

Same concepts. Each language's idioms respected.

Docs: foundations.typr.dev

**Bluesky — Standalone posts (alternative angles)**

**Angle 1:**

Your DuckDB STRUCT becomes a shapeless Object. Your PostgreSQL int4range becomes a string you parse yourself.

The database has real types — your library just ignores them.

We fixed this. 6 databases, every type modeled exactly.

github.com/typr-dev/foundations-jdbc

**Angle 2:**

What if one test could verify every SQL query in your codebase against the real database schema?

Column types. Nullability. Parameter counts. All checked at test time.

That's Query Analysis in Foundations JDBC.

foundations.typr.dev/docs/query-analysis

---

## X/Twitter — Standalone posts (alternative angles)

**Angle 1 — Problem/solution:**

Your DuckDB STRUCT becomes a shapeless Object. Your PostgreSQL int4range becomes a string you parse yourself.

The database has real types — your library just ignores them.

We fixed this. 6 databases, every type modeled exactly.

https://github.com/typr-dev/foundations-jdbc

#java #postgresql #duckdb #opensource

**Angle 2 — Query Analysis:**

What if one test could verify every SQL query in your codebase against the real database schema?

Column types. Nullability. Parameter counts. All checked at test time.

That's Query Analysis in Foundations JDBC.

https://foundations.typr.dev/docs/query-analysis

#java #jdbc

**Angle 3 — Comparison:**

jOOQ validates DSL at compile time but can't check hand-written SQL.

Hibernate validates annotations but not query correctness.

Foundations JDBC validates your actual queries against your actual database.

https://github.com/typr-dev/foundations-jdbc

#java #opensource
