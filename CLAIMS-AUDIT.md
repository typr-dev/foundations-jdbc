# Landing Page & Comparison Table Claims Audit

An independent review of every factual claim on the foundations-jdbc landing page (`site/src/pages/index.js`), verified against official documentation, source code, and GitHub issues.

---

## Comparison Table Claims

### Hibernate

| Row | Claim | Verdict | Evidence |
|-----|-------|---------|----------|
| Approach | "ORM with entity mapping" | **Accurate** | Canonical description. [hibernate.org/orm](https://hibernate.org/orm/) |
| Languages | "Java, Kotlin" | **Accurate** | No official Scala support. Kotlin requires `kotlin-noarg` plugin workarounds. [Baeldung: Kotlin + JPA](https://www.baeldung.com/kotlin/jpa) |
| DB portability | "HQL abstracts over DBs" (green) | **Accurate** | HQL uses Dialect system for DB-specific SQL generation. Edge cases exist for DB-specific functions. [Hibernate Portability Guide](https://docs.jboss.org/hibernate/orm/3.3/reference/en-US/html/portability.html) |
| Type model | "Java types only" (yellow) | **Accurate** | Standard Java types by default. Hibernate 6.2 added `@Struct` for composites and `BasicPluralType` for basic arrays, but ranges, enums, domains still need custom handling. [Hibernate 6.2 Composite Aggregates](https://in.relation.to/2023/02/13/hibernate-orm-62-composite-aggregates/) |
| Composites, arrays, ranges | "Via UserType" (yellow) | **Partially outdated** | Composites now have `@Struct` (6.2+), basic arrays have `BasicPluralType`. Ranges still need UserType/Hypersistence Utils. Yellow rating still appropriate. [Thorben Janssen: Mapping Arrays](https://thorben-janssen.com/mapping-arrays-with-hibernate/), [Vlad Mihalcea: Range types](https://vladmihalcea.com/map-postgresql-range-column-type-jpa-hibernate/) |
| Reflection | "Heavy" (red) | **Accurate** | Entity metadata via reflection, Byte Buddy proxy generation for lazy loading, bytecode enhancement, reflective field/method access. [Hibernate Proxies](https://thorben-janssen.com/hibernate-proxies/), [Bytecode Enhancement](https://in.relation.to/2019/07/30/bytecode-proxy/) |
| Query type checking | "No" (red) | **Defensible simplification** | By default no checking. `@NamedQuery` validated at startup. Opt-in `@CheckHQL` (6.3+) validates HQL at compile time against entity metamodel (not DB schema). Neither is default behavior. [hibernate/query-validator](https://github.com/hibernate/query-validator), [@CheckHQL Javadoc](https://docs.hibernate.org/stable/orm/javadocs/org/hibernate/annotations/processing/CheckHQL.html) |
| Nullable columns | "@Column(nullable)" (yellow) | **Accurate** | `@Column(nullable=false)` is a DDL hint only — does not enforce in Java code. Field type is still `String`, not `Optional<String>`. [Baeldung: @NotNull vs @Column(nullable)](https://www.baeldung.com/hibernate-notnull-vs-nullable) |
| Code generation | "Not supported" (red) | **INACCURATE** | Hibernate Tools `hbm2java` generates entity classes from DB schema. Official project, actively maintained. Generates entity scaffolding, not query helpers. [Hibernate Tools](https://hibernate.org/tools/), [GitHub](https://github.com/hibernate/hibernate-tools) |

### JDBI

| Row | Claim | Verdict | Evidence |
|-----|-------|---------|----------|
| Approach | "SQL + annotations" | **Partially accurate** | JDBI has two APIs: fluent/core (no annotations) and SQL Object extension (annotation-driven). "SQL + annotations" only describes one half. [jdbi.org](https://jdbi.org/) |
| Languages | "Java, Kotlin" | **Accurate** | First-party `jdbi3-kotlin` module. No official Scala module (only archived third-party). [jdbi3-kotlin on Maven Central](https://mvnrepository.com/artifact/org.jdbi/jdbi3-kotlin) |
| DB portability | "Raw SQL (portable enough)" (green) | **Fair** | JDBI passes through raw SQL. Portability depends on the SQL you write. Consistent with JdbcTemplate rating. |
| Type model | "Basic + custom" (yellow) | **Accurate** | Standard JDBC types + java.time + Optional built-in. Extensible via ColumnMapper/ArgumentFactory. Postgres plugin adds hstore, UUID, inet, geometric types. [jdbi.org](https://jdbi.org/) |
| Composites, arrays, ranges | "Manual mapping" (yellow) | **Accurate** | PostgresPlugin has partial array support (primitive types only). Basic composite registration via `registerCustomType()` (3.7.0+). Zero range support. [Issue #589](https://github.com/jdbi/jdbi/issues/589), [Issue #602](https://github.com/jdbi/jdbi/issues/602) |
| Reflection | "Moderate" (yellow) | **Accurate** | SQL Object uses runtime proxies (CGLIB). BeanMapper, ConstructorMapper, FieldMapper all use reflection. Less than Hibernate (no entity proxying/bytecode enhancement). [Issue #341](https://github.com/jdbi/jdbi/issues/341) |
| Query type checking | "No" (red) | **Accurate** | No query validation against schema at any stage. SQL strings are opaque. |
| Nullable columns | "Manual null checks" (red) | **Accurate for Java, slightly harsh overall** | Java: no type-level enforcement. `Optional<T>` is opt-in and has had bugs ([Issue #946](https://github.com/jdbi/jdbi/issues/946), [Issue #2669](https://github.com/jdbi/jdbi/issues/2669)). Kotlin: `KotlinMapper` respects `T?`. |
| Code generation | "Not supported" (red) | **INACCURATE** | `jdbi3-generator` (annotation processor) generates SqlObject implementation classes at compile time. Not schema-to-code, but is code generation. [Libraries.io: jdbi3-generator](https://libraries.io/maven/org.jdbi:jdbi3-generator), [Issue #1575](https://github.com/jdbi/jdbi/issues/1575) |

### JdbcTemplate

| Row | Claim | Verdict | Evidence |
|-----|-------|---------|----------|
| Approach | "SQL + RowMapper" | **Accurate** | Core paradigm: raw SQL string + `RowMapper<T>` functional interface. [RowMapper Javadoc](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/jdbc/core/RowMapper.html) |
| Languages | "Java, Kotlin" | **Accurate** | Spring officially supports Java and Kotlin. `spring-scala` was archived April 2022. [Spring JDBC docs](https://docs.spring.io/spring-framework/reference/data-access/jdbc/core.html) |
| DB portability | "Raw SQL (portable enough)" (green) | **Fair** | No SQL abstraction layer. Portability depends on the SQL written. Consistent with JDBI rating. |
| Type model | "Basic Java types" (red) | **Accurate** | Only standard JDBC `getXxx()` types. No built-in awareness of PostgreSQL arrays, composites, ranges, hstore, JSON, geometric types, etc. [BeanPropertyRowMapper Javadoc](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/jdbc/core/BeanPropertyRowMapper.html) |
| Composites, arrays, ranges | "Raw JDBC only" (red) | **Accurate** | Zero abstraction for any of these. Spring issue [#17771](https://github.com/spring-projects/spring-framework/issues/17771) (array support request) was closed without implementation. Must use `connection.createArrayOf()` directly. |
| Reflection | "None (manual mapper)" (green) | **Technically correct due to qualifier** | Manual `RowMapper` = no reflection. But `BeanPropertyRowMapper`, `DataClassRowMapper`, and `JdbcClient`'s `SimplePropertyRowMapper` all use heavy reflection. The "(manual mapper)" qualifier makes the claim true but presents the best-case scenario. [BeanPropertyRowMapper source](https://github.com/spring-projects/spring-framework/blob/main/spring-jdbc/src/main/java/org/springframework/jdbc/core/BeanPropertyRowMapper.java) |
| Query type checking | "No" (red) | **Accurate** | Raw SQL strings with no validation at any stage. |
| Nullable columns | "Manual null checks" (red) | **Accurate** | `rs.getInt()` returns 0 for NULL; must call `wasNull()`. No type-level distinction between nullable/non-nullable. `BeanPropertyRowMapper` throws on null-to-primitive mismatch or silently defaults. |
| Code generation | "Not supported" (red) | **Accurate** | No built-in code generation. `spring-data-jdbc-codegen` is a third-party community project for a different framework (Spring Data JDBC). |

### Exposed

| Row | Claim | Verdict | Evidence |
|-----|-------|---------|----------|
| Approach | "Kotlin DSL" | **Accurate** | Official tagline: "Kotlin SQL Framework." DSL is the primary API. Also has a DAO layer. [GitHub: JetBrains/Exposed](https://github.com/JetBrains/Exposed) |
| Languages | "Kotlin only" | **Accurate** | All APIs rely on Kotlin-specific features (extension functions, infix, operator overloading). No Java/Scala examples or support. [Exposed docs](https://www.jetbrains.com/help/exposed/about.html) |
| DB portability | "DSL is mostly portable" (green) | **Accurate** | Dialect system for H2, MariaDB, MySQL, Oracle, PostgreSQL, SQL Server, SQLite. Standard CRUD is portable; advanced features vary. [Exposed README](https://github.com/JetBrains/Exposed/blob/main/README.md) |
| Type model | "Kotlin types + custom" (yellow) | **Accurate** | Built-in `integer()`, `varchar()`, `text()`, `bool()`, etc. Extensible via custom `ColumnType`. [Data Types docs](https://www.jetbrains.com/help/exposed/data-types.html), [Custom Data Types docs](https://www.jetbrains.com/help/exposed/custom-data-types.html) |
| Composites, arrays, ranges | "Custom column types" (yellow) | **Partially outdated** | Arrays now have built-in `ArrayColumnType` support ([PR #1986](https://github.com/JetBrains/Exposed/pull/1986)). Ranges ([Issue #1298](https://github.com/JetBrains/Exposed/issues/1298)) and composites still require custom ColumnType. Yellow rating still appropriate overall. |
| Reflection | "DAO layer" (yellow) | **Accurate** | DAO `Entity` uses reflection for constructor discovery. DSL layer is reflection-free. [Entity Definition docs](https://www.jetbrains.com/help/exposed/dao-entity-definition.html): "reflection is used to determine the primary constructor" |
| Query type checking | "DSL only (compile)" (yellow) | **Accurate** | DSL provides compile-time type safety via `Column<T>` and `Expression<T>`. Raw SQL via `exec()` has no checking. [Exposed docs](https://www.jetbrains.com/help/exposed/working-with-sql-strings.html) |
| Nullable columns | "T? in Kotlin" (green) | **Accurate** | `.nullable()` transforms `Column<T>` to `Column<T?>`. Compile-time enforcement. [Working with Tables docs](https://www.jetbrains.com/help/exposed/working-with-tables.html) |
| Code generation | "Not supported" (red) | **INACCURATE** | Official `exposed-gradle-plugin` generates table definitions from DB schema. Low-profile (43 stars, not listed in main modules docs) but is an official JetBrains project. [exposed-gradle-plugin](https://github.com/JetBrains/exposed-intellij-plugin/tree/master/exposed-gradle-plugin) |

---

## "Code Generation" Row — Recommendation

The "Not supported" claim is wrong for Hibernate, JDBI, and Exposed:

| Library | Reality | Nature of codegen |
|---------|---------|-------------------|
| Hibernate | Hibernate Tools `hbm2java` | DB schema → entity classes (official, actively maintained) |
| JDBI | `jdbi3-generator` | Annotated interfaces → implementation classes (annotation processor, not schema-driven) |
| Exposed | `exposed-gradle-plugin` | DB schema → table definitions (official JetBrains, low-profile) |
| JdbcTemplate | None | No codegen exists |

**Recommendation:** Remove the "Code generation" row entirely, or reword it to "Schema-driven code generation" and clarify what each tool generates. The JDBI generator is arguably not the same category (it generates implementation plumbing, not schema-derived types). Hibernate Tools and the Exposed plugin are genuine schema-to-code generators but produce entity scaffolding / table definitions, not typed query infrastructure.

---

## Query Analysis Section Claims

### "No other Java SQL library does this."

**Verdict: Accurate in substance.**

No other Java/JVM SQL library provides a test-time facility that takes a constructed query object (with its declared parameter types, column types, and nullability) and validates it against the actual live database schema, producing a detailed report of mismatches.

- **jOOQ** validates its generated DSL at compile time but cannot check hand-written SQL at compile time. `parseWithMetaLookups` provides partial runtime validation but is not enabled by default, has known bugs ([Issue #10514](https://github.com/jOOQ/jOOQ/issues/10514), [#11132](https://github.com/jOOQ/jOOQ/issues/11132), [#11846](https://github.com/jOOQ/jOOQ/issues/11846)), and is a parser enrichment setting — not a dedicated test-time analysis feature. A feature request for type-safe plain SQL queries ([Issue #11049](https://github.com/jOOQ/jOOQ/issues/11049)) was closed without implementation.
- **Hibernate** has `@CheckHQL` (compile-time, against entity metamodel — not DB schema) and `@NamedQuery` startup validation. Neither checks against the actual database.
- **JDBI, JdbcTemplate, Exposed** have no query validation of any kind.

### "jOOQ validates DSL at compile time but can't check hand-written SQL."

**Verdict: Accurate.** jOOQ's compile-time validation is limited to the generated DSL. Plain SQL strings (`DSL.field("...")`, `ctx.resultQuery("...")`) are not schema-validated. The `PlainSQLChecker` (Checker Framework) only detects *usage* of the plain SQL API, not the *content* of SQL strings. [jOOQ Checker Framework docs](https://www.jooq.org/doc/latest/manual/sql-building/checker-framework/)

### "Hibernate validates annotations at startup but not query correctness."

**Verdict: Partially accurate.** Entity mapping annotations are validated at `SessionFactory` construction. `@NamedQuery` queries *are* also validated at startup (syntax and entity/property names). Dynamic `createQuery()` strings are only validated at runtime. More precise wording: "Hibernate validates entity mappings at startup; named queries are checked at startup but dynamic queries are not." [Hibernate Introduction](https://docs.jboss.org/hibernate/orm/6.3/introduction/html_single/Hibernate_Introduction.html)

---

## Design Philosophy Section Claims

### "Zero reflection, zero bytecode generation, zero annotation processing"

**Verdict: Accurate.**

A thorough search of the `foundations-jdbc/src/java/` source (184 Java files) found:

- **No introspective reflection** — no `Class.forName`, `Method.invoke`, `Field.get/set`, `Constructor.newInstance`, or `getDeclared*()` calls. The only use of `java.lang.reflect` is `Array.newInstance()` for creating generic typed arrays (a standard Java generics pattern required because type erasure prevents `new T[]`).
- **Zero bytecode generation** — no ASM, ByteBuddy, cglib, or javassist.
- **Zero annotation processing** — build explicitly sets `-proc:none`. No custom annotations with RUNTIME retention.

---

## Other Landing Page Claims

### "Full roundtrip fidelity"

**Verdict: Verifiable from source.** The library models each database's type system individually (PgTypes, MariaTypes, DuckDbTypes, OracleTypes, SqlServerTypes, Db2Types) with read and write codecs for every type. The test suite (`foundations-jdbc-test`) contains roundtrip tests.

### "Works with GraalVM native-image out of the box"

**Verdict: Follows from no-reflection.** Since there is no reflective class/method introspection, no proxies, and no bytecode generation, the library does not require GraalVM reflection configuration. This is a logical consequence of the architecture.

### "Not an ORM — No entity manager, no session, no lazy loading"

**Verdict: Accurate.** The source code contains no entity manager, session, identity map, dirty tracking, lazy loading proxies, or object lifecycle management. Queries return plain values.

### "Java, Kotlin, Scala — Core library in Java. Kotlin gets nullable types natively. Scala gets Option types and string interpolation."

**Verdict: Accurate.** Core is Java 21. `foundations-jdbc-kotlin` provides Kotlin-idiomatic wrappers with nullable types. `foundations-jdbc-scala_3` provides Scala wrappers with `Option[T]` (via `.nullable` extension) and `sql"..."` string interpolator.

---

## Issues Found & Resolved

All material inaccuracies have been fixed in the comparison table:

| # | Issue | Resolution |
|---|-------|------------|
| 1 | Hibernate code generation: was "Not supported" | Changed to "Reverse engineering" (neutral) with footnote about Hibernate Tools |
| 2 | Exposed code generation: was "Not supported" | Changed to "Gradle plugin" (neutral) with footnote about official JetBrains plugin |
| 3 | Hibernate composites: was "Via UserType" | Changed to "Partial" (yellow) with footnote about @Struct (6.2+) and built-in arrays |
| 4 | Exposed composites: was "Custom column types" | Changed to "Partial" (yellow) with footnote about built-in arrays |
| 5 | Hibernate query checking: was "No" (red) | Changed to "Opt-in" (yellow) with footnote about @CheckHQL (validates against metamodel, not DB) |

### Accepted as-is (minor simplifications appropriate for a comparison table)

| # | Item | Rationale |
|---|------|-----------|
| 1 | JDBI approach: "SQL + annotations" | JDBI also has a fluent API, but "SQL + annotations" is the most recognizable description |
| 2 | JDBI code generation: "Not supported" | `jdbi3-generator` generates implementation classes from interfaces, not schema-driven codegen |
| 3 | JdbcTemplate reflection: "None (manual mapper)" | Technically correct due to qualifier; BeanPropertyRowMapper uses reflection but is opt-in |
| 4 | Hibernate query analysis callout | "@NamedQuery validated at startup" is a special case; the general claim about dynamic queries is accurate |
