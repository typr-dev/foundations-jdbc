package dev.typr.foundations.docs.core;

import dev.typr.foundations.Fragment;
import dev.typr.foundations.Operation;
import dev.typr.foundations.PgTypes;
import dev.typr.foundations.RowCodec;
import dev.typr.foundations.Transactor;
import java.util.List;

@SuppressWarnings("unused")
public class ManualTransaction {
  record Order(int id, int userId, String product) {}

  record Dashboard(long userCount, List<Order> recentOrders) {}

  static RowCodec<Order> orderCodec =
      RowCodec.<Order>builder()
          .field(PgTypes.int4, Order::id)
          .field(PgTypes.int4, Order::userId)
          .field(PgTypes.text, Order::product)
          .build(Order::new);

  Transactor tx = null; // placeholder

  // start
  Operation<Long> countUsers =
      Fragment.of("SELECT count(*) FROM users").query(RowCodec.of(PgTypes.int8).exactlyOne());
  Operation<List<Order>> recentOrders =
      Fragment.of(
              """
              SELECT * FROM orders
              ORDER BY id DESC LIMIT 10\
              """)
          .query(orderCodec.all());

  // Run both in one transaction using the connection directly
  Dashboard dashboard() {
    return tx.execute(
        conn -> {
          long count = countUsers.run(conn);
          List<Order> orders = recentOrders.run(conn);
          return new Dashboard(count, orders);
        });
  }
  // stop
}
