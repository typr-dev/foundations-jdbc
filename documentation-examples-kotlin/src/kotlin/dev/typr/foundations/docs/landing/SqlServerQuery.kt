package dev.typr.foundations.docs.landing

import dev.typr.foundations.Fragment
import dev.typr.foundations.kotlin.RowParser
import dev.typr.foundations.kotlin.query
import dev.typr.foundations.SqlServerTypes
import java.math.BigDecimal
import java.sql.Connection

@Suppress("unused")
class SqlServerQuery {
    data class OrderRow(val id: Int, val name: String, val price: BigDecimal)
    val orderRowParser: RowParser<OrderRow>? = null // placeholder
    val maxPrice: BigDecimal? = null
    val conn: Connection? = null // placeholder

    //start
    // Build small reusable filters - SQL Server example
    fun byName(name: String): Fragment =
        Fragment.interpolate("name LIKE ")
            .param(SqlServerTypes.nvarchar, name).done()

    fun cheaperThan(max: BigDecimal): Fragment =
        Fragment.interpolate("price < ")
            .param(SqlServerTypes.decimal, max).done()

    // Compose dynamically - only include the filters that are present
    val filters = listOfNotNull(
        byName("%widget%"),
        maxPrice?.let { cheaperThan(it) }
    )

    val orders: List<OrderRow> = Fragment.interpolate("SELECT * FROM orders ")
        .param(Fragment.whereAnd(filters)).done()
        .query(orderRowParser!!.all())
        .runUnchecked(conn)
    //stop
}
