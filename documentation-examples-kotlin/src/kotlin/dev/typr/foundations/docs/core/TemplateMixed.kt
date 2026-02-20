package dev.typr.foundations.docs.core

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*

@Suppress("unused")
class TemplateMixed {
    data class User(val id: Int, val name: String, val status: String)

    val userCodec: RowCodec<User> =
        RowCodec.builder<User>()
            .field(PgTypes.int4, User::id)
            .field(PgTypes.text, User::name)
            .field(PgTypes.text, User::status)
            .build(::User)

    lateinit var tx: Transactor

    //start
    // Mix bound and unbound parameters in the same template.
    // Status is fixed at "active"; name filter and limit vary per call.
    val activeUsersByName: Template.Query2<String, Int, List<User>> =
        sql { "SELECT id, name, status FROM users WHERE status = " }
            .value(PgTypes.text, "active")
            .append(" AND name ILIKE ")
            .param(PgTypes.text)
            .append(" ORDER BY name LIMIT ")
            .param(PgTypes.int4)
            .query(userCodec.all())

    fun example(): List<User> =
        activeUsersByName.on("%alice%", 10).transact(tx)
    //stop
}
