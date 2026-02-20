package dev.typr.foundations.docs.core;

import dev.typr.foundations.And;
import dev.typr.foundations.PgTypes;
import dev.typr.foundations.RowParser;

import java.util.Optional;

@SuppressWarnings("unused")
public class ComposingParsers {
    record ProductRow(Integer id, String name) {}
    record CategoryRow(Integer id, String categoryName) {}

    RowParser<ProductRow> productParser =
        RowParser.<ProductRow>builder()
            .field(PgTypes.int4, ProductRow::id)
            .field(PgTypes.text, ProductRow::name)
            .build(ProductRow::new);

    RowParser<CategoryRow> categoryParser =
        RowParser.<CategoryRow>builder()
            .field(PgTypes.int4, CategoryRow::id)
            .field(PgTypes.text, CategoryRow::categoryName)
            .build(CategoryRow::new);

    //start
    // Inner join — both sides always present
    RowParser<And<ProductRow, CategoryRow>> innerJoined =
        productParser.joined(categoryParser);

    // Left join — right side is Optional (nullable in Kotlin, Option in Scala)
    RowParser<And<ProductRow, Optional<CategoryRow>>> leftJoined =
        productParser.leftJoined(categoryParser);
    //stop
}
