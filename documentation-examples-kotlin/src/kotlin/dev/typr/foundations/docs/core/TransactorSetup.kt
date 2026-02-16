package dev.typr.foundations.docs.core

import dev.typr.foundationskt.*
import dev.typr.foundationskt.connect.*
import dev.typr.foundationskt.data.*
import java.math.BigDecimal

@Suppress("unused")
class TransactorSetup {
    data class ProductRow(val id: Int, val name: String, val price: BigDecimal)

    val rowParser: RowParser<ProductRow> =
        RowParser.builder<ProductRow>()
            .field(PgTypes.int4, ProductRow::id)
            .field(PgTypes.text, ProductRow::name)
            .field(PgTypes.numeric, ProductRow::price)
            .build(::ProductRow)

    lateinit var connectionSource: ConnectionSource
    val minPrice: BigDecimal = BigDecimal("10")

    //start
    // The Transactor manages connections and transactions
    // You choose the strategy — it handles the lifecycle
    fun query(): List<ProductRow> {
        val tx =
            connectionSource.transactor(Transactor.defaultStrategy())

        // Everything inside runs in one transaction: begin, commit, close
        return Sql { """
            SELECT * FROM product
            WHERE price > ${PgTypes.numeric(minPrice)}
        """.trimIndent() }
            .query(rowParser.all())
            .transact(tx)
    }
    //stop
}
