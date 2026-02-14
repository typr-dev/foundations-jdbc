package dev.typr.foundations.docs.core

import dev.typr.kotlinfoundations.*
import dev.typr.kotlinfoundations.data.*

@Suppress("unused")
class ExecuteRunChecked {
    data class Order(val id: Int, val userId: Int, val product: String)
    data class Dashboard(val userCount: Long, val recentOrders: List<Order>)

    val orderParser: RowParser<Order> = RowParser.builder<Order>()
        .field(PgTypes.int4, Order::id)
        .field(PgTypes.int4, Order::userId)
        .field(PgTypes.text, Order::product)
        .build(::Order)

    lateinit var tx: Transactor

    val countUsers: Operation<Long> =
        Fragment.of("SELECT count(*) FROM users")
            .query(RowParser.of(PgTypes.int8).exactlyOne())
    val recentOrders: Operation<List<Order>> =
        Fragment.of("SELECT * FROM orders ORDER BY id DESC LIMIT 10")
            .query(orderParser.all())

    //start
    fun dashboard(): Dashboard = tx.transact { conn ->
        val count = countUsers.runChecked(conn)
        val orders = recentOrders.runChecked(conn)
        Dashboard(count, orders)
    }
    //stop
}
