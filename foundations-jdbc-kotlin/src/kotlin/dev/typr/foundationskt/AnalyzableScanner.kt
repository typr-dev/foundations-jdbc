@file:Suppress("unused")
package dev.typr.foundationskt

object AnalyzableScanner {
    @JvmStatic
    fun scan(packageName: String): List<dev.typr.foundations.Analyzable> =
        dev.typr.foundations.AnalyzableScanner.scan(packageName)

    @JvmStatic
    fun scan(packageName: String, transactor: Transactor): List<dev.typr.foundations.Analyzable> =
        dev.typr.foundations.AnalyzableScanner.scan(packageName, transactor.underlying)
}
