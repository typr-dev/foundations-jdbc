package dev.typr.foundations.docs.core

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*

@Suppress("unused")
class ComposingParsers {
    data class ProductRow(val id: Int, val name: String)
    data class CategoryRow(val id: Int, val categoryName: String)

    val productParser: RowParser<ProductRow> =
        RowParser.builder<ProductRow>()
            .field(PgTypes.int4, ProductRow::id)
            .field(PgTypes.text, ProductRow::name)
            .build(::ProductRow)

    val categoryParser: RowParser<CategoryRow> =
        RowParser.builder<CategoryRow>()
            .field(PgTypes.int4, CategoryRow::id)
            .field(PgTypes.text, CategoryRow::categoryName)
            .build(::CategoryRow)

    //start
    // Inner join — both sides always present
    val innerJoined: RowParser<Pair<ProductRow, CategoryRow>> =
        productParser.joined(categoryParser)

    // Left join — right side is nullable
    val leftJoined: RowParser<Pair<ProductRow, CategoryRow?>> =
        productParser.leftJoined(categoryParser)
    //stop
}
