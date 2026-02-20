package dev.typr.foundations.docs.core

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*

@Suppress("unused")
class ComposingIfEmpty {
    data class User(val id: Int, val name: String, val email: String)

    val userParser: RowCodec<User> =
        RowCodec.builder<User>()
            .field(PgTypes.int4, User::id)
            .field(PgTypes.text, User::name)
            .field(PgTypes.text, User::email)
            .build(::User)

    lateinit var tx: Transactor
    val email: String = "alice@example.com"
    val name: String = "Alice"

    //start
    // Find-or-create pattern
    val findUser: SqlTemplate<String, User?> =
        Fragment.of("SELECT id, name, email FROM users WHERE email = ")
            .param(PgTypes.text)
            .query(userParser.maxOne())

    val createUser: SqlTemplate.Query2<String, String, User> =
        Fragment.of("INSERT INTO users(name, email) VALUES(")
            .param(PgTypes.text)
            .append(", ")
            .param(PgTypes.text)
            .append(") RETURNING *")
            .query(userParser.exactlyOne())

    fun findOrCreate(): User =
        Operation.ifEmpty(
            findUser.on(email),
            createUser.on(name, email)
        ).transact(tx)
    //stop
}
