package dev.typr.foundations.docs.analysis

import dev.typr.foundations.ParamDef
import dev.typr.foundations.PgTypes
import dev.typr.foundations.Procedure
import dev.typr.foundations.analysis.QueryChecker

@Suppress("unused")
class QueryAnalysisRoutine {
    private val transactor: dev.typr.foundations.Transactor? = null // placeholder

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
