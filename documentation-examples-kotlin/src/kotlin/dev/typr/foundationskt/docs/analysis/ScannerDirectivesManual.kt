package dev.typr.foundationskt.docs.analysis

import dev.typr.foundationskt.*

@Suppress("unused")
class ScannerDirectivesManual {
    private lateinit var transactor: Transactor

    class ReportRepo {
        data class ReportFilter(val category: String, val limit: Int)

        fun filteredReport(filter: ReportFilter): OperationRead<List<String>> =
            sql { "SELECT name FROM reports WHERE category = ${PgTypes.text(filter.category)}" }
                .queryAll(PgTypes.text)

        fun allReports(): OperationRead<List<String>> =
            sql { "SELECT name FROM reports" }.queryAll(PgTypes.text)
    }

    //start
    fun checkWithManualDirective() {
        val repo = ReportRepo()

        val analyzables = AnalyzableScanner.scan(
            "com.myapp.reports",

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
