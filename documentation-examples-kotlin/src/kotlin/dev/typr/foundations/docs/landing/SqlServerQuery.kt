package dev.typr.foundations.docs.landing

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*
import java.math.BigDecimal
import java.sql.Connection

@Suppress("unused")
class SqlServerQuery {
    data class OrderRow(val id: Int, val name: String, val price: BigDecimal)
    val orderRowParser: RowParser<OrderRow>? = null // placeholder
    val maxPrice: BigDecimal? = null
    lateinit var conn: Connection

    //start
    // Build small reusable filters - SQL Server example
    fun byName(name: String): Fragment =
        Sql { "name LIKE ${SqlServerTypes.nvarchar(name)}" }

    fun cheaperThan(max: BigDecimal): Fragment =
        Sql { "price < ${SqlServerTypes.decimal(max)}" }

    // Compose dynamically - only include the filters that are present
    val filters: List<Fragment> =
        listOfNotNull(
            byName("%widget%"),
            maxPrice?.let { cheaperThan(it) }
        )

    val orders: List<OrderRow> =
        Sql { "SELECT * FROM orders ${Fragment.whereAnd(filters)}" }
            .query(orderRowParser!!.all())
            .run(conn)
    //stop
}
