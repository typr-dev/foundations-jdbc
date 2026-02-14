package dev.typr.foundations.docs.analysis

import dev.typr.kotlinfoundations.*

@Suppress("unused")
class QueryAnalysisRoutine {
    private lateinit var transactor: Transactor

    //start
    // Verify a stored function matches the database definition
    fun checkStoredFunction() {
        val addUser = Procedure.buildFunction("add_user",
            listOf(ParamDef.`in`(PgTypes.text), ParamDef.`in`(PgTypes.text)),
            PgTypes.int4)

        val checker = QueryChecker.create(transactor)
        QueryChecker.checkRoutine(checker, addUser)
    }
    //stop
}
