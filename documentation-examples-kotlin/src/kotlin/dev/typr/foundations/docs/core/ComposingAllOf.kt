package dev.typr.foundations.docs.core

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*

@Suppress("unused")
class ComposingAllOf {
    lateinit var tx: Transactor

    //start
    // Run multiple writes in one transaction, discard individual results
    val insertUser: Operation<Int> =
        Fragment.of("INSERT INTO users(name) VALUES(")
            .value(PgTypes.text, "Alice").append(")").update()
    val insertAudit: Operation<Int> =
        Fragment.of("INSERT INTO audit_log(action) VALUES(")
            .value(PgTypes.text, "user_created").append(")").update()
    val updateStats: Operation<Int> =
        Fragment.of("UPDATE stats SET user_count = user_count + 1")
            .update()

    fun createUserWithAudit() {
        Operation.allOf(insertUser, insertAudit, updateStats).transact(tx)
    }
    //stop
}
