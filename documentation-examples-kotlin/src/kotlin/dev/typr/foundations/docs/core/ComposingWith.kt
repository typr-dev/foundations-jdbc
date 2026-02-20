package dev.typr.foundations.docs.core

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*

@Suppress("unused")
class ComposingWith {
    data class User(val id: Int, val name: String)
    data class Order(val id: Int, val userId: Int, val product: String)
    data class Dashboard(val userCount: Long, val recentOrders: List<Order>)
    data class Stats(val userCount: Long, val orderCount: Long, val revenue: Long)

    val orderParser: RowCodec<Order> =
        RowCodec.builder<Order>()
            .field(PgTypes.int4, Order::id)
            .field(PgTypes.int4, Order::userId)
            .field(PgTypes.text, Order::product)
            .build(::Order)

    lateinit var tx: Transactor

    //start
    // Combine two independent queries — both run in one transaction
    val countUsers: Operation<Long> =
        Sql { "SELECT count(*) FROM users" }
            .query(RowCodec.of(PgTypes.int8).exactlyOne())
    val recentOrders: Operation<List<Order>> =
        Sql { "SELECT * FROM orders ORDER BY id DESC LIMIT 10" }
            .query(orderParser.all())

    fun dashboard(): Dashboard =
        countUsers
            .with(recentOrders, ::Dashboard)
            .transact(tx)

    // Three-way: all run in one transaction, results combined
    val countOrders: Operation<Long> =
        Sql { "SELECT count(*) FROM orders" }
            .query(RowCodec.of(PgTypes.int8).exactlyOne())
    val totalRevenue: Operation<Long> =
        Sql { "SELECT coalesce(sum(amount), 0) FROM orders" }
            .query(RowCodec.of(PgTypes.int8).exactlyOne())

    fun stats(): Stats =
        countUsers
            .with(countOrders, totalRevenue, ::Stats)
            .transact(tx)
    //stop
}
