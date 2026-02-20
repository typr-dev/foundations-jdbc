package dev.typr.foundations.docs.analysis

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*
import java.sql.Connection

@Suppress("unused")
class QueryAnalysisBasic {
    data class User(val id: Int, val name: String, val email: String)

    private lateinit var connection: Connection
    private val userId = 1

    private val userRowCodec: RowCodec<User> =
        RowCodec.builder<User>()
            .field(PgTypes.int4, User::id)
            .field(PgTypes.text, User::name)
            .field(PgTypes.text, User::email)
            .build(::User)

    //start
    fun analyzeQuery() {
        // Build your query as normal
        val query: Operation.Query<List<User>> =
            sql { """
                SELECT id, name, email
                FROM users
                WHERE id = ${PgTypes.int4(userId)}
            """ }
                .query(userRowCodec.all())

        // Analyze it against the database
        val analysis: QueryAnalysis =
            QueryAnalyzer.analyze(query, connection).single()

        // Check the results
        if (!analysis.succeeded()) {
            throw AssertionError(analysis.report())
        }
    }
    //stop
}
