package dev.typr.foundations.docs.core;

import dev.typr.foundations.Fragment;
import dev.typr.foundations.PgTypes;
import dev.typr.foundations.RowCodec;
import dev.typr.foundations.Template;
import dev.typr.foundations.Transactor;

import java.util.List;

@SuppressWarnings("unused")
public class TemplateThen {
    record Order(int id, int userId, String product) {}

    static RowCodec<Order> orderCodec =
        RowCodec.<Order>builder()
            .field(PgTypes.int4, Order::id)
            .field(PgTypes.int4, Order::userId)
            .field(PgTypes.text, Order::product)
            .build(Order::new);

    Transactor tx = null; // placeholder

    //start
    // Define templates
    Template<String, Integer> insertUser =
        Fragment.of("INSERT INTO users(name) VALUES(")
            .param(PgTypes.text)
            .append(") RETURNING id")
            .query(RowCodec.of(PgTypes.int4).exactlyOne());

    Template<Integer, List<Order>> ordersByUser =
        Fragment.of("""
                SELECT id, user_id, product
                FROM orders WHERE user_id =
                """)
            .param(PgTypes.int4)
            .query(orderCodec.all());

    // Chain: insert user, then use returned id to fetch their orders
    List<Order> insertAndFetchOrders() {
        return insertUser.on("Alice")
            .then(ordersByUser)
            .transact(tx);
    }
    //stop
}
