package dev.typr.foundations.docs.landing

import dev.typr.foundationskt.*
import java.math.BigDecimal

@Suppress("unused")
object JsonCodecs {
    lateinit var tx: Transactor

    data class OrderLine(val product: String, val qty: Int, val price: BigDecimal)

    val lineParser: RowCodec<OrderLine> =
        RowCodec.builder<OrderLine>()
            .field(DuckDbTypes.varchar, OrderLine::product)
            .field(DuckDbTypes.integer, OrderLine::qty)
            .field(DuckDbTypes.decimal(10, 2), OrderLine::price)
            .build(::OrderLine)

    //start
    // RowCodec → JSON column type, zero extra code
    val linesType: DuckDbType<List<OrderLine>> =
        DuckDbTypes.jsonArrayEncodedList(lineParser)

    fun getOrderLines(customerId: Int): List<OrderLine> =
        Sql { """
            SELECT json_group_array(json_array(product, qty, price))
            FROM order_lines
            WHERE customer_id = ${DuckDbTypes.integer(customerId)}
        """ }
            .query(RowCodec.of(linesType).exactlyOne())
            .transact(tx)
    //stop
}
