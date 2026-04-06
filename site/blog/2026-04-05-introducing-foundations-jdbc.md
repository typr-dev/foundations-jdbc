---
title: "Introducing Foundations JDBC: Type-Safe Database Access for the JVM"
description: "Every type your database has, as a real typed value. Queries that compose. No annotations, no reflection, no surprises."
slug: introducing-foundations-jdbc
authors:
  - name: Øyvind Raddum Berg
tags: [release, announcement]
---

What if your JDBC library actually knew what your database types were?

Not `Object`. Not `getInt()` with a column index and a prayer. Real types — `int4range`, `jsonb`, `STRUCT`, `LIST<MAP<VARCHAR, INTEGER>>`, `GEOGRAPHY` — modeled exactly as the database defines them, with full roundtrip fidelity.

That's what we built. **Foundations JDBC** is an open-source, MIT-licensed JDBC library for Java, Kotlin, and Scala. It gives you composable SQL fragments, typed row codecs, and test-time query verification — across PostgreSQL, MariaDB, DuckDB, Oracle, SQL Server, and DB2.

Today we're releasing **1.0.0-RC2**.

<!-- truncate -->

## The Problem

Every JVM developer who writes SQL has hit the same walls.

**Queries are unchecked strings.** Rename a column in your schema and nothing fails until production. No existing library catches this at test time.

**Nullability is invisible.** A nullable column and a non-nullable column have the same Java type. You find out which is which when `NullPointerException` fires at 2 AM.

**Type fidelity is lost.** Your DuckDB `STRUCT` becomes a shapeless `Object`. Your PostgreSQL `int4range` becomes a string you have to parse yourself. The database has real types — your library just ignores them.

**Database-specific features are second-class.** PostgreSQL arrays, Oracle `MULTISET`, DuckDB `STRUCT`, MariaDB unsigned types — every interesting database feature requires escape hatches and manual JDBC gymnastics.

Foundations JDBC solves all four.

## Query Analysis: Find SQL Bugs at Test Time, Not 2 AM

This is the feature no other Java SQL library has, and the reason we built the rest.

[Query Analysis](/docs/query-analysis) prepares your SQL against a real database and verifies that parameter types, column types, nullability, and column counts all match your code. A single test verifies every query in your codebase:

```java
@Test
void verifyAllQueries() {
    var analyzables = AnalyzableScanner.scan("com.myapp.repository");
    QueryChecker.create(transactor).checkAll(analyzables);
}
```

`AnalyzableScanner` discovers every [`Operation`](/docs/operations), [`Template`](/docs/templates), and `RowTemplate` in the given package. Add a new query anywhere — it's automatically included next test run. No manual list maintenance.

When something fails, the report tells you exactly what went wrong:

```
✗ 2 error(s) found:

  1. Column 3 'created_at': type mismatch
     │ Declared: int4 (JDBC: INTEGER)
     │ Returned: timestamptz (JDBC: TIMESTAMP_WITH_TIMEZONE)
     └ The declared type cannot read from TIMESTAMP_WITH_TIMEZONE

  2. Column 4 'email': nullability mismatch
     │ The database says this column is nullable
     │ But the type text is not Optional
     └ Use .opt() to make the type nullable
```

jOOQ validates its DSL at compile time but can't check hand-written SQL. Hibernate validates annotations at startup but not query correctness. Foundations JDBC validates your actual queries against your actual database.

[Templates](/docs/templates) with `.optionally()` produce 2^N possible query shapes from N optional predicates — Query Analysis verifies every combination automatically. [Composed operations](/docs/composing-operations) are walked recursively, verifying every SQL statement in the tree. [Stored procedures](/docs/stored-procedures) are checked too — parameter counts, types, and modes all verified against the database catalog.

This is what makes the rest of the library worth building. Every feature below feeds into Query Analysis.

## A Quick Look

Here's the shortest path from zero to a running query. DuckDB runs in-memory — no database server needed:

```kotlin
import dev.typr.foundationskt.*
import dev.typr.foundationskt.connect.*

fun main() {
    val tx = SingleConnectionDataSource.create(
        DuckDbConfig.inMemory().build()
    ).transactor()

    val answer: Int = sql { "SELECT 42" }
        .queryExactlyOne(DuckDbTypes.integer)
        .transact(tx)

    println("Result: $answer") // Result: 42
}
```

`sql { }` is Kotlin's [type-safe string interpolation](/docs/kotlin-interpolation) for SQL. `DuckDbTypes.integer` tells the library exactly how to read the value. [`transact(tx)`](/docs/transactors) obtains a connection, runs in a transaction, commits on success, rolls back on error, and cleans up. The return type is `Int`, not `Object`, not `ResultSet`.

## Row Codecs: Define Once, Use Everywhere

