package dev.typr.foundations.docs.analysis

import dev.typr.foundations.Fragment
import dev.typr.foundations.PgTypes
import dev.typr.foundations.kotlin.RowParser
import dev.typr.foundations.kotlin.query
import dev.typr.foundations.analysis.QueryAnalysis
import dev.typr.foundations.analysis.QueryAnalyzer
import java.sql.Connection
import java.sql.SQLException

@Suppress("unused")
class QueryAnalysisBasic {
    data class User(val id: Int, val name: String, val email: String)

    private val connection: Connection? = null // placeholder
    private val userId = 1

    private val userRowParser: RowParser<User> = RowParser.builder<User>()
        .field(PgTypes.int4, User::id)
        .field(PgTypes.text, User::name)
        .field(PgTypes.text, User::email)
        .build(::User)

    //start
    @Throws(SQLException::class)
    fun analyzeQuery() {
        // Build your query as normal
        val query = Fragment.interpolate("SELECT id, name, email FROM users WHERE id = ")
            .param(PgTypes.int4, userId)
            .done()
            .query(userRowParser.all())

        // Analyze it against the database
        val analysis: QueryAnalysis = QueryAnalyzer.analyze(query, connection)

        // Check the results
        if (!analysis.succeeded()) {
            throw AssertionError(analysis.report())
        }
    }
    //stop
}
