@file:Suppress("unused")
package dev.typr.foundationskt

object QueryAnalyzer {
    @JvmStatic
    fun analyze(op: Operation<*>, conn: java.sql.Connection): List<dev.typr.foundations.QueryAnalysis> =
        dev.typr.foundations.QueryAnalyzer.analyze(op.underlying, conn)

    @JvmStatic
    fun analyze(template: SqlTemplate<*, *>, conn: java.sql.Connection): List<dev.typr.foundations.QueryAnalysis> =
        dev.typr.foundations.QueryAnalyzer.analyze(template.underlying, conn)

    @JvmStatic
    fun analyze(template: RowSqlTemplate<*, *>, conn: java.sql.Connection): List<dev.typr.foundations.QueryAnalysis> =
        dev.typr.foundations.QueryAnalyzer.analyze(template.underlying, conn)
}
