package dev.typr.foundationskt.docs.core

import dev.typr.foundationskt.*

@Suppress("unused")
class ReadonlyTransactionMulti {
    data class User(val name: String, val email: String)

    val userCodec: RowCodec<User> = RowCodec.builder<User>()
        .field(PgTypes.text, User::name)
        .field(PgTypes.text, User::email)
        .build(::User)

    lateinit var tx: Transactor

    val findAll: OperationRead<List<User>> =
        Fragment.of("SELECT name, email FROM users").query(userCodec.all())

    val countUsers: OperationRead<Long> =
        Fragment.of("SELECT count(*) FROM users").queryExactlyOne(PgTypes.int8)

    val findRecent: OperationRead<List<User>> =
        Fragment.of("SELECT name, email FROM users ORDER BY created_at DESC LIMIT 10")
            .query(userCodec.all())

    //start
    // Multiple reads in one session — same connection, auto-commit mode
    data class Dashboard(val users: List<User>, val count: Long, val recent: List<User>)

    fun dashboard(): Dashboard =
        tx.transactRead { conn ->
            val users = conn.execute(findAll)
            val count = conn.execute(countUsers)
            val recent = conn.execute(findRecent)
            Dashboard(users, count, recent)
        }
    //stop
}
