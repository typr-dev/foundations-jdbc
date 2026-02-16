package dev.typr.foundations.docs.core;

import dev.typr.foundations.*;

import java.math.BigDecimal;
import java.time.Instant;

@SuppressWarnings("unused")
public class NamedRowParser {
    //start
    record Product(Integer id, String name, BigDecimal price, Instant createdAt) {}

    RowParserNamed<Product> productParser = RowParser.<Product>namedBuilder()
        .field("id", PgTypes.int4, Product::id)
        .field("name", PgTypes.text, Product::name)
        .field("price", PgTypes.numeric, Product::price)
        .field("created_at", PgTypes.timestamptz, Product::createdAt)
        .build(Product::new);

    // Column list for SQL — no hand-written strings to keep in sync
    Fragment allProducts = Fragment.of("SELECT ").append(productParser.columnList()).append(" FROM product");
    //stop
}
