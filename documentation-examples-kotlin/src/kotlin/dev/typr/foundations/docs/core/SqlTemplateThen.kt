package dev.typr.foundations.docs.core

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*

@Suppress("unused")
class SqlTemplateThen {
    data class Order(val id: Int, val userId: Int, val product: String)

    val orderParser: RowParser<Order> =
        RowParser.builder<Order>()
            .field(PgTypes.int4, Order::id)
            .field(PgTypes.int4, Order::userId)
            .field(PgTypes.text, Order::product)
            .build(::Order)

    lateinit var tx: Transactor

    //start
    // Define templates
    val insertUser: SqlTemplate<String, Int> =
        Fragment.of("INSERT INTO users(name) VALUES(")
            .param(PgTypes.text)
            .append(") RETURNING id")
            .query(RowParser.of(PgTypes.int4).exactlyOne())

    val ordersByUser: SqlTemplate<Int, List<Order>> =
        Fragment.of("""
            SELECT id, user_id, product
            FROM orders
            WHERE user_id = """.trimIndent())
            .param(PgTypes.int4)
            .query(orderParser.all())

    // Chain: insert user, then use returned id to fetch their orders
    fun insertAndFetchOrders(): List<Order> =
        insertUser.on("Alice")
            .then(ordersByUser)
            .transact(tx)
    //stop
}
