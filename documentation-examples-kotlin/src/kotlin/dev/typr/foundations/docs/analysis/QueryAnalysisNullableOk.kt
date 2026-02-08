package dev.typr.foundations.docs.analysis

import dev.typr.kotlinfoundations.*
import dev.typr.kotlinfoundations.data.*
import java.math.BigDecimal
import java.sql.Connection
import java.sql.SQLException

@Suppress("unused")
class QueryAnalysisNullableOk {
    private val connection: Connection? = null // placeholder

    //start
    data class OrderRow(val userId: Int, val userName: String, val orderTotal: BigDecimal)

    // The LEFT JOIN makes o.total nullable in the result set,
    // but .nullableOk() tells analysis we'll handle it
    val orderParser: RowParser<OrderRow> = RowParser.builder<OrderRow>()
        .field(PgTypes.int4, OrderRow::userId)
        .field(PgTypes.text, OrderRow::userName)
        .field(PgTypes.numeric.nullableOk(), OrderRow::orderTotal)
        .build(::OrderRow)

    @Throws(SQLException::class)
    fun analyzeLeftJoin() {
        val query = Fragment.lit("""
            SELECT u.id, u.name, o.total
            FROM users u
            LEFT JOIN orders o ON u.id = o.user_id
        """.trimIndent()).query(orderParser.all())

        val analysis: QueryAnalysis = QueryAnalyzer.analyze(query, connection)
        if (!analysis.succeeded()) {
            throw AssertionError(analysis.report())
        }
    }
    //stop
}
