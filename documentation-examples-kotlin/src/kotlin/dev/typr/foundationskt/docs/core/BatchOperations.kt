package dev.typr.foundationskt.docs.core

import dev.typr.foundationskt.*
import java.math.BigDecimal
import java.sql.Connection
import java.time.Instant

@Suppress("unused")
class BatchOperations {
    data class Product(val id: Int, val name: String, val price: BigDecimal, val createdAt: Instant)

    val productCodec: RowCodecNamed<Product> =
        RowCodec.namedBuilder<Product>()
            .field("id", PgTypes.int4, Product::id)
            .field("name", PgTypes.text, Product::name)
            .field("price", PgTypes.numeric, Product::price)
            .field("created_at", PgTypes.timestamptz, Product::createdAt)
            .build(::Product)

    lateinit var conn: Connection

    //start
    // Batch insert — all columns as parameters
    val insertAll: RowTemplate.Update<Product> =
        Fragment.insertInto("product", productCodec)

    fun insertProducts(products: List<Product>): List<Int> =
        insertAll.onMany(products.iterator()).run(conn)

    // Batch insert — skip auto-generated ID column
    val insertAutoId: RowTemplate.Update<Product> =
        Fragment.insertInto("product", productCodec, "id")

    fun insertProductsAutoId(products: List<Product>): List<Int> =
        insertAutoId.onMany(products.iterator()).run(conn)
    //stop
}
