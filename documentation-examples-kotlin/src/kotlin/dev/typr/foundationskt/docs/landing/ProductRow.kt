package dev.typr.foundationskt.docs.landing

import java.math.BigDecimal
import java.time.Instant

@Suppress("unused")
class ProductRow {
    //start
    data class Product(
        val id: Int,
        val name: String,
        val price: BigDecimal,
        val createdAt: Instant?
    )
    //stop
}
