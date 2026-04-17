package dev.typr.foundationskt.docs.core

import dev.typr.foundationskt.*
import java.math.BigDecimal

@Suppress("unused")
object JsonAggregation {
    lateinit var tx: Transactor

    data class OrderLine(val product: String, val qty: Int, val price: BigDecimal)

    val lineCodec: RowCodec<OrderLine> =
        RowCodec.builder<OrderLine>()
            .field(DuckDbTypes.varchar, OrderLine::product)
            .field(DuckDbTypes.integer, OrderLine::qty)
            .field(DuckDbTypes.decimalOf(10, 2), OrderLine::price)
            .build(::OrderLine)

    // A column type that stores rows as positional JSON arrays
    val linesType: DuckDbType<List<OrderLine>> =
        DuckDbTypes.jsonArrayEncodedList(lineCodec)

    //start
    // Aggregate child rows as JSON in a single query
    fun getOrderLines(customerId: Int): List<OrderLine> =
        sql { """
            SELECT json_group_array(json_array(product, qty, price))
            FROM order_lines
            WHERE customer_id = ${DuckDbTypes.integer(customerId)}
        """ }
            .query(RowCodec.of(linesType).exactlyOne())
            .transact(tx)
    //stop
}
