package dev.typr.foundationskt.docs.dynamic

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*
import java.math.BigDecimal

@Suppress("unused")
class ListBased {
    data class ProductRow(val id: Int, val name: String, val price: BigDecimal)

    val codec: RowCodec<ProductRow> =
        RowCodec.builder<ProductRow>()
            .field(PgTypes.int4, ProductRow::id)
            .field(PgTypes.text, ProductRow::name)
            .field(PgTypes.numeric, ProductRow::price)
            .build(::ProductRow)

    //start
    // Build a list at runtime, then join with `Fragment.whereAnd`. Query Analysis
    // sees only the SQL shape constructed at scan time — runtime variants that
    // the test never built are not checked.
    fun search(
        namePattern: String?,
        maxPrice: BigDecimal?
    ): OperationRead.Query<List<ProductRow>> {
        val filters: List<Fragment> = listOfNotNull(
            namePattern?.let { sql { "name ILIKE ${PgTypes.text(it)}" } },
            maxPrice  ?.let { sql { "price < ${PgTypes.numeric(it)}" } }
        )
        return sql { "SELECT id, name, price FROM product ${Fragment.whereAnd(filters)}" }
            .query(codec.all())
    }
    //stop
}
