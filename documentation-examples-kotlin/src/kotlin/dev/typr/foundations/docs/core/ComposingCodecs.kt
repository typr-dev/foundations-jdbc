package dev.typr.foundations.docs.core

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*

@Suppress("unused")
class ComposingCodecs {
    data class ProductRow(val id: Int, val name: String)
    data class CategoryRow(val id: Int, val categoryName: String)

    val productCodec: RowCodec<ProductRow> =
        RowCodec.builder<ProductRow>()
            .field(PgTypes.int4, ProductRow::id)
            .field(PgTypes.text, ProductRow::name)
            .build(::ProductRow)

    val categoryCodec: RowCodec<CategoryRow> =
        RowCodec.builder<CategoryRow>()
            .field(PgTypes.int4, CategoryRow::id)
            .field(PgTypes.text, CategoryRow::categoryName)
            .build(::CategoryRow)

    //start
    // Inner join — both sides always present
    val innerJoined: RowCodec<Pair<ProductRow, CategoryRow>> =
        productCodec.joined(categoryCodec)

    // Left join — right side is nullable
    val leftJoined: RowCodec<Pair<ProductRow, CategoryRow?>> =
        productCodec.leftJoined(categoryCodec)
    //stop
}
