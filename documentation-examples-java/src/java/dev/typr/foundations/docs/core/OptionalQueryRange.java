package dev.typr.foundations.docs.core;

import dev.typr.foundations.Fragment;
import dev.typr.foundations.PgTypes;
import dev.typr.foundations.RowParser;
import dev.typr.foundations.SqlTemplate;
import dev.typr.foundations.Transactor;
import dev.typr.foundations.Tuple;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
public class OptionalQueryRange {
    record Product(int id, String name, BigDecimal price) {}

    static RowParser<Product> productParser =
        RowParser.<Product>builder()
            .field(PgTypes.int4, Product::id)
            .field(PgTypes.text, Product::name)
            .field(PgTypes.numeric, Product::price)
            .build(Product::new);

    Transactor tx = null; // placeholder

    //start
    // When an optional clause needs multiple parameters,
    // pass a multi-parameter builder.
    // The grouped parameters are provided or omitted together.
    SqlTemplate<Optional<Tuple.Tuple2<BigDecimal, BigDecimal>>, List<Product>>
        byPriceRange = Fragment.of("""
                SELECT id, name, price FROM products WHERE 1=1
                """)
            .optionally(
                Fragment.of(" AND price BETWEEN ")
                    .param(PgTypes.numeric)
                    .append(" AND ")
                    .param(PgTypes.numeric))
            .query(productParser.all());

    // With range
    List<Product> inRange() throws SQLException {
        return byPriceRange
            .on(Optional.of(Tuple.of(
                new BigDecimal("10"), new BigDecimal("50"))))
            .transact(tx);
    }

    // Without range — returns all products
    List<Product> all() throws SQLException {
        return byPriceRange.on(Optional.empty()).transact(tx);
    }
    //stop
}
