package dev.typr.foundations.docs.postgresql;

import dev.typr.foundations.*;
import dev.typr.foundations.connect.ConnectionSource;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Iterator;

@SuppressWarnings("unused")
public class StreamingInsertMulti {

    record ProductRow(String name, BigDecimal price, int quantity) {}

    //start
    // Define a RowCodec for your row type
    static RowCodec<ProductRow> productParser = RowCodec.<ProductRow>builder()
        .field(PgTypes.text, ProductRow::name)
        .field(PgTypes.numeric, ProductRow::price)
        .field(PgTypes.int4, ProductRow::quantity)
        .build(ProductRow::new);

    // PgText.from() derives a text encoder from the RowCodec
    static PgText<ProductRow> productText = PgText.from(productParser);

    long insertProducts(Iterator<ProductRow> products, Transactor tx) throws SQLException {
        return streamingInsert
            .of("COPY products(name, price, quantity) FROM STDIN", 1000, products, productText)
            .transact(tx);
    }
    //stop
}
