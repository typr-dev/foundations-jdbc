package dev.typr.foundationskt.docs.core

import dev.typr.foundationskt.*

@Suppress("unused")
class ReadonlyTransaction {
    data class User(val name: String, val email: String)

    val userCodec: RowCodec<User> = RowCodec.builder<User>()
        .field(PgTypes.text, User::name)
        .field(PgTypes.text, User::email)
        .build(::User)

    lateinit var tx: Transactor

    val findAll: OperationRead<List<User>> =
        Fragment.of("SELECT name, email FROM users").query(userCodec.all())

    //start
    // Single read operation — no transaction overhead
    fun allUsers(): List<User> =
        findAll.transactRead(tx)
    //stop
}
