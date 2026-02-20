package dev.typr.foundations.docs.landing

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*
import java.math.BigDecimal
import java.time.Instant

@Suppress("unused")
class ProductRowCodec {
    data class Product(val id: ProductId, val name: String, val price: BigDecimal, val tags: Array<String>?,
                       val dimensions: Dim?, val metadata: Jsonb?, val createdAt: Instant?)
    data class ProductId(val value: Int)
    data class Dim(val width: Double, val height: Double, val depth: Double, val unit: String)
    data class Category(val id: Int, val name: String)

    val productIdType: PgType<ProductId> =
        PgTypes.int4.transform(::ProductId, ProductId::value)
    val dimensionsType: PgType<Dim> =
        PgStruct.builder<Dim>("dimensions")
            .field("width", PgTypes.float8, Dim::width)
            .field("height", PgTypes.float8, Dim::height)
            .field("depth", PgTypes.float8, Dim::depth)
            .field("unit", PgTypes.text, Dim::unit)
            .build(::Dim)
            .asType()

    val categoryRowCodec: RowCodec<Category> =
        RowCodec.builder<Category>()
            .field(PgTypes.int4, Category::id)
            .field(PgTypes.text, Category::name)
            .build(::Category)

    //start
    val rowCodec: RowCodec<Product> =
        RowCodec.builder<Product>()
            .field(productIdType, Product::id)
            .field(PgTypes.text, Product::name)
            .field(PgTypes.numeric, Product::price)
            .field(PgTypes.textArray.opt(), Product::tags)
            .field(dimensionsType.opt(), Product::dimensions)
            .field(PgTypes.jsonb.opt(), Product::metadata)
            .field(PgTypes.timestamptz.opt(), Product::createdAt)
            .build(::Product)

    // Compose codecs for joins
    val joined: RowCodec<Pair<Product, Category?>> =
        rowCodec.leftJoined(categoryRowCodec)
    //stop
}
