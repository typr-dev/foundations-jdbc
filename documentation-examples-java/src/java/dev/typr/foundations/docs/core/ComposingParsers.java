package dev.typr.foundations.docs.core;

import dev.typr.foundations.And;
import dev.typr.foundations.PgTypes;
import dev.typr.foundations.RowParser;

import java.util.Optional;

@SuppressWarnings("unused")
public class ComposingParsers {
    //start
    record ProductRow(Integer id, String name) {}
    record CategoryRow(Integer id, String categoryName) {}

    RowParser<ProductRow> productRowParser = RowParser.<ProductRow>builder()
        .field(PgTypes.int4, ProductRow::id)
        .field(PgTypes.text, ProductRow::name)
        .build(ProductRow::new);

    RowParser<CategoryRow> categoryRowParser = RowParser.<CategoryRow>builder()
        .field(PgTypes.int4, CategoryRow::id)
        .field(PgTypes.text, CategoryRow::categoryName)
        .build(CategoryRow::new);

    RowParser<And<ProductRow, Optional<CategoryRow>>> joined =
        productRowParser.leftJoined(categoryRowParser);
    //stop
}
