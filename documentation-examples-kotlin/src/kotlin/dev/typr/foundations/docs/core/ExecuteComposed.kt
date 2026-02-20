package dev.typr.foundations.docs.core

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*

@Suppress("unused")
class ExecuteComposed {
    data class Order(val id: Int, val userId: Int, val product: String)
    data class Dashboard(val userCount: Long, val recentOrders: List<Order>)

    val orderParser: RowCodec<Order> =
        RowCodec.builder<Order>()
            .field(PgTypes.int4, Order::id)
            .field(PgTypes.int4, Order::userId)
            .field(PgTypes.text, Order::product)
            .build(::Order)

    lateinit var tx: Transactor

    val countUsers: Operation<Long> =
        Sql { "SELECT count(*) FROM users" }
            .query(RowCodec.of(PgTypes.int8).exactlyOne())
    val recentOrders: Operation<List<Order>> =
        Sql { "SELECT * FROM orders ORDER BY id DESC LIMIT 10" }
            .query(orderParser.all())

    //start
    fun dashboard(): Dashboard = tx.transact { conn ->
        countUsers.with(recentOrders, ::Dashboard).run(conn)
    }
    //stop
}
