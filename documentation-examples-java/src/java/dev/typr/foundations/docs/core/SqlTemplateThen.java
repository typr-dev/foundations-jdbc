package dev.typr.foundations.docs.core;

import dev.typr.foundations.Fragment;
import dev.typr.foundations.PgTypes;
import dev.typr.foundations.RowParser;
import dev.typr.foundations.SqlTemplate;
import dev.typr.foundations.Transactor;

import java.sql.SQLException;
import java.util.List;

@SuppressWarnings("unused")
public class SqlTemplateThen {
    record Order(int id, int userId, String product) {}

    static RowParser<Order> orderParser = RowParser.<Order>builder()
        .field(PgTypes.int4, Order::id)
        .field(PgTypes.int4, Order::userId)
        .field(PgTypes.text, Order::product)
        .build(Order::new);

    Transactor tx = null; // placeholder

    //start
    // Define templates
    SqlTemplate<String, Integer> insertUser =
        Fragment.of("INSERT INTO users(name) VALUES(")
            .param(PgTypes.text)
            .append(") RETURNING id")
            .query(RowParser.of(PgTypes.int4).exactlyOne());

    SqlTemplate<Integer, List<Order>> ordersByUser =
        Fragment.of("SELECT id, user_id, product FROM orders WHERE user_id = ")
            .param(PgTypes.int4)
            .query(orderParser.all());

    // Chain: insert user, then use returned id to fetch their orders
    List<Order> insertAndFetchOrders() throws SQLException {
        return insertUser.on("Alice")
            .then(ordersByUser)
            .transact(tx);
    }
    //stop
}
