package dev.typr.foundations.docs.postgresql

import dev.typr.foundationskt.*

import java.math.BigDecimal

@Suppress("unused")
class StreamingInsertMulti {

    data class ProductRow(val name: String, val price: BigDecimal, val quantity: Int)

    //start
    // Define a RowCodec for your row type
    val productCodec: RowCodec<ProductRow> = RowCodec.builder<ProductRow>()
        .field(PgTypes.text, ProductRow::name)
        .field(PgTypes.numeric, ProductRow::price)
        .field(PgTypes.int4, ProductRow::quantity)
        .build(::ProductRow)

    // PgText.from() derives a text encoder from the RowCodec
    val productText: PgText<ProductRow> = PgText.from(productCodec.underlying)

    fun insertProducts(products: Iterator<ProductRow>, tx: Transactor): Long {
        return streamingInsert
            .of("COPY products(name, price, quantity) FROM STDIN", 1000, products, productText)
            .transact(tx)
    }
    //stop
}
