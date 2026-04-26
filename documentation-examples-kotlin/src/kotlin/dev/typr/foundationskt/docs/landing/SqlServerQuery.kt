package dev.typr.foundationskt.docs.landing

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*
import java.math.BigDecimal

@Suppress("unused")
class SqlServerQuery {
    data class OrderRow(val id: Int, val name: String, val price: BigDecimal)
    val orderRowCodec: RowCodec<OrderRow> =
        RowCodec.builder<OrderRow>()
            .field(SqlServerTypes.int_, OrderRow::id)
            .field(SqlServerTypes.nvarchar, OrderRow::name)
            .field(SqlServerTypes.decimal, OrderRow::price)
            .build(::OrderRow)

    val name: String? = null
    val maxPrice: BigDecimal? = null
    val onlyActive: Boolean = false
    lateinit var conn: Connection

    //start
    // Reusable conditional filters as Fragment extensions —
    // each wraps `.optionally().append(...)` so calls read like domain verbs.
    // Query Analysis still expands every branch at test time.
    fun Fragment.matchingName(name: String?): Fragment =
        optionally(name).append(" AND name LIKE ", SqlServerTypes.nvarchar)

    fun Fragment.cheaperThan(max: BigDecimal?): Fragment =
        optionally(max).append(" AND price < ", SqlServerTypes.decimal)

    fun Fragment.activeOnly(active: Boolean): Fragment =
        optionally(active).append(" AND active = 1")

    val orders: List<OrderRow> =
        Fragment.of("SELECT id, name, price FROM orders WHERE 1 = 1")
            .matchingName(name)
            .cheaperThan(maxPrice)
            .activeOnly(onlyActive)
            .query(orderRowCodec.all())
            .run(conn)
    //stop
}
