package dev.typr.foundationskt.docs.analysis

import dev.typr.foundationskt.*

@Suppress("unused")
class ScannerDirectives {
    private lateinit var transactor: Transactor

    class ReportRepo {
        data class ReportFilter(val category: String, val limit: Int)

        fun generateReport(onProgress: Runnable): Operation<List<String>> =
            sql { "SELECT name FROM reports" }.queryAll(PgTypes.text)

        fun filteredReport(filter: ReportFilter): Operation<List<String>> =
            sql { "SELECT name FROM reports WHERE category = ${PgTypes.text(filter.category)}" }
                .queryAll(PgTypes.text)

        fun allReports(): Operation<List<String>> =
            sql { "SELECT name FROM reports" }.queryAll(PgTypes.text)
    }

    //start
    fun checkWithDirectives() {
        val repo = ReportRepo()

        val analyzables = AnalyzableScanner.scan(
            "com.myapp.reports",

            // Skip a method entirely — it won't be type-checked
            skip(ReportRepo::class.java, "generateReport"),

            // Provide specific arguments for a method
            manual(
                ReportRepo::class.java, "filteredReport", "defaults",
                repo.filteredReport(ReportRepo.ReportFilter("all", 100)))
        )

        val checker = QueryChecker.create(transactor)
        checker.checkAll(analyzables)
    }
    //stop
}
