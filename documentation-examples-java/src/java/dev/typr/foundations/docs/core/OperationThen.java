package dev.typr.foundations.docs.core;

import dev.typr.foundations.Fragment;
import dev.typr.foundations.OperationRead;
import dev.typr.foundations.PgTypes;
import dev.typr.foundations.RowCodec;
import dev.typr.foundations.Transactor;
import java.util.List;

@SuppressWarnings("unused")
public class OperationThen {
  record Order(int id, int userId, String product) {}

  static RowCodec<Order> orderCodec =
      RowCodec.<Order>builder()
          .field(PgTypes.int4, Order::id)
          .field(PgTypes.int4, Order::userId)
          .field(PgTypes.text, Order::product)
          .build(Order::new);

  Transactor tx = null; // placeholder

  // start
  // Reusable queries as methods
  OperationRead<Integer> insertUser(String name) {
    return Fragment.of("INSERT INTO users(name) VALUES(")
        .value(PgTypes.text, name)
        .append(") RETURNING id")
        .query(RowCodec.of(PgTypes.int4).exactlyOne());
  }

  OperationRead<List<Order>> ordersByUser(int userId) {
    return Fragment.of(
            """
            SELECT id, user_id, product
            FROM orders WHERE user_id =
            """)
        .value(PgTypes.int4, userId)
        .query(orderCodec.all());
  }

  // Chain: insert user, then use returned id to fetch their orders.
  // .then(fn) flat-maps the result of the first operation into the second.
  List<Order> insertAndFetchOrders() {
    return tx.execute(insertUser("Alice").then(this::ordersByUser));
  }
  // stop
}
