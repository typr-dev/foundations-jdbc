package dev.typr.foundations.docs.analysis

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*
import java.math.BigDecimal
import java.sql.Connection

@Suppress("unused")
class QueryAnalysisNullableOk {
    private lateinit var connection: Connection

    //start
    data class OrderRow(val userId: Int, val userName: String, val orderTotal: BigDecimal)

    // The LEFT JOIN makes o.total nullable in the result set,
    // but .nullableOk() tells analysis we'll handle it
    val orderCodec: RowCodec<OrderRow> =
        RowCodec.builder<OrderRow>()
            .field(PgTypes.int4, OrderRow::userId)
            .field(PgTypes.text, OrderRow::userName)
            .field(PgTypes.numeric.nullableOk(), OrderRow::orderTotal)
            .build(::OrderRow)

    fun analyzeLeftJoin() {
        val query =
            Sql { """
                SELECT u.id, u.name, o.total
                FROM users u
                LEFT JOIN orders o ON u.id = o.user_id
            """ }
                .query(orderCodec.all())

        val analysis: QueryAnalysis =
            QueryAnalyzer.analyze(query, connection).single()
        if (!analysis.succeeded()) {
            throw AssertionError(analysis.report())
        }
    }
    //stop
}
