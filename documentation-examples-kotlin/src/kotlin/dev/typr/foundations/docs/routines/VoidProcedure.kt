package dev.typr.foundations.docs.routines

import dev.typr.kotlinfoundations.*

@Suppress("unused")
class VoidProcedure {
    private val tx: Transactor? = null // placeholder

    //start
    companion object {
        // A void procedure — no OUT parameters, just side effects
        val auditLog: DbProcedure.Def2_0<String, String> =
            DbProcedure.define("audit_log")
                .`in`(PgTypes.text)       // action
                .`in`(PgTypes.text)       // details
                .build()
    }

    fun logAction(action: String, details: String) {
        auditLog.call(action, details).transact(tx!!)
    }
    //stop
}
