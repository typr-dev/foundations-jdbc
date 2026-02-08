package dev.typr.foundations.docs.postgresql

import dev.typr.kotlinfoundations.*

import java.math.BigDecimal

@Suppress("unused")
class StreamingInsertMulti {

    data class ProductRow(val name: String, val price: BigDecimal, val quantity: Int)

    //start
    // Define a RowParser for your row type
    val productParser: RowParser<ProductRow> = RowParser.builder<ProductRow>()
        .field(PgTypes.text, ProductRow::name)
        .field(PgTypes.numeric, ProductRow::price)
        .field(PgTypes.int4, ProductRow::quantity)
        .build(::ProductRow)

    // PgText.from() derives a text encoder from the RowParser
    val productText: PgText<ProductRow> = PgText.from(productParser.underlying)

    fun insertProducts(products: Iterator<ProductRow>, tx: Transactor): Long {
        return streamingInsert
            .of("COPY products(name, price, quantity) FROM STDIN", 1000, products, productText)
            .transact(tx)
    }
    //stop
}
