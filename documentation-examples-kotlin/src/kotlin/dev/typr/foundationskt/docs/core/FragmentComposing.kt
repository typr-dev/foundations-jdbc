package dev.typr.foundationskt.docs.core

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*
import java.math.BigDecimal

@Suppress("unused")
class FragmentComposing {
    data class ProductRow(val id: Int, val name: String, val price: BigDecimal)

    val rowCodec: RowCodec<ProductRow> =
        RowCodec.builder<ProductRow>()
            .field(PgTypes.int4, ProductRow::id)
            .field(PgTypes.text, ProductRow::name)
            .field(PgTypes.numeric, ProductRow::price)
            .build(::ProductRow)

    lateinit var tx: Transactor
    val maxPrice: BigDecimal? = BigDecimal("100")

    //start
    // Build small reusable filters
    fun byName(name: String): Fragment =
        sql { "name ILIKE ${PgTypes.text(name)}" }

    fun cheaperThan(max: BigDecimal): Fragment =
        sql { "price < ${PgTypes.numeric(max)}" }

    // Compose dynamically — only include the filters that are present
    fun query(): List<ProductRow> {
        val filters = listOfNotNull(
            byName("%widget%"),
            maxPrice?.let(::cheaperThan)
        )

        return tx.execute(
            sql { "SELECT * FROM product ${Fragment.whereAnd(filters)}" }
                .query(rowCodec.all())
        )
    }
    //stop
}
