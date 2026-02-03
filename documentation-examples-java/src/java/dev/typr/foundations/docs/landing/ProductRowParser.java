package dev.typr.foundations.docs.landing;

import dev.typr.foundations.And;
import dev.typr.foundations.PgType;
import dev.typr.foundations.PgTypes;
import dev.typr.foundations.RowParser;
import dev.typr.foundations.data.Jsonb;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

@SuppressWarnings("unused")
public class ProductRowParser {
    record Product(ProductId id, String name, BigDecimal price, Optional<String[]> tags,
                   Optional<Dim> dimensions, Optional<Jsonb> metadata, Optional<Instant> createdAt) {}
    record ProductId(Integer value) {}
    record Dim(Double width, Double height, Double depth, String unit) {}
    record Category(Integer id, String name) {}

    static final PgType<ProductId> productIdType = PgTypes.int4.bimap(ProductId::new, ProductId::value);
    static final PgType<Dim> dimensionsType = null; // placeholder
    static final RowParser<Category> categoryRowParser = null; // placeholder

    //start
    static RowParser<Product> rowParser = RowParser.<Product>builder()
        .field(productIdType, Product::id)
        .field(PgTypes.text, Product::name)
        .field(PgTypes.numeric, Product::price)
        .field(PgTypes.textArray.opt(), Product::tags)
        .field(dimensionsType.opt(), Product::dimensions)
        .field(PgTypes.jsonb.opt(), Product::metadata)
        .field(PgTypes.timestamptz.opt(), Product::createdAt)
        .build(Product::new);

    // Compose parsers for joins
    static RowParser<And<Product, Optional<Category>>> joined =
        rowParser.leftJoined(categoryRowParser);
    //stop
}
