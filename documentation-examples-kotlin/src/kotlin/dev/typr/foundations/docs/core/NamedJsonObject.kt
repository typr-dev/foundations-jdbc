package dev.typr.foundations.docs.core

import dev.typr.foundationskt.*
import java.math.BigDecimal

@Suppress("unused")
class NamedJsonObject {
    //start
    data class OrderLine(val product: String, val qty: Int, val price: BigDecimal)

    val lineParser: RowParserNamed<OrderLine> =
        RowParser.namedBuilder<OrderLine>()
            .field("product", DuckDbTypes.varchar, OrderLine::product)
            .field("qty", DuckDbTypes.integer, OrderLine::qty)
            .field("price", DuckDbTypes.decimal(10, 2), OrderLine::price)
            .build(::OrderLine)

    // Stores rows as positional JSON arrays: [["Widget", 3, 9.99], ...]
    val arrayType: DuckDbType<List<OrderLine>> =
        DuckDbTypes.jsonArrayEncodedList(lineParser)

    // Stores rows as named JSON objects: [{"product": "Widget", "qty": 3, "price": 9.99}, ...]
    // Column names come from the parser — no redundant list to maintain
    val objectType: DuckDbType<List<OrderLine>> =
        DuckDbTypes.jsonObjectEncodedList(lineParser)
    //stop
}
