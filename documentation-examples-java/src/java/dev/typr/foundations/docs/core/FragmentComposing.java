package dev.typr.foundations.docs.core;

import dev.typr.foundations.Fragment;
import dev.typr.foundations.PgTypes;
import dev.typr.foundations.RowParser;
import dev.typr.foundations.Transactor;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@SuppressWarnings("unused")
public class FragmentComposing {
    record ProductRow(Integer id, String name, BigDecimal price) {}

    static RowParser<ProductRow> rowParser = RowParser.<ProductRow>builder()
        .field(PgTypes.int4, ProductRow::id)
        .field(PgTypes.text, ProductRow::name)
        .field(PgTypes.numeric, ProductRow::price)
        .build(ProductRow::new);

    Transactor tx = null; // placeholder
    Optional<BigDecimal> maxPrice = Optional.of(new BigDecimal("100"));

    //start
    // Build small reusable filters
    Fragment byName(String name) {
        return Fragment.of("name ILIKE ").value(PgTypes.text, name);
    }
    Fragment cheaperThan(BigDecimal max) {
        return Fragment.of("price < ").value(PgTypes.numeric, max);
    }

    // Compose dynamically — only include the filters that are present
    List<ProductRow> query() throws SQLException {
        List<Fragment> filters = Stream.of(
                Optional.of(byName("%widget%")),
                maxPrice.map(this::cheaperThan)
            )
            .flatMap(Optional::stream)
            .toList();

        return Fragment.of("SELECT * FROM product ")
            .append(Fragment.whereAnd(filters))
            .query(rowParser.all())
            .transact(tx);
    }
    //stop
}
