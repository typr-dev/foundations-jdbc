package dev.typr.foundations.docs.core;

import dev.typr.foundations.*;

import java.math.BigDecimal;
import java.sql.Connection;
import java.time.Instant;

@SuppressWarnings("unused")
public class FragmentRow {
    record Product(Integer id, String name, BigDecimal price, Instant createdAt) {}

    RowCodecNamed<Product> productParser =
        RowCodec.<Product>namedBuilder()
            .field("id", PgTypes.int4, Product::id)
            .field("name", PgTypes.text, Product::name)
            .field("price", PgTypes.numeric, Product::price)
            .field("created_at", PgTypes.timestamptz, Product::createdAt)
            .build(Product::new);

    Connection conn = null; // placeholder

    //start
    Product insert(Product product) {
        return Fragment.of("INSERT INTO product (")
            .append(productParser.columnList())
            .append(") VALUES (")
            .row(productParser, product)
            .append(") RETURNING ")
            .append(productParser.columnList())
            .query(productParser.exactlyOne())
            .run(conn);
    }

    // Skip columns with database defaults — pass column names to except
    Product insertWithDefault(Product product) {
        return Fragment.of("INSERT INTO product (")
            .append(productParser.columnList())
            .append(") VALUES (DEFAULT, ")
            .row(productParser, product, "id")
            .append(") RETURNING ")
            .append(productParser.columnList())
            .query(productParser.exactlyOne())
            .run(conn);
    }
    //stop
}
