package dev.typr.foundationskt.docs.landing

import dev.typr.foundationskt.*
import java.math.BigDecimal
import java.time.Instant

@Suppress("unused")
class ProductRowCodec {
    data class Product(val id: Int, val name: String, val price: BigDecimal, val createdAt: Instant?)
    data class Category(val id: Int, val name: String)

    val categoryRowCodec: RowCodec<Category> =
        RowCodec.builder<Category>()
            .field(PgTypes.int4, Category::id)
            .field(PgTypes.text, Category::name)
            .build(::Category)

    //start
    val productCodec: RowCodecNamed<Product> =
        RowCodec.namedBuilder<Product>()
            .field("id", PgTypes.int4, Product::id)
            .field("name", PgTypes.text, Product::name)
            .field("price", PgTypes.numeric, Product::price)
            .field("created_at", PgTypes.timestamptz.opt(), Product::createdAt)
            .build(::Product)

    // Compose codecs for joins
    val joined: RowCodec<Pair<Product, Category?>> =
        productCodec.leftJoin(categoryRowCodec)
    //stop
}
