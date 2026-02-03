package dev.typr.foundations.docs.landing

import dev.typr.foundations.And
import dev.typr.foundations.PgType
import dev.typr.foundations.PgTypes
import dev.typr.foundations.kotlin.RowParser
import dev.typr.foundations.data.Jsonb
import dev.typr.foundations.kotlin.nullable
import java.math.BigDecimal
import java.time.Instant

@Suppress("unused")
class ProductRowParser {
    data class Product(val id: ProductId, val name: String, val price: BigDecimal, val tags: Array<String>?,
                       val dimensions: Dim?, val metadata: Jsonb?, val createdAt: Instant?)
    data class ProductId(val value: Int)
    data class Dim(val width: Double, val height: Double, val depth: Double, val unit: String)
    data class Category(val id: Int, val name: String)

    val productIdType: PgType<ProductId> = PgTypes.int4.bimap(::ProductId, ProductId::value)
    val dimensionsType: PgType<Dim>? = null // placeholder
    val categoryRowParser: RowParser<Category>? = null // placeholder

    //start
    val rowParser: RowParser<Product> = RowParser.builder<Product>()
        .field(productIdType, Product::id)
        .field(PgTypes.text, Product::name)
        .field(PgTypes.numeric, Product::price)
        .field(PgTypes.textArray.nullable, Product::tags)
        .field(dimensionsType!!.nullable, Product::dimensions)
        .field(PgTypes.jsonb.nullable, Product::metadata)
        .field(PgTypes.timestamptz.nullable, Product::createdAt)
        .build(::Product)

    // Compose parsers for joins
    val joined: RowParser<And<Product, Category?>> =
        rowParser.leftJoined(categoryRowParser)
    //stop
}
