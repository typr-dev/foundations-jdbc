package dev.typr.foundationskt.docs.core

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*

@Suppress("unused")
class OptionalQueryBasic {
    data class User(val id: Int, val name: String, val email: String)

    val userCodec: RowCodec<User> =
        RowCodec.builder<User>()
            .field(PgTypes.int4, User::id)
            .field(PgTypes.text, User::name)
            .field(PgTypes.text, User::email)
            .build(::User)

    lateinit var tx: Transactor

    //start
    // A search with an optional name filter.
    // When present, the filter is applied; when absent, it's skipped.
    val searchUsers: Template<String?, List<User>> =
        sql { "SELECT id, name, email FROM users WHERE 1=1" }
            .optionally(
                sql { " AND name ILIKE " }.param(PgTypes.text))
            .query(userCodec.all())

    // Apply filter
    fun filtered(): List<User> =
        searchUsers.on("%alice%").transact(tx)

    // Skip filter — returns all users
    fun all(): List<User> =
        searchUsers.on(null).transact(tx)
    //stop
}
