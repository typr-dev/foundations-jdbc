package dev.typr.foundations.docs.landing

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*
import dev.typr.foundationskt.QueryAnalysis as AnalysisResult
import java.sql.Connection

@Suppress("unused")
class QueryAnalysis {
    data class User(val id: Int, val name: String, val createdAt: Int, val email: String)
    lateinit var connection: Connection

    //start
    // Your query looks fine at compile time...
    val query: Operation.Query<List<User>> = Sql { "SELECT id, name, created_at, email FROM users WHERE active = ${PgTypes.bool(true)}" }
        .query(RowParser.builder<User>()
            .field(PgTypes.int4, User::id)           // id: correct
            .field(PgTypes.text, User::name)         // name: correct
            .field(PgTypes.int4, User::createdAt)    // created_at: WRONG! Should be timestamptz
            .field(PgTypes.text, User::email)        // email: nullable but not Optional!
            .build(::User)
            .all())

    // But Query Analysis catches the bugs in your tests
    fun check() {
        val analysis: AnalysisResult = QueryAnalyzer.analyze(query, connection).single()
        if (!analysis.succeeded()) {
            throw AssertionError(analysis.report())  // Fails with the detailed report
        }
    }
    //stop
}
