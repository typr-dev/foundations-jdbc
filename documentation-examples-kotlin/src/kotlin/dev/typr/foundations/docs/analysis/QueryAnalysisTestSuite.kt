package dev.typr.foundations.docs.analysis

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*
import javax.sql.DataSource

@Suppress("unused")
class QueryAnalysisTestSuite {
    data class User(val id: Int, val name: String, val email: String)
    data class Product(val id: Int, val name: String)

    private lateinit var testDataSource: DataSource

    private val userParser: RowParser<User> = RowParser.builder<User>()
        .field(PgTypes.int4, User::id)
        .field(PgTypes.text, User::name)
        .field(PgTypes.text, User::email)
        .build(::User)

    private val productParser: RowParser<Product> = RowParser.builder<Product>()
        .field(PgTypes.int4, Product::id)
        .field(PgTypes.text, Product::name)
        .build(::Product)

    //start
    fun allQueriesTypeCheck() {
        testDataSource.connection.use { conn ->
            // Collect all queries to check
            val queries: List<Operation.Query<*>> = listOf(
                Fragment.of("SELECT id, name, email FROM users WHERE id = ")
                    .value(PgTypes.int4, 1).query(userParser.all()),
                Fragment.of("SELECT id, name FROM products WHERE name LIKE ")
                    .value(PgTypes.text, "%widget%").query(productParser.all())
            )

            // Analyze each one
            val failures = queries.mapNotNull { query ->
                val analysis: QueryAnalysis = QueryAnalyzer.analyze(query, conn).single()
                if (!analysis.succeeded()) analysis.report() else null
            }

            // Report all failures at once
            if (failures.isNotEmpty()) {
                throw AssertionError("Query type check failed:\n\n${failures.joinToString("\n\n")}")
            }
        }
    }
    //stop
}