A [`RowCodec`](/docs/row-codecs) maps database columns to your domain objects. Define it once — it works for reading queries, writing inserts, [streaming with COPY](/docs/streaming-inserts), [JSON round-trips](/docs/json), and [query analysis](/docs/query-analysis) verification:

```java
record Product(int id, String name, BigDecimal price, Optional<Instant> createdAt) {}

static final RowCodecNamed<Product> productCodec =
    RowCodec.<Product>namedBuilder()
        .field("id", PgTypes.int4, Product::id)
        .field("name", PgTypes.text, Product::name)
        .field("price", PgTypes.numeric, Product::price)
        .field("created_at", PgTypes.timestamptz.opt(), Product::createdAt)
        .build(Product::new);
```

`.opt()` on `timestamptz` declares a nullable column — the type becomes `Optional<Instant>`. Forget it on a nullable column and Query Analysis catches it. If the column isn't nullable, the type is `Instant` — no null check needed.

Named codecs generate SQL for you:

```java
// SELECT id, name, price, created_at
Fragment.of("SELECT ").append(productCodec.columnList()).append(" FROM product")

// INSERT INTO product (id, name, price, created_at) VALUES (?, ?, ?, ?)
Fragment.insertInto("product", productCodec)
```

Codecs [compose for joins](/docs/row-codecs#composing-codecs-for-joins) — inner joins give you `Tuple2<A, B>`, left joins wrap the right side in `Optional`. If all columns on the right are null, you get `Optional.empty()`. This isn't a convention — it's in the type.

## Six Databases, Full Type Fidelity

Each database has its own [typed hierarchy](/docs/database-types). Not a lowest-common-denominator abstraction — the full type system of each database, modeled exactly:

[**PostgreSQL**](/docs/postgresql) — arrays, ranges, JSON, geometric types, network types, text search, composite types, enums, domains.

[**DuckDB**](/docs/duckdb) — `LIST`, `MAP`, `STRUCT`, `UNION`, `ENUM`, unsigned integers, `HUGEINT`, `INTERVAL`, JSON, UUID.

[**Oracle**](/docs/oracle) — `OBJECT` types, `NESTED TABLE`, `VARRAY`, `XMLTYPE`, intervals, LOBs, `RAW`, `ROWID`.

[**MariaDB/MySQL**](/docs/mariadb) — `SET`, `ENUM`, JSON, unsigned integers, spatial types, `INET4`, `INET6`, `YEAR`.

[**SQL Server**](/docs/sqlserver) — `GEOGRAPHY`, `GEOMETRY`, `HIERARCHYID`, `DATETIMEOFFSET`, `SQL_VARIANT`, `VECTOR`, XML.

[**DB2**](/docs/db2) — `DECFLOAT`, graphic types, XML, `ROWID`, high-precision `TIMESTAMP(p)`.

PostgreSQL composite types become records with typed fields. DuckDB structs and maps work the same way. Oracle OBJECT types, too. Each database's unique capabilities are first-class citizens.

## Fragments: Composable SQL

A [`Fragment`](/docs/fragments) is an immutable piece of SQL with typed, bound parameters. You compose fragments like functions:

```kotlin
fun byName(name: String): Fragment =
    sql { "name LIKE ${SqlServerTypes.nvarchar(name)}" }

fun cheaperThan(max: BigDecimal): Fragment =
    sql { "price < ${SqlServerTypes.decimal(max)}" }

val filters = listOfNotNull(
    byName("%widget%"),
    maxPrice?.let { cheaperThan(it) }
)

val orders = sql { "SELECT * FROM orders ${Fragment.whereAnd(filters)}" }
    .query(orderCodec.all())
    .run(conn)
```

Parameters are always bound — never concatenated. The `${}` syntax creates prepared statement parameters. Fragment references like `Fragment.whereAnd()` splice SQL directly. The library knows the difference.

Java and Scala get the same composability through the builder API and Scala's `sql""` interpolator.

## Operations and Templates

Calling `.query()` or `.update()` on a fragment gives you an [`Operation<T>`](/docs/operations) — a database action that produces a typed result. Nothing runs until you say so:

```java
Operation<List<Product>> findAll =
    Fragment.of("SELECT ")
        .append(productCodec.columnList())
        .append(" FROM product ORDER BY name")
        .query(productCodec.all());

List<Product> products = findAll.transact(tx);
```

[Operations compose](/docs/composing-operations). Combine independent queries with `.combineWith()`. Chain dependent operations with `.then()`. Find-or-create with `Operation.ifEmpty()`. Every combinator produces a new `Operation` — still just a value, still composable, still analyzable.

[Templates](/docs/templates) separate SQL structure from parameter values — define the shape once, fill it many times:

```java
static final Template<String, Optional<User>> findByEmail =
    Fragment.of("SELECT id, name, email FROM users WHERE email = ")
        .param(PgTypes.text)
        .query(userCodec.maxOne());

Optional<User> alice = findByEmail.on("alice@example.com").transact(tx);
```

Templates support dynamic SQL through `.optionally()` and batch execution via `.onMany()`.

## Built-in JSON Codecs

Your `RowCodec` doubles as a [JSON codec](/docs/json) with zero extra code:

```java
DuckDbType<List<OrderLine>> linesType = DuckDbTypes.jsonArrayEncodedList(lineCodec);

// Aggregate child rows as JSON — one query instead of N+1
List<OrderLine> lines = Fragment.of("""
    SELECT json_group_array(json_array(product, qty, price))
    FROM order_lines WHERE customer_id = """)
    .value(DuckDbTypes.integer, customerId)
    .query(RowCodec.of(linesType).exactlyOne())
    .transact(tx);
```

Same types for result sets and JSON. No separate deserialization. Works with `json_agg()` (PostgreSQL), `JSON_ARRAYAGG()` (MariaDB, Oracle, DB2), `json_group_array()` (DuckDB), and `FOR JSON PATH` (SQL Server).

## Type-Safe Stored Procedures

Define a [procedure](/docs/stored-procedures) once — the builder tracks IN and OUT types statically:

```java
static final DbProcedure.Def1_2<Integer, String, String> getUser =
    DbProcedure.define("get_user_by_id")
        .input(PgTypes.int4)
        .out(PgTypes.text)
        .out(PgTypes.text)
        .build();

Tuple2<String, String> result = getUser.call(userId).transact(tx);
```

`Def1_2` encodes the shape: 1 input, 2 outputs. Wrong argument types or missing parameters are compile errors. Functions work too, using `SELECT` instead of `CALL`.

## Production Ready

Foundations JDBC is derived from code that has seen real production use, but it has been substantially rewritten and rearchitected into what you see today. Everything is thoroughly tested, but as a fresh release there may be rough edges — we'd rather ship and iterate than wait for perfection. It ships with everything you need:

- [**Connection Pooling**](/docs/connection-pooling) — HikariCP integration with typed config builders.
- [**Spring Boot**](/docs/spring-boot) — Auto-configured `Transactor` bean. `@Transactional` just works.
- [**Observability**](/docs/observability) — Query listeners, `.named()` comments visible in `pg_stat_activity`, interpolated SQL for debugging. [OpenTelemetry](/docs/opentelemetry) module with automatic spans and pool metrics.
- [**Virtual Threads**](/docs/virtual-threads) — Blocking API works naturally with JDK 21+ virtual threads. No suspend wrappers, no reactive adapters.
- **Streaming** — [Cursor-based reads](/docs/streaming-reads) for large result sets. PostgreSQL [COPY protocol](/docs/streaming-inserts) for bulk loading.
- [**Testing**](/docs/testing) — `testStrategy()` rolls back instead of committing. Tests run against real SQL without leaving data behind.

## Performance

We [measured mapping overhead](/docs/benchmarks) against other popular libraries. In-memory DuckDB, 15-column rows, pure framework overhead:

| Rows | Raw JDBC | Foundations | Hibernate (entity) | JDBI | JdbcTemplate |
|---:|---:|---:|---:|---:|---:|
| 1 | 289 us | 384 us | 775 us | 415 us | 297 us |
| 1,000 | 6.18 ms | 5.79 ms | 9.03 ms | 6.65 ms | 6.41 ms |
| 100,000 | 570 ms | 581 ms | 688 ms | 582 ms | 586 ms |

Foundations JDBC sits right next to raw JDBC. At scale, the difference vanishes into noise. Full type safety and query analysis at essentially zero cost.

## Java, Kotlin, Scala

The core library is Java 21. Kotlin and Scala get dedicated wrapper modules:

- **Java** — `Optional<T>` for nullable columns, builder pattern, `Tuple` for joins.
- **Kotlin** — `T?` for nullable columns, [`sql { }` interpolation](/docs/kotlin-interpolation), `Pair` for joins.
- **Scala 3** — `Option[T]` for nullable columns, `sql""` interpolation, tuples for joins.

Same concepts. Same capabilities. Each language's idioms respected.

## Get Started

Head to the [Getting Started guide](/docs/) for setup instructions and your first query. Source code on [GitHub](https://github.com/typr-dev/foundations-jdbc).

## What's Next

Two things are coming that will change how you use Foundations JDBC.

**World-class codegen with a SQL DSL.** Generate all the RowCodecs, type definitions, and repository scaffolding directly from your database schema. Write queries in a type-safe SQL DSL that composes like the language it's embedded in.

**A native PostgreSQL driver for the JVM.** We've been working on something that fundamentally changes what's possible with PostgreSQL on the JVM. It bypasses JDBC entirely, speaks the PostgreSQL wire protocol directly, and unlocks a class of optimizations that no connection pool or driver can offer today. The same Fragments, RowCodecs, and Operations you write today will run on it without changing a line of code.

Stay tuned.
