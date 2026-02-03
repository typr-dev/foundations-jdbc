package dev.typr.foundations.docs.core

import dev.typr.foundations.Fragment
import dev.typr.foundations.PgTypes
import dev.typr.foundations.kotlin.RowParser
import dev.typr.foundations.kotlin.query
import dev.typr.foundations.Transactor
import java.math.BigDecimal
import java.sql.SQLException

@Suppress("unused")
class FragmentComposing {
    data class ProductRow(val id: Int, val name: String, val price: BigDecimal)

    val rowParser: RowParser<ProductRow> = RowParser.builder<ProductRow>()
        .field(PgTypes.int4, ProductRow::id)
        .field(PgTypes.text, ProductRow::name)
        .field(PgTypes.numeric, ProductRow::price)
        .build(::ProductRow)

    val tx: Transactor? = null // placeholder
    val maxPrice: BigDecimal? = BigDecimal("100")

    //start
    // Build small reusable filters
    fun byName(name: String): Fragment =
        Fragment.interpolate("name ILIKE ").param(PgTypes.text, name).done()

    fun cheaperThan(max: BigDecimal): Fragment =
        Fragment.interpolate("price < ").param(PgTypes.numeric, max).done()

    // Compose dynamically — only include the filters that are present
    @Throws(SQLException::class)
    fun query(): List<ProductRow> {
        val filters = listOfNotNull(
            byName("%widget%"),
            maxPrice?.let(::cheaperThan)
        )

        return Fragment.lit("SELECT * FROM product ")
            .append(Fragment.whereAnd(filters))
            .query(rowParser.all())
            .transact(tx)
    }
    //stop
}
