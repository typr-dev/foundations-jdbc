package dev.typr.foundationskt.docs.core

import dev.typr.foundationskt.*
import java.math.BigDecimal
import java.time.Instant

@Suppress("unused")
class FragmentRowGeneratedKeys {
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
    // For databases without RETURNING (DB2, Oracle, SQL Server, MariaDB):
    fun insertGeneratedKey(product: Product): Int =
        Fragment.insertIntoGeneratedKey(
                "product", productCodec, "id",
                RowCodec.of(PgTypes.int4).exactlyOne())
            .on(product)
            .run(conn)
    //stop
}
