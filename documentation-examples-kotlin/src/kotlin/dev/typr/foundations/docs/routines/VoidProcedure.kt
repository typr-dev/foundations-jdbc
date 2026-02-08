package dev.typr.foundations.docs.routines

import dev.typr.foundations.PgTypes
import dev.typr.kotlinfoundations.*
import java.sql.SQLException

@Suppress("unused")
class VoidProcedure {
    private val tx: dev.typr.foundations.Transactor? = null // placeholder

    //start
    companion object {
        // A void procedure — no OUT parameters, just side effects
        val auditLog: DbProcedure.Def2_0<String, String> =
            DbProcedure.define("audit_log")
                .`in`(PgTypes.text)       // action
                .`in`(PgTypes.text)       // details
                .build()
    }

    @Throws(SQLException::class)
    fun logAction(action: String, details: String) {
        auditLog.call(action, details).transact(tx!!)
    }
    //stop
}
