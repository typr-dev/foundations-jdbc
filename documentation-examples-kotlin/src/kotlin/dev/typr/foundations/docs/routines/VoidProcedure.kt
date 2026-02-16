package dev.typr.foundations.docs.routines

import dev.typr.foundationskt.*

@Suppress("unused")
class VoidProcedure {
    private val tx: Transactor? = null // placeholder

    //start
    companion object {
        // A void procedure — no OUT parameters, just side effects
        val auditLog =
            DbProcedure.define("audit_log")
                .input(PgTypes.text)       // action
                .input(PgTypes.text)       // details
                .build()
    }

    fun logAction(action: String, details: String) {
        auditLog.call(action, details).transact(tx!!)
    }
    //stop
}
