package dev.typr.foundations.docs.core

import dev.typr.foundations.Fragment
import dev.typr.foundations.PgTypes
import dev.typr.foundations.kotlin.RowParser
import dev.typr.foundations.kotlin.query
import dev.typr.foundations.SqlFunction
import dev.typr.foundations.Transactor
import dev.typr.foundations.connect.ConnectionSource
import java.math.BigDecimal
import java.sql.Connection
import java.sql.SQLException

@Suppress("unused")
class TransactorSetup {
    data class ProductRow(val id: Int, val name: String, val price: BigDecimal)

    val rowParser: RowParser<ProductRow> = RowParser.builder<ProductRow>()
        .field(PgTypes.int4, ProductRow::id)
        .field(PgTypes.text, ProductRow::name)
        .field(PgTypes.numeric, ProductRow::price)
        .build(::ProductRow)

    val connectionSource: ConnectionSource? = null // placeholder
    val minPrice: BigDecimal = BigDecimal("10")

    //start
    // The Transactor manages connections and transactions
    // You choose the strategy — it handles the lifecycle
    @Throws(SQLException::class)
    fun query(): List<ProductRow> {
        val tx = connectionSource!!.transactor(Transactor.defaultStrategy())

        // Everything inside runs in one transaction: begin, commit, close
        return tx.execute(SqlFunction { conn: Connection ->
            Fragment.interpolate("SELECT * FROM product WHERE price > ")
                .param(PgTypes.numeric, minPrice)
                .done()
                .query(rowParser.all())
                .runUnchecked(conn)
        })
    }
    //stop
}
