package dev.typr.foundationskt.docs.analysis

import dev.typr.foundationskt.*

@Suppress("unused")
class ScannerInstance {
    private lateinit var transactor: Transactor

    class ExternalRepo {
        val allItems: OperationRead<List<String>> =
            sql { "SELECT name FROM items" }.queryAll(PgTypes.text)

        fun activeItems(): OperationRead<List<String>> =
            sql { "SELECT name FROM items WHERE active" }.queryAll(PgTypes.text)

        fun generateReport(callback: Runnable): OperationRead<List<String>> =
            sql { "SELECT name FROM items" }.queryAll(PgTypes.text)
    }

    //start
    fun checkWithExternalObjects() {
        val external = ExternalRepo()

        val analyzables = AnalyzableScanner.scan(
            "com.myapp.db",

            // Add an object from outside the scanned package
            instance(external),

            // Add with per-instance overrides
            instance(external) {
                skip(ExternalRepo::class.java, "generateReport")
            }
        )

        val checker = QueryChecker.create(transactor)
        checker.checkAll(analyzables)
    }
    //stop
}
