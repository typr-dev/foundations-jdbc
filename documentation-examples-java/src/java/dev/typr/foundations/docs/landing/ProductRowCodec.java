package dev.typr.foundations.docs.landing;

import dev.typr.foundations.PgStruct;
import dev.typr.foundations.PgType;
import dev.typr.foundations.PgTypes;
import dev.typr.foundations.RowCodec;
import dev.typr.foundations.Tuple;
import dev.typr.foundations.data.Jsonb;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

@SuppressWarnings("unused")
public class ProductRowCodec {
    record Product(ProductId id, String name, BigDecimal price, Optional<String[]> tags,
                   Optional<Dim> dimensions, Optional<Jsonb> metadata, Optional<Instant> createdAt) {}
    record ProductId(Integer value) {}
    record Dim(Double width, Double height, Double depth, String unit) {}
    record Category(Integer id, String name) {}

    static final PgType<ProductId> productIdType = PgTypes.int4.transform(ProductId::new, ProductId::value);
    static final PgType<Dim> dimensionsType =
        PgStruct.<Dim>builder("dimensions")
            .field("width", PgTypes.float8, Dim::width)
            .field("height", PgTypes.float8, Dim::height)
            .field("depth", PgTypes.float8, Dim::depth)
            .field("unit", PgTypes.text, Dim::unit)
            .build(Dim::new)
            .asType();

    static final RowCodec<Category> categoryRowCodec =
        RowCodec.<Category>builder()
            .field(PgTypes.int4, Category::id)
            .field(PgTypes.text, Category::name)
            .build(Category::new);

    //start
    static RowCodec<Product> rowCodec =
        RowCodec.<Product>builder()
            .field(productIdType, Product::id)
            .field(PgTypes.text, Product::name)
            .field(PgTypes.numeric, Product::price)
            .field(PgTypes.textArray.opt(), Product::tags)
            .field(dimensionsType.opt(), Product::dimensions)
            .field(PgTypes.jsonb.opt(), Product::metadata)
            .field(PgTypes.timestamptz.opt(), Product::createdAt)
            .build(Product::new);

    // Compose codecs for joins
    static RowCodec<Tuple.Tuple2<Product, Optional<Category>>> joined =
        rowCodec.leftJoined(categoryRowCodec);
    //stop
}
