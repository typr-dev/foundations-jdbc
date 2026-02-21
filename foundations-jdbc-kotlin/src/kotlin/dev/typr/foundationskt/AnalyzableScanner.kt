@file:Suppress("unused")
package dev.typr.foundationskt

object AnalyzableScanner {
    private fun wrap(java: dev.typr.foundations.Analyzable): Analyzable =
        object : Analyzable {
            override val analyzable: dev.typr.foundations.Analyzable = java
        }

    @JvmStatic
    fun scan(packageName: String): List<Analyzable> =
        dev.typr.foundations.AnalyzableScanner.scan(packageName).map(::wrap)

    @JvmStatic
    fun scan(packageName: String, transactor: Transactor): List<Analyzable> =
        dev.typr.foundations.AnalyzableScanner.scan(packageName, transactor.underlying).map(::wrap)

    @JvmStatic
    fun scanDetailed(packageName: String): List<dev.typr.foundations.AnalyzableScanner.Result> =
        dev.typr.foundations.AnalyzableScanner.scanDetailed(packageName)

    @JvmStatic
    fun scanDetailed(packageName: String, transactor: Transactor): List<dev.typr.foundations.AnalyzableScanner.Result> =
        dev.typr.foundations.AnalyzableScanner.scanDetailed(packageName, transactor.underlying)

    @JvmStatic
    fun describe(analyzable: Analyzable): String =
        dev.typr.foundations.AnalyzableScanner.describe(analyzable.analyzable)
}
