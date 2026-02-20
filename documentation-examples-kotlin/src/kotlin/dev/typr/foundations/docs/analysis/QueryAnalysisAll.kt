package dev.typr.foundations.docs.analysis

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*
import java.sql.Connection

@Suppress("unused")
class QueryAnalysisAll {
    data class User(val id: Int, val name: String)

    val userCodec: RowCodec<User> =
        RowCodec.builder<User>()
            .field(PgTypes.int4, User::id)
            .field(PgTypes.text, User::name)
            .build(::User)

    lateinit var conn: Connection

    val insertUser: Template<String, Int> =
        Fragment.of("INSERT INTO users(name) VALUES(")
            .param(PgTypes.text)
            .append(") RETURNING id")
            .query(RowCodec.of(PgTypes.int4).exactlyOne())

    val allUsers: Operation<List<User>> =
        Sql { "SELECT id, name FROM users" }
            .query(userCodec.all())

    //start
    fun analyzeComposedOperation() {
        // Build a composed operation
        val transaction: Operation<*> =
            insertUser.on("Alice")
                .thenIgnore(allUsers)

        // Analyze every SQL statement in the tree — one call
        val results: List<QueryAnalysis> =
            QueryAnalyzer.analyze(transaction, conn)

        for (analysis in results) {
            if (!analysis.succeeded()) {
                System.err.println(analysis.report())
            }
        }
    }
    //stop
}
