package dev.typr.foundations.docs.landing

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*
import java.math.BigDecimal
import java.sql.Connection

@Suppress("unused")
class SqlServerQuery {
    data class OrderRow(val id: Int, val name: String, val price: BigDecimal)
    val orderRowCodec: RowCodec<OrderRow> =
        RowCodec.builder<OrderRow>()
            .field(SqlServerTypes.int_, OrderRow::id)
            .field(SqlServerTypes.nvarchar, OrderRow::name)
            .field(SqlServerTypes.decimal, OrderRow::price)
            .build(::OrderRow)
    val maxPrice: BigDecimal? = null
    lateinit var conn: Connection

    //start
    // Build small reusable filters - SQL Server example
    fun byName(name: String): Fragment =
        sql { "name LIKE ${SqlServerTypes.nvarchar(name)}" }

    fun cheaperThan(max: BigDecimal): Fragment =
        sql { "price < ${SqlServerTypes.decimal(max)}" }

    // Compose dynamically - only include the filters that are present
    val filters: List<Fragment> =
        listOfNotNull(
            byName("%widget%"),
            maxPrice?.let { cheaperThan(it) }
        )

    val orders: List<OrderRow> =
        sql { "SELECT * FROM orders ${Fragment.whereAnd(filters)}" }
            .query(orderRowCodec.all())
            .run(conn)
    //stop
}
