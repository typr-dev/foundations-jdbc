package dev.typr.foundations.docs.analysis

import dev.typr.kotlinfoundations.*
import dev.typr.kotlinfoundations.data.*
import java.sql.Connection
import java.sql.SQLException

@Suppress("unused")
class QueryAnalysisNamed {
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
    fun analyzeNamedQuery() {
        val query = Fragment.interpolate("SELECT id, name, email FROM users WHERE id = ")
            .param(PgTypes.int4, userId)
            .done()
            .query(userRowParser.all())

        // Give your query a name - it shows up in the error report
        val analysis = dev.typr.foundations.analysis.QueryAnalyzer.analyze("findUserById", query, connection)

        if (!analysis.succeeded()) {
            throw AssertionError(analysis.report())
        }
    }
    //stop
}
