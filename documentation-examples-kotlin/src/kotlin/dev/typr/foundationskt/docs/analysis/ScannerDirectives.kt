package dev.typr.foundationskt.docs.analysis

import dev.typr.foundationskt.*

@Suppress("unused")
class ScannerDirectives {
    private lateinit var transactor: Transactor

    class ReportRepo {
        fun generateReport(onProgress: Runnable): Operation<List<String>> =
            sql { "SELECT name FROM reports" }.queryAll(PgTypes.text)

        fun allReports(): Operation<List<String>> =
            sql { "SELECT name FROM reports" }.queryAll(PgTypes.text)
    }

    //start
    fun checkWithDirectives() {
        val analyzables = AnalyzableScanner.scan(
            "com.myapp.reports",

            // Skip a method entirely — it won't be type-checked
            skip(ReportRepo::class.java, "generateReport")
        )

        val checker = QueryChecker.create(transactor)
        checker.checkAll(analyzables)
    }
    //stop
}
