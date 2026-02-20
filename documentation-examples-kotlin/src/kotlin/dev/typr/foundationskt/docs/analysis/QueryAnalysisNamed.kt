package dev.typr.foundationskt.docs.analysis

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*
import java.sql.Connection

@Suppress("unused")
class QueryAnalysisNamed {
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
    fun analyzeNamedQuery() {
        val query =
            sql { """
                SELECT id, name, email
                FROM users
                WHERE id = ${PgTypes.int4(userId)}
            """ }
                .query(userRowCodec.all())
                .named("findUserById")

        // The name shows up in the error report
        val analysis =
            QueryAnalyzer.analyze(query, connection)
                .single()

        if (!analysis.succeeded()) {
            throw AssertionError(analysis.report())
        }
    }
    //stop
}
