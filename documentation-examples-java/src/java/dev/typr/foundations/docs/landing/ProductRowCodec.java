package dev.typr.foundations.docs.landing;

import dev.typr.foundations.PgTypes;
import dev.typr.foundations.RowCodec;
import dev.typr.foundations.RowCodecNamed;
import dev.typr.foundations.Tuple;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

@SuppressWarnings("unused")
public class ProductRowCodec {
    record Product(int id, String name, BigDecimal price, Optional<Instant> createdAt) {}
    record Category(Integer id, String name) {}

    static final RowCodec<Category> categoryRowCodec =
        RowCodec.<Category>builder()
            .field(PgTypes.int4, Category::id)
            .field(PgTypes.text, Category::name)
            .build(Category::new);

    //start
    static RowCodecNamed<Product> productCodec =
        RowCodec.<Product>namedBuilder()
            .field("id", PgTypes.int4, Product::id)
            .field("name", PgTypes.text, Product::name)
            .field("price", PgTypes.numeric, Product::price)
            .field("created_at", PgTypes.timestamptz.opt(), Product::createdAt)
            .build(Product::new);

    // Compose codecs for joins
    static RowCodec<Tuple.Tuple2<Product, Optional<Category>>> joined =
        productCodec.leftJoined(categoryRowCodec);
    //stop
}
