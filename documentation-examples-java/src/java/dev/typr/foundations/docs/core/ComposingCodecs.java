package dev.typr.foundations.docs.core;

import dev.typr.foundations.And;
import dev.typr.foundations.PgTypes;
import dev.typr.foundations.RowCodec;

import java.util.Optional;

@SuppressWarnings("unused")
public class ComposingCodecs {
    record ProductRow(Integer id, String name) {}
    record CategoryRow(Integer id, String categoryName) {}

    RowCodec<ProductRow> productParser =
        RowCodec.<ProductRow>builder()
            .field(PgTypes.int4, ProductRow::id)
            .field(PgTypes.text, ProductRow::name)
            .build(ProductRow::new);

    RowCodec<CategoryRow> categoryParser =
        RowCodec.<CategoryRow>builder()
            .field(PgTypes.int4, CategoryRow::id)
            .field(PgTypes.text, CategoryRow::categoryName)
            .build(CategoryRow::new);

    //start
    // Inner join — both sides always present
    RowCodec<And<ProductRow, CategoryRow>> innerJoined =
        productParser.joined(categoryParser);

    // Left join — right side is Optional (nullable in Kotlin, Option in Scala)
    RowCodec<And<ProductRow, Optional<CategoryRow>>> leftJoined =
        productParser.leftJoined(categoryParser);
    //stop
}
