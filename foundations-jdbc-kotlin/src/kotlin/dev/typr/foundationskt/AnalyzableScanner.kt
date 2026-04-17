@file:Suppress("unused")
package dev.typr.foundationskt

object AnalyzableScanner {
    private fun wrap(java: dev.typr.foundations.Analyzable): Analyzable =
        object : Analyzable {
            override val analyzable: dev.typr.foundations.Analyzable = java
            override fun toString(): String = java.toString()
        }

    @JvmStatic
    fun scan(packageName: String): List<Analyzable> =
        dev.typr.foundations.AnalyzableScanner.scan(packageName).map(::wrap)

    @JvmStatic
    fun scan(packageName: String, transactor: Transactor): List<Analyzable> =
        dev.typr.foundations.AnalyzableScanner.scan(packageName, transactor.underlying).map(::wrap)

    @JvmStatic
    fun scan(packageName: String, vararg directives: ScanDirective): List<Analyzable> =
        dev.typr.foundations.AnalyzableScanner
            .scan(packageName, *directives.map { it.toJava() }.toTypedArray())
            .map(::wrap)

    @JvmStatic
    fun scan(packageName: String, transactor: Transactor, vararg directives: ScanDirective): List<Analyzable> =
        dev.typr.foundations.AnalyzableScanner
            .scan(packageName, transactor.underlying, *directives.map { it.toJava() }.toTypedArray())
            .map(::wrap)


    @JvmStatic
    fun scanDetailed(packageName: String): List<dev.typr.foundations.AnalyzableScanner.Result> =
        dev.typr.foundations.AnalyzableScanner.scanDetailed(packageName)

    @JvmStatic
    fun scanDetailed(packageName: String, transactor: Transactor): List<dev.typr.foundations.AnalyzableScanner.Result> =
        dev.typr.foundations.AnalyzableScanner.scanDetailed(packageName, transactor.underlying)

    @JvmStatic
    fun scanDetailed(packageName: String, vararg directives: ScanDirective): List<dev.typr.foundations.AnalyzableScanner.Result> =
        dev.typr.foundations.AnalyzableScanner.scanDetailed(packageName, *directives.map { it.toJava() }.toTypedArray())

    @JvmStatic
    fun scanDetailed(packageName: String, transactor: Transactor, vararg directives: ScanDirective): List<dev.typr.foundations.AnalyzableScanner.Result> =
        dev.typr.foundations.AnalyzableScanner.scanDetailed(packageName, transactor.underlying, *directives.map { it.toJava() }.toTypedArray())

    @JvmStatic
    fun describe(analyzable: Analyzable): String =
        dev.typr.foundations.AnalyzableScanner.describe(analyzable.analyzable)
}
