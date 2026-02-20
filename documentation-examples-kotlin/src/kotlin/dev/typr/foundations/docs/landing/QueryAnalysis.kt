package dev.typr.foundations.docs.landing

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*
import java.sql.Connection

@Suppress("unused")
class QueryAnalysisExample {
    data class User(val id: Int, val name: String, val createdAt: Int, val email: String)
    lateinit var connection: Connection

    val userCodec: RowCodec<User> =
        RowCodec.builder<User>()
            .field(PgTypes.int4, User::id)           // id: correct
            .field(PgTypes.text, User::name)         // name: correct
            .field(PgTypes.int4, User::createdAt)    // created_at: WRONG! Should be timestamptz
            .field(PgTypes.text, User::email)        // email: nullable but not Optional!
            .build(::User)

    //start
    val query: Operation.Query<List<User>> =
        sql { """
            SELECT id, name, created_at, email
            FROM users
            WHERE active = ${PgTypes.bool(true)}
        """ }
            .query(userCodec.all())

    fun check() {
        val analysis: QueryAnalysis =
            QueryAnalyzer.analyze(query, connection).single()
        if (!analysis.succeeded()) {
            throw AssertionError(analysis.report())
        }
    }
    //stop
}
