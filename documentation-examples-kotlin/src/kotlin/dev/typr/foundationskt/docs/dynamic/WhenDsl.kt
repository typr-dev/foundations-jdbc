package dev.typr.foundationskt.docs.dynamic

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*
import java.math.BigDecimal

@Suppress("unused")
class WhenDsl {
    data class ProductRow(val id: Int, val name: String, val price: BigDecimal)

    val codec: RowCodec<ProductRow> =
        RowCodec.builder<ProductRow>()
            .field(PgTypes.int4, ProductRow::id)
            .field(PgTypes.text, ProductRow::name)
            .field(PgTypes.numeric, ProductRow::price)
            .build(::ProductRow)

    //start
    // Three optional filters → 2^3 = 8 SQL shapes Query Analysis verifies.
    fun search(
        namePattern: String?,
        maxPrice: BigDecimal?,
        onlyActive: Boolean
    ): OperationRead.Query<List<ProductRow>> =
        sql { "SELECT id, name, price FROM product WHERE 1 = 1" }
            .optionally(namePattern).append(" AND name ILIKE ", PgTypes.text)
            .optionally(maxPrice)  .append(" AND price < ",    PgTypes.numeric)
            .optionally(onlyActive).append(" AND active = TRUE")
            .query(codec.all())
    //stop
}
