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
        return Fragment.of("name LIKE ")
            .value(SqlServerTypes.nvarchar, name);
    }
    Fragment cheaperThan(BigDecimal max) {
        return Fragment.of("price < ")
            .value(SqlServerTypes.decimal, max);
    }

    // Compose dynamically - only include the filters that are present
    List<Fragment> filters =
        Stream.of(
                Optional.of(byName("%widget%")),
                maxPrice.map(this::cheaperThan)
            )
            .flatMap(Optional::stream)
            .toList();

    List<OrderRow> orders =
        Fragment.of("SELECT * FROM orders ")
            .append(Fragment.whereAnd(filters))
            .query(orderRowParser.all())
            .run(conn);
    //stop
}
