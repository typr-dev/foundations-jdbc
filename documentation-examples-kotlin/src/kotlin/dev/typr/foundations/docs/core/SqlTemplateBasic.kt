package dev.typr.foundations.docs.core

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*

@Suppress("unused")
class SqlTemplateBasic {
    data class User(val id: Int, val name: String, val email: String)

    val userParser: RowParser<User> = RowParser.builder<User>()
        .field(PgTypes.int4, User::id)
        .field(PgTypes.text, User::name)
        .field(PgTypes.text, User::email)
        .build(::User)

    lateinit var tx: Transactor

    //start
    // Define a reusable template — SQL structure is fixed, values come later
    val findByEmail: SqlTemplate<String, User?> =
        Fragment.of("SELECT id, name, email FROM users WHERE email = ")
            .param(PgTypes.text)
            .query(userParser.maxOne())

    // Fill the template with a value to get a concrete operation
    fun findAlice(): User? = findByEmail.on("alice@example.com").transact(tx)

    // Reuse the same template with different values
    fun findBob(): User? = findByEmail.on("bob@example.com").transact(tx)
    //stop
}
