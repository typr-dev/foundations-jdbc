package dev.typr.foundationskt.docs.core

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*
import java.math.BigDecimal

@Suppress("unused")
class OptionalQueryRange {
    data class Product(val id: Int, val name: String, val price: BigDecimal)

    val productCodec: RowCodec<Product> =
        RowCodec.builder<Product>()
            .field(PgTypes.int4, Product::id)
            .field(PgTypes.text, Product::name)
            .field(PgTypes.numeric, Product::price)
            .build(::Product)

    lateinit var tx: Transactor

    //start
    // When an optional clause needs multiple parameters,
    // pass a multi-parameter builder.
    // The grouped parameters are provided or omitted together.
    val byPriceRange: Template<Pair<BigDecimal, BigDecimal>?, List<Product>> =
        sql { "SELECT id, name, price FROM products WHERE 1=1" }
            .optionally(
                sql { " AND price BETWEEN " }
                    .param(PgTypes.numeric)
                    .append(" AND ")
                    .param(PgTypes.numeric))
            .query(productCodec.all())

    // With range
    fun inRange(): List<Product> =
        byPriceRange
            .on(BigDecimal("10") to BigDecimal("50"))
            .transact(tx)

    // Without range — returns all products
    fun all(): List<Product> =
        byPriceRange.on(null).transact(tx)
    //stop
}
