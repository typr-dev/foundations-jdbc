package dev.typr.foundationskt.docs.analysis

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*

@Suppress("unused")
class QueryAnalysisBasic {
    data class User(val id: Int, val name: String, val email: String)

    private lateinit var transactor: Transactor

    private val userRowCodec: RowCodec<User> =
        RowCodec.builder<User>()
            .field(PgTypes.int4, User::id)
            .field(PgTypes.text, User::name)
            .field(PgTypes.text, User::email)
            .build(::User)

    //start
    fun checkQueryManually() {
        val query: OperationRead.Query<List<User>> =
            sql { """
                SELECT id, name, email
                FROM users
                WHERE id = ${PgTypes.int4(1)}
            """ }
                .query(userRowCodec.all())

        val checker = QueryChecker.create(transactor)
        checker.check(query)
    }
    //stop
}
