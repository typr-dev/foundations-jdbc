package dev.typr.foundationskt.docs.landing

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*
import java.math.BigDecimal
import java.time.OffsetDateTime

@Suppress("unused")
class ProductRow {
    data class ProductId(val value: Int)
    data class Dimensions(val width: Double, val height: Double, val depth: Double, val unit: String)

    //start
    data class Product(
        val id: ProductId,
        val name: String,
        val price: BigDecimal,
        val tags: Array<String>?,            // text[]
        val dimensions: Dimensions?,         // composite type
        val metadata: Jsonb?,                // jsonb
        val createdAt: OffsetDateTime?       // timestamptz
    )
    //stop
}
