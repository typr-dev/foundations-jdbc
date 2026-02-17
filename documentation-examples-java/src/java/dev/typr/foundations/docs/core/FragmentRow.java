package dev.typr.foundations.docs.core;

import dev.typr.foundations.*;

import java.math.BigDecimal;
import java.sql.Connection;
import java.time.Instant;

@SuppressWarnings("unused")
public class FragmentRow {
    record Product(Integer id, String name, BigDecimal price, Instant createdAt) {}

    RowParserNamed<Product> productParser =
        RowParser.<Product>namedBuilder()
            .field("id", PgTypes.int4, Product::id)
            .field("name", PgTypes.text, Product::name)
            .field("price", PgTypes.numeric, Product::price)
            .field("created_at", PgTypes.timestamptz, Product::createdAt)
            .build(Product::new);

    Connection conn = null; // placeholder

    //start
    // All columns as parameters — great for app-generated IDs
    RowSqlTemplate<Product, Product> insertTemplate =
        Fragment.of("INSERT INTO product (")
            .append(productParser.columnList())
            .append(") VALUES (")
            .paramRow(productParser)
            .append(") RETURNING ")
            .append(productParser.columnList())
            .query(productParser.exactlyOne());

    Product insert(Product product) {
        return insertTemplate.on(product).run(conn);
    }

    // Skip columns handled by the database — e.g. sequences or defaults
    RowSqlTemplate<Product, Product> insertWithSequenceTemplate =
        Fragment.of("INSERT INTO product (")
            .append(productParser.columnList())
            .append(") VALUES (nextval('product_id_seq'), ")
            .paramRow(productParser, "id")
            .append(") RETURNING ")
            .append(productParser.columnList())
            .query(productParser.exactlyOne());

    Product insertWithSequence(Product product) {
        return insertWithSequenceTemplate.on(product).run(conn);
    }
    //stop
}
