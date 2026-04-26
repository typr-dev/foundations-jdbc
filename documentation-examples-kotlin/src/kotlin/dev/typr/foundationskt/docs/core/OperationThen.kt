package dev.typr.foundationskt.docs.core

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*

@Suppress("unused")
class OperationThen {
    data class Order(val id: Int, val userId: Int, val product: String)

    val orderCodec: RowCodec<Order> =
        RowCodec.builder<Order>()
            .field(PgTypes.int4, Order::id)
            .field(PgTypes.int4, Order::userId)
            .field(PgTypes.text, Order::product)
            .build(::Order)

    lateinit var tx: Transactor

    //start
    // Reusable queries as methods
    fun insertUser(name: String): OperationRead<Int> =
        Fragment.of("INSERT INTO users(name) VALUES(")
            .value(PgTypes.text, name)
            .append(") RETURNING id")
            .query(RowCodec.of(PgTypes.int4).exactlyOne())

    fun ordersByUser(userId: Int): OperationRead<List<Order>> =
        Fragment.of("SELECT id, user_id, product FROM orders WHERE user_id = ")
            .value(PgTypes.int4, userId)
            .query(orderCodec.all())

    // Chain: insert user, then use returned id to fetch their orders.
    fun insertAndFetchOrders(): List<Order> =
        insertUser("Alice").then { id -> ordersByUser(id) }.transact(tx)
    //stop
}
