package dev.typr.foundationskt.docs.core

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*

@Suppress("unused")
class OperationThenRecord {
    data class NewUser(val id: Int, val name: String)

    val newUserCodec: RowCodec<NewUser> =
        RowCodec.builder<NewUser>()
            .field(PgTypes.int4, NewUser::id)
            .field(PgTypes.text, NewUser::name)
            .build(::NewUser)

    lateinit var tx: Transactor

    //start
    // Insert and return the new user (id + name).
    fun insertUser(name: String): OperationRead<NewUser> =
        Fragment.of("INSERT INTO users(name) VALUES(")
            .value(PgTypes.text, name)
            .append(") RETURNING id, name")
            .query(newUserCodec.exactlyOne())

    // Log the creation, taking the new user as input.
    fun logCreation(user: NewUser): Operation<Int> =
        Fragment.of("INSERT INTO audit_log(user_id, username) VALUES(")
            .value(PgTypes.int4, user.id)
            .append(", ")
            .value(PgTypes.text, user.name)
            .append(")")
            .update()

    // Chain: insertUser → returned NewUser → logCreation.
    fun insertAndLog(): Int =
        insertUser("Alice").then { user -> logCreation(user) }.transact(tx)
    //stop
}
