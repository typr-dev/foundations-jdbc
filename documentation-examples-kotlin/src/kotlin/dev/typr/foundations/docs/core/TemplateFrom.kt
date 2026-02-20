package dev.typr.foundations.docs.core

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*

@Suppress("unused")
class TemplateFrom {
    lateinit var tx: Transactor

    //start
    // A data class gives names to each template parameter
    data class InsertUser(val name: String, val email: String)

    // .from() maps data class fields to template params
    val insertUser: Template.From<InsertUser, Int> =
        sql { "INSERT INTO users(name, email) VALUES(" }
            .param(PgTypes.text)
            .append(", ")
            .param(PgTypes.text)
            .append(")")
            .update()
            .from(InsertUser::name, InsertUser::email)

    // Callers pass the data class
    fun createUser(): Int =
        insertUser.on(InsertUser("Alice", "alice@example.com"))
            .transact(tx)
    //stop
}
