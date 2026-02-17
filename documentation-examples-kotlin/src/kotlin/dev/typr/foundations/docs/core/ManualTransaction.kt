package dev.typr.foundations.docs.core

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*

@Suppress("unused")
class ManualTransaction {
    data class Order(val id: Int, val userId: Int, val product: String)
    data class Dashboard(val userCount: Long, val recentOrders: List<Order>)

    val orderParser: RowParser<Order> =
        RowParser.builder<Order>()
            .field(PgTypes.int4, Order::id)
            .field(PgTypes.int4, Order::userId)
            .field(PgTypes.text, Order::product)
            .build(::Order)

    lateinit var tx: Transactor

    //start
    val countUsers: Operation<Long> =
        Sql { "SELECT count(*) FROM users" }
            .query(RowParser.of(PgTypes.int8).exactlyOne())
    val recentOrders: Operation<List<Order>> =
        Sql { "SELECT * FROM orders ORDER BY id DESC LIMIT 10" }
            .query(orderParser.all())

    // Run both in one transaction using the connection directly
    fun dashboard(): Dashboard =
        tx.transact { conn ->
            val count = countUsers.run(conn)
            val orders = recentOrders.run(conn)
            Dashboard(count, orders)
        }
    //stop
}
