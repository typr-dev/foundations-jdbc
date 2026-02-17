package dev.typr.foundations.docs.core

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*

@Suppress("unused")
class ComposingParsers {
    //start
    data class ProductRow(val id: Int, val name: String)
    data class CategoryRow(val id: Int, val categoryName: String)

    val productRowParser: RowParser<ProductRow> =
        RowParser.builder<ProductRow>()
            .field(PgTypes.int4, ProductRow::id)
            .field(PgTypes.text, ProductRow::name)
            .build(::ProductRow)

    val categoryRowParser: RowParser<CategoryRow> =
        RowParser.builder<CategoryRow>()
            .field(PgTypes.int4, CategoryRow::id)
            .field(PgTypes.text, CategoryRow::categoryName)
            .build(::CategoryRow)

    // leftJoined() returns nullable right side automatically
    val joined: RowParser<Pair<ProductRow, CategoryRow?>> =
        productRowParser.leftJoined(categoryRowParser)
    //stop
}
