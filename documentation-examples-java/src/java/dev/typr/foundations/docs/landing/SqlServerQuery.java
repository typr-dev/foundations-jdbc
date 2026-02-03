package dev.typr.foundations.docs.landing;

import dev.typr.foundations.Fragment;
import dev.typr.foundations.RowParser;
import dev.typr.foundations.SqlServerTypes;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@SuppressWarnings("unused")
public class SqlServerQuery {
    record OrderRow(Integer id, String name, BigDecimal price) {}
    static RowParser<OrderRow> orderRowParser = null; // placeholder
    Optional<BigDecimal> maxPrice = Optional.empty();
    Connection conn = null; // placeholder

    //start
    // Build small reusable filters - SQL Server example
    Fragment byName(String name) {
        return Fragment.interpolate("name LIKE ")
            .param(SqlServerTypes.nvarchar, name).done();
    }
    Fragment cheaperThan(BigDecimal max) {
        return Fragment.interpolate("price < ")
            .param(SqlServerTypes.decimal, max).done();
    }

    // Compose dynamically - only include the filters that are present
    List<Fragment> filters = Stream.of(
            Optional.of(byName("%widget%")),
            maxPrice.map(this::cheaperThan)
        )
        .flatMap(Optional::stream)
        .toList();

    List<OrderRow> orders = Fragment.interpolate("SELECT * FROM orders ")
        .param(Fragment.whereAnd(filters)).done()
        .query(orderRowParser.all())
        .runUnchecked(conn);
    //stop
}
