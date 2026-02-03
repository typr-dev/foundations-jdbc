package dev.typr.foundations.docs.analysis

import dev.typr.foundations.Fragment
import dev.typr.foundations.Operation
import dev.typr.foundations.PgTypes
import dev.typr.foundations.kotlin.RowParser
import dev.typr.foundations.kotlin.query
import dev.typr.foundations.analysis.QueryAnalysis
import dev.typr.foundations.analysis.QueryAnalyzer
import javax.sql.DataSource
import java.sql.SQLException

@Suppress("unused")
class QueryAnalysisTestSuite {
    data class User(val id: Int, val name: String, val email: String)
    data class Product(val id: Int, val name: String)

    private val testDataSource: DataSource? = null // placeholder

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
    @Throws(SQLException::class)
    fun allQueriesTypeCheck() {
        testDataSource!!.connection.use { conn ->
            // Collect all queries to check
            val queries: List<Operation.Query<*>> = listOf(
                Fragment.interpolate("SELECT id, name, email FROM users WHERE id = ")
                    .param(PgTypes.int4, 1).done().query(userParser.all()),
                Fragment.interpolate("SELECT id, name FROM products WHERE name LIKE ")
                    .param(PgTypes.text, "%widget%").done().query(productParser.all())
            )

            // Analyze each one
            val failures = queries.mapNotNull { query ->
                val analysis: QueryAnalysis = QueryAnalyzer.analyze(query, conn)
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
