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
    val namePattern: String? = "%widget%"
    val maxPrice: BigDecimal? = BigDecimal("100")

    //start
    // Compose dynamic filters with the `optionally` DSL. Each `.optionally().append(...)`
    // is a branch point Query Analysis verifies against the schema, even when
    // the runtime never takes that branch at this call site.
    fun query(): List<ProductRow> = tx.execute(
        sql { "SELECT id, name, price FROM product WHERE 1 = 1" }
            .optionally(namePattern).append(" AND name ILIKE ", PgTypes.text)
            .optionally(maxPrice)  .append(" AND price < ",    PgTypes.numeric)
            .query(rowCodec.all())
    )
    //stop
}
