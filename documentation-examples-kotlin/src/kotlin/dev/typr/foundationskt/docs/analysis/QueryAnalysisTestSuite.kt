package dev.typr.foundationskt.docs.analysis

import dev.typr.foundationskt.*

@Suppress("unused")
class QueryAnalysisTestSuite {
    private lateinit var transactor: Transactor

    //start
    fun allQueriesTypeCheck() {
        val analyzables = AnalyzableScanner.scan("com.myapp.db")
        val checker = QueryChecker.create(transactor)
        checker.checkAll(analyzables)
    }
    //stop
}
