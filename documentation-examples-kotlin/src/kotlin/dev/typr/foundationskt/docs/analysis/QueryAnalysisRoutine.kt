package dev.typr.foundationskt.docs.analysis

import dev.typr.foundationskt.*

@Suppress("unused")
class QueryAnalysisRoutine {
    private lateinit var transactor: Transactor

    //start
    // Verify a stored function matches the database definition
    fun checkStoredFunction() {
        val addUser =
            Procedure.buildFunction(
                "add_user",
                listOf(
                    ParamDef.input(PgTypes.text),
                    ParamDef.input(PgTypes.text)
                ),
                PgTypes.int4
            )

        val checker = QueryChecker.create(transactor)
        checker.checkRoutine(addUser)
    }
    //stop
}
