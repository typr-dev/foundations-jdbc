package dev.typr.foundationskt.docs.analysis

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

    fun insertUser(name: String): OperationRead<Int> =
        Fragment.of("INSERT INTO users(name) VALUES(")
            .value(PgTypes.text, name)
            .append(") RETURNING id")
            .query(RowCodec.of(PgTypes.int4).exactlyOne())

    val allUsers: OperationRead<List<User>> =
        Fragment.of("SELECT id, name FROM users")
            .query(userCodec.all())

    //start
    fun analyzeComposedOperation() {
        val transaction: Operation<*> =
            insertUser("Alice").productL(allUsers)

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
