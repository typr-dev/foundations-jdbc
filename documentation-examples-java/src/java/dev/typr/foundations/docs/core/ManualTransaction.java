package dev.typr.foundations.docs.core;

import dev.typr.foundations.Fragment;
import dev.typr.foundations.Operation;
import dev.typr.foundations.PgTypes;
import dev.typr.foundations.RowParser;
import dev.typr.foundations.Transactor;

import java.sql.SQLException;
import java.util.List;

@SuppressWarnings("unused")
public class ManualTransaction {
    record Order(int id, int userId, String product) {}
    record Dashboard(long userCount, List<Order> recentOrders) {}

    static RowParser<Order> orderParser = RowParser.<Order>builder()
        .field(PgTypes.int4, Order::id)
        .field(PgTypes.int4, Order::userId)
        .field(PgTypes.text, Order::product)
        .build(Order::new);

    Transactor tx = null; // placeholder

    //start
    Operation<Long> countUsers =
        Fragment.of("SELECT count(*) FROM users")
            .query(RowParser.of(PgTypes.int8).exactlyOne());
    Operation<List<Order>> recentOrders =
        Fragment.of("SELECT * FROM orders ORDER BY id DESC LIMIT 10")
            .query(orderParser.all());

    // Run both in one transaction using the connection directly
    Dashboard dashboard() throws SQLException {
        return tx.execute(conn -> {
            long count = countUsers.runChecked(conn);
            List<Order> orders = recentOrders.runChecked(conn);
            return new Dashboard(count, orders);
        });
    }
    //stop
}
