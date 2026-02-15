package dev.typr.foundations.docs.core

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*

@Suppress("unused")
class ComposingAllOf {
    lateinit var tx: Transactor

    //start
    // Run multiple writes in one transaction, discard individual results
    val insertUser: Operation<Int> =
        Sql { "INSERT INTO users(name) VALUES(${PgTypes.text("Alice")})" }.update()
    val insertAudit: Operation<Int> =
        Sql { "INSERT INTO audit_log(action) VALUES(${PgTypes.text("user_created")})" }.update()
    val updateStats: Operation<Int> =
        Sql { "UPDATE stats SET user_count = user_count + 1" }.update()

    fun createUserWithAudit() {
        Operation.allOf(insertUser, insertAudit, updateStats).transact(tx)
    }
    //stop
}
