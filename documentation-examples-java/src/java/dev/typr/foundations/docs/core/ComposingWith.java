package dev.typr.foundations.docs.core;

import dev.typr.foundations.Fragment;
import dev.typr.foundations.Operation;
import dev.typr.foundations.PgTypes;
import dev.typr.foundations.RowParser;
import dev.typr.foundations.Transactor;

import java.sql.SQLException;
import java.util.List;

@SuppressWarnings("unused")
public class ComposingWith {
    record User(int id, String name) {}
    record Order(int id, int userId, String product) {}
    record Dashboard(long userCount, List<Order> recentOrders) {}
    record Stats(long userCount, long orderCount, long revenue) {}

    static RowParser<Order> orderParser =
        RowParser.<Order>builder()
            .field(PgTypes.int4, Order::id)
            .field(PgTypes.int4, Order::userId)
            .field(PgTypes.text, Order::product)
            .build(Order::new);

    Transactor tx = null; // placeholder

    //start
    // Combine two independent queries — both run in one transaction
    Operation<Long> countUsers =
        Fragment.of("SELECT count(*) FROM users")
            .query(RowParser.of(PgTypes.int8).exactlyOne());
    Operation<List<Order>> recentOrders =
        Fragment.of("""
                SELECT * FROM orders
                ORDER BY id DESC LIMIT 10""")
            .query(orderParser.all());

    Dashboard dashboard() throws SQLException {
        return countUsers
            .with(recentOrders, Dashboard::new)
            .transact(tx);
    }

    // Three-way: all run in one transaction, results combined
    Operation<Long> countOrders =
        Fragment.of("SELECT count(*) FROM orders")
            .query(RowParser.of(PgTypes.int8).exactlyOne());
    Operation<Long> totalRevenue =
        Fragment.of("""
                SELECT coalesce(sum(amount), 0)
                FROM orders""")
            .query(RowParser.of(PgTypes.int8).exactlyOne());

    Stats stats() throws SQLException {
        return countUsers
            .with(countOrders, totalRevenue, Stats::new)
            .transact(tx);
    }
    //stop
}
