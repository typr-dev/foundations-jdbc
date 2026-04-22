package dev.typr.foundationskt.docs.analysis

import dev.typr.foundationskt.*

@Suppress("unused")
class QueryAnalysisRoutine {
    private lateinit var transactor: Transactor

    //start
    // Verify a stored function matches the database definition
    fun checkStoredFunction() {
        val addUser = DbFunction.define("add_user", PgTypes.int4)
            .input(PgTypes.text)
            .input(PgTypes.text)
            .build()

        val checker = QueryChecker.create(transactor)
        checker.checkRoutine(addUser)
    }
    //stop
}
