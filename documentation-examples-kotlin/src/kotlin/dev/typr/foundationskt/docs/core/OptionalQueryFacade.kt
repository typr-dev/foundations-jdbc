package dev.typr.foundationskt.docs.core

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*

@Suppress("unused")
class OptionalQueryFacade {
    data class User(val id: Int, val name: String, val email: String)

    val userCodec: RowCodec<User> =
        RowCodec.builder<User>()
            .field(PgTypes.int4, User::id)
            .field(PgTypes.text, User::name)
            .field(PgTypes.text, User::email)
            .build(::User)

    lateinit var tx: Transactor

    //start
    // Package filters into a data class so callers see a clean API
    data class UserSearch(
        val name: String?,
        val email: String?,
        val activeOnly: Boolean
    )

    // .from() maps getters to template params
    private val searchTemplate: Template.From<UserSearch, List<User>> =
        sql { "SELECT id, name, email FROM users WHERE 1=1" }
            .optionally(
                sql { " AND name ILIKE " }.param(PgTypes.text))
            .optionally(
                sql { " AND email ILIKE " }.param(PgTypes.text))
            .optionally(
                sql { " AND active = TRUE" })
            .append(" ORDER BY name")
            .query(userCodec.all())
            .from(UserSearch::name, UserSearch::email,
                UserSearch::activeOnly)

    // Callers just pass the data class
    fun searchUsers(search: UserSearch): List<User> =
        searchTemplate.on(search).transact(tx)

    fun example(): List<User> {
        val search = UserSearch(
            name = "%alice%", email = null, activeOnly = true)
        return searchUsers(search)
    }
    //stop
}
