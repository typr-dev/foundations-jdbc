package dev.typr.foundationskt.docs.core

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*

@Suppress("unused")
class OptionalQueryBooleanFlags {
    data class User(val id: Int, val name: String, val email: String)

    val userCodec: RowCodec<User> =
        RowCodec.builder<User>()
            .field(PgTypes.int4, User::id)
            .field(PgTypes.text, User::name)
            .field(PgTypes.text, User::email)
            .build(::User)

    lateinit var tx: Transactor

    //start
    val activeUsers: Template<Boolean, List<User>> =
        sql { "SELECT id, name, email FROM users WHERE 1=1" }
            .optionally(sql { " AND active = TRUE" })
            .query(userCodec.all())

    // Include the filter
    fun onlyActive(): List<User> =
        activeUsers.on(true).transact(tx)

    // Skip the filter — returns all users
    fun all(): List<User> =
        activeUsers.on(false).transact(tx)
    //stop
}
