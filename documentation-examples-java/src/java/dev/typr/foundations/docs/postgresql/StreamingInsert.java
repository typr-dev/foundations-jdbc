package dev.typr.foundations.docs.postgresql;

import dev.typr.foundations.*;
import dev.typr.foundations.connect.ConnectionSource;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

@SuppressWarnings("unused")
public class StreamingInsert {

    //start:single-column
    // Insert a list of strings using COPY
    long insertNames(List<String> names, Transactor tx) throws SQLException {
        return streamingInsert
            .of("COPY users(name) FROM STDIN", 1000, names.iterator(), PgTypes.text.pgText())
            .transact(tx);
    }
    //stop:single-column

    record ProductRow(String name, BigDecimal price, int quantity) {}

    //start:multi-column
    // Define a RowParser for your row type
    static RowParser<ProductRow> productParser = RowParser.<ProductRow>builder()
        .field(PgTypes.text, ProductRow::name)
        .field(PgTypes.numeric, ProductRow::price)
        .field(PgTypes.int4, ProductRow::quantity)
        .build(ProductRow::new);

    // PgText.from() derives a text encoder from the RowParser
    static PgText<ProductRow> productText = PgText.from(productParser);

    long insertProducts(List<ProductRow> products, Transactor tx) throws SQLException {
        return streamingInsert
            .of("COPY products(name, price, quantity) FROM STDIN", 1000, products.iterator(), productText)
            .transact(tx);
    }
    //stop:multi-column
}
