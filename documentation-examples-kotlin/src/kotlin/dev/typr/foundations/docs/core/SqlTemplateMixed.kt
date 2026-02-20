package dev.typr.foundations.docs.core

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*

@Suppress("unused")
class SqlTemplateMixed {
    data class User(val id: Int, val name: String, val status: String)

    val userParser: RowCodec<User> =
        RowCodec.builder<User>()
            .field(PgTypes.int4, User::id)
            .field(PgTypes.text, User::name)
            .field(PgTypes.text, User::status)
            .build(::User)

    lateinit var tx: Transactor

    //start
    // Mix bound and unbound parameters in the same template
    // The status is fixed at "active", but the limit varies per call
    val activeUsersWithLimit: SqlTemplate<Int, List<User>> =
        Fragment.of("SELECT id, name, status FROM users WHERE status = ")
            .value(PgTypes.text, "active")
            .append(" ORDER BY name LIMIT ")
            .param(PgTypes.int4)
            .query(userParser.all())

    fun topTen(): List<User> =
        activeUsersWithLimit.on(10).transact(tx)

    fun topFifty(): List<User> =
        activeUsersWithLimit.on(50).transact(tx)
    //stop
}
