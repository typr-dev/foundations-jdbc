package dev.typr.foundationskt.docs.core

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*

@Suppress("unused")
class ExecuteComposed {
    data class Order(val id: Int, val userId: Int, val product: String)
    data class Dashboard(val userCount: Long, val recentOrders: List<Order>)

    val orderCodec: RowCodec<Order> =
        RowCodec.builder<Order>()
            .field(PgTypes.int4, Order::id)
            .field(PgTypes.int4, Order::userId)
            .field(PgTypes.text, Order::product)
            .build(::Order)

    lateinit var tx: Transactor

    val countUsers: OperationRead<Long> =
        sql { "SELECT count(*) FROM users" }
            .query(RowCodec.of(PgTypes.int8).exactlyOne())
    val recentOrders: OperationRead<List<Order>> =
        sql { "SELECT * FROM orders ORDER BY id DESC LIMIT 10" }
            .query(orderCodec.all())

    //start
    fun dashboard(): Dashboard =
        tx.execute(countUsers.combineWith(recentOrders, ::Dashboard))
    //stop
}
