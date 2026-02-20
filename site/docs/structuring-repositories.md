---
title: Structuring Repositories
---

# Structuring Repositories

A common pattern is to define queries as private fields on a repository object, with public methods that bind parameters and execute them. This separates query definition (validated once at startup) from query execution:

```kotlin
object UserRepo {
    // Fixed query — use Sql { } (Kotlin) or Fragment.of() (Java)
    private val selectAll = Sql { "SELECT ${userCodec.columnList} FROM users ORDER BY name" }
        .query(userCodec.all())

    // Parameterized query — use the builder to create a Template
    private val selectByIdTemplate = Fragment.of("SELECT ")
        .append(userCodec.columnList).append(" FROM users WHERE id = ")
        .param(PgTypes.int4)
        .query(userCodec.maxOne())

    // Public methods bind parameters and return Operations or results
    fun findAll(conn: Connection): List<User> = selectAll.run(conn)
    fun findById(id: Int, conn: Connection): User? = selectByIdTemplate.on(id).run(conn)

    // Expose Operations for composition with .with(), .then(), etc.
    fun findAllOp(): Operation.Query<List<User>> = selectAll
    fun findByIdOp(id: Int): Operation.Query<User?> = selectByIdTemplate.on(id)

    // Collect all queries for batch analysis in tests
    fun analyzeQueries(conn: Connection): List<QueryAnalysis> = listOf(
        QueryAnalyzer.analyze(selectAll.named("UserRepo.selectAll"), conn),
        QueryAnalyzer.analyze(selectByIdTemplate, conn),
    ).flatten()
}
```

The `analyzeQueries` method lets you verify all queries against the database schema in a single test:

```kotlin
tx.transact { conn ->
    val analyses = UserRepo.analyzeQueries(conn) + OrderRepo.analyzeQueries(conn)
    for (a in analyses) {
        check(a.succeeded()) { a.reportColored() }
    }
}
```

For methods that callers use inside a `transact` block, accept a `Connection` parameter. For standalone operations, return an `Operation` that the caller runs with `.transact(tx)` or composes further. Offering both gives callers the most flexibility.
