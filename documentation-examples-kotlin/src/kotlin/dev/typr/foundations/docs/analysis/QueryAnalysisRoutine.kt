package dev.typr.foundations.docs.analysis

import dev.typr.kotlinfoundations.*

@Suppress("unused")
class QueryAnalysisRoutine {
    private val transactor: Transactor? = null // placeholder

    //start
    // Verify a stored function matches the database definition
    fun checkStoredFunction() {
        val addUser = Procedure.buildFunction("add_user",
            listOf(ParamDef.`in`(PgTypes.text), ParamDef.`in`(PgTypes.text)),
            PgTypes.int4)

        val checker = QueryChecker { transactor!! }
        checker.checkRoutine(addUser)
    }
    //stop
}
