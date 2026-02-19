package dev.typr.foundations.docs.core

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*

@Suppress("unused")
class SqlTemplateThenFrom {
    data class NewUser(val id: Int, val name: String)

    val newUserParser: RowParser<NewUser> =
        RowParser.builder<NewUser>()
            .field(PgTypes.int4, NewUser::id)
            .field(PgTypes.text, NewUser::name)
            .build(::NewUser)

    lateinit var tx: Transactor

    //start
    // 1-param template: insert user, return id and name
    val insertUser: SqlTemplate<String, NewUser> =
        Fragment.of("INSERT INTO users(name) VALUES(")
            .param(PgTypes.text)
            .append(") RETURNING id, name")
            .query(newUserParser.exactlyOne())

    // 2-param template: log the creation with both fields
    val logCreation: SqlTemplate.Update2<Int, String> =
        Fragment.of("INSERT INTO audit_log(user_id, username) VALUES(")
            .param(PgTypes.int4)
            .append(", ")
            .param(PgTypes.text)
            .append(")")
            .update()

    // Chain: .from() adapts the 2-param template to accept NewUser
    fun insertAndLog(): Int =
        insertUser.on("Alice")
            .then(logCreation.from(NewUser::id, NewUser::name))
            .transact(tx)
    //stop
}
