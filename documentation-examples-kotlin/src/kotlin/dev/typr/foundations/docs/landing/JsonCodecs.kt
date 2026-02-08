package dev.typr.foundations.docs.landing

import dev.typr.kotlinfoundations.*
import dev.typr.kotlinfoundations.data.*
import java.math.BigDecimal
import java.sql.SQLException

@Suppress("unused")
object JsonCodecs {
    var tx: Transactor? = null // placeholder

    //start
    data class OrderLine(val product: String, val qty: Int, val price: BigDecimal)

    // Define once — reads from ResultSet AND JSON
    val lineParser: RowParser<OrderLine> = RowParser.builder<OrderLine>()
        .field(DuckDbTypes.varchar, OrderLine::product)
        .field(DuckDbTypes.integer, OrderLine::qty)
        .field(DuckDbTypes.decimal(10, 2), OrderLine::price)
        .build(::OrderLine)

    // Same RowParser, now as a JSON codec — zero extra code
    val linesCodec: DbJson<List<OrderLine>> =
        lineParser.jsonArray().list()

    // Aggregate child rows as JSON in a single query
    @Throws(SQLException::class)
    fun getOrderLines(customerId: Int): List<OrderLine> {
        val json: Json = Fragment.interpolate(
            "SELECT json_group_array(json_array(product, qty, price)) "
            + "FROM order_lines WHERE customer_id = ")
            .param(DuckDbTypes.integer, customerId)
            .done()
            .query(RowParser.of(DuckDbTypes.json).exactlyOne())
            .transact(tx)

        return linesCodec.fromJson(JsonValue.parse(json.value()))
    }
    //stop
}
