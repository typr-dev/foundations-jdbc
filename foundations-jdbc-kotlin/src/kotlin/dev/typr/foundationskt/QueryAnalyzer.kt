@file:Suppress("unused")
package dev.typr.foundationskt

object QueryAnalyzer {
    @JvmStatic
    fun analyze(analyzable: Analyzable, conn: Connection): List<QueryAnalysis> =
        dev.typr.foundations.QueryAnalyzer.analyze(analyzable.analyzable, conn.javaConnection)

    @JvmStatic
    fun analyze(op: OperationRead<*>, conn: Connection): List<QueryAnalysis> =
        dev.typr.foundations.QueryAnalyzer.analyze(op.underlying, conn.javaConnection)

    @Deprecated("Use the overload that takes Connection instead", ReplaceWith("analyze(analyzable, conn)"))
    @JvmStatic
    fun analyze(analyzable: Analyzable, conn: java.sql.Connection): List<QueryAnalysis> =
        @Suppress("DEPRECATION")
        dev.typr.foundations.QueryAnalyzer.analyze(analyzable.analyzable, conn)

    @Deprecated("Use the overload that takes Connection instead", ReplaceWith("analyze(op, conn)"))
    @JvmStatic
    fun analyze(op: OperationRead<*>, conn: java.sql.Connection): List<QueryAnalysis> =
        @Suppress("DEPRECATION")
        dev.typr.foundations.QueryAnalyzer.analyze(op.underlying, conn)
}
