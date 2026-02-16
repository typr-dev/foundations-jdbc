package dev.typr.foundations.docs.analysis

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*
import java.sql.Connection

@Suppress("unused")
class QueryAnalysisNamed {
    data class User(val id: Int, val name: String, val email: String)

    private lateinit var connection: Connection
    private val userId = 1

    private val userRowParser: RowParser<User> =
        RowParser.builder<User>()
            .field(PgTypes.int4, User::id)
            .field(PgTypes.text, User::name)
            .field(PgTypes.text, User::email)
            .build(::User)

    //start
    fun analyzeNamedQuery() {
        val query =
            Sql { """
                SELECT id, name, email
                FROM users
                WHERE id = ${PgTypes.int4(userId)}
            """.trimIndent() }
                .query(userRowParser.all())

        // Give your query a name - it shows up in the error report
        val analysis =
            QueryAnalyzer.analyze("findUserById", query, connection)
                .single()

        if (!analysis.succeeded()) {
            throw AssertionError(analysis.report())
        }
    }
    //stop
}
