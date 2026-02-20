package dev.typr.foundationskt.docs.core

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*
import java.math.BigDecimal
import java.time.Instant

@Suppress("unused")
class NamedRowCodec {
    //start
    data class Product(val id: Int, val name: String, val price: BigDecimal, val createdAt: Instant)

    val productCodec: RowCodecNamed<Product> =
        RowCodec.namedBuilder<Product>()
            .field("id", PgTypes.int4, Product::id)
            .field("name", PgTypes.text, Product::name)
            .field("price", PgTypes.numeric, Product::price)
            .field("created_at", PgTypes.timestamptz, Product::createdAt)
            .build(::Product)

    // Column list for SQL — no hand-written strings to keep in sync
    val allProducts =
        sql { "SELECT ${productCodec.columnList} FROM product" }
    //stop
}
