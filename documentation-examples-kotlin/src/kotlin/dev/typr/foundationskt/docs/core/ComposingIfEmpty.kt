package dev.typr.foundationskt.docs.core

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*

@Suppress("unused")
class ComposingIfEmpty {
    data class User(val id: Int, val name: String, val email: String)

    val userCodec: RowCodec<User> =
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
    fun findUser(email: String): OperationRead<User?> =
        Fragment.of("SELECT id, name, email FROM users WHERE email = ")
            .value(PgTypes.text, email)
            .query(userCodec.maxOne())

    fun createUser(name: String, email: String): OperationRead<User> =
        Fragment.of("INSERT INTO users(name, email) VALUES(")
            .value(PgTypes.text, name)
            .append(", ")
            .value(PgTypes.text, email)
            .append(") RETURNING *")
            .query(userCodec.exactlyOne())

    fun findOrCreate(): User =
        Operation.ifEmpty(findUser(email), createUser(name, email)).transact(tx)
    //stop
}
