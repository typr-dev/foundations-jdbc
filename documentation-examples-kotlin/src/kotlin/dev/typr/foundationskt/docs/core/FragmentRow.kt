package dev.typr.foundationskt.docs.core

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*
import java.math.BigDecimal
import java.time.Instant

@Suppress("unused")
class FragmentRow {
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
    fun insert(product: Product): Product =
        Fragment.insertOneReturning("product", productCodec, product).run(conn)

    // Skip columns with database defaults — pass column names to except
    fun insertWithDefault(product: Product): Product =
        Fragment.insertOneReturning("product", productCodec, product, "id").run(conn)
    //stop
}
