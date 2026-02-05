package dev.typr.foundations.docs.core;

import dev.typr.foundations.Fragment;
import dev.typr.foundations.PgTypes;
import dev.typr.foundations.RowParser;
import dev.typr.foundations.Transactor;
import dev.typr.foundations.connect.ConnectionSource;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

@SuppressWarnings("unused")
public class TransactorSetup {
    record ProductRow(Integer id, String name, BigDecimal price) {}

    static RowParser<ProductRow> rowParser = RowParser.<ProductRow>builder()
        .field(PgTypes.int4, ProductRow::id)
        .field(PgTypes.text, ProductRow::name)
        .field(PgTypes.numeric, ProductRow::price)
        .build(ProductRow::new);

    ConnectionSource connectionSource = null; // placeholder
    BigDecimal minPrice = new BigDecimal("10");

    //start
    // The Transactor manages connections and transactions
    // You choose the strategy — it handles the lifecycle
    List<ProductRow> query() throws SQLException {
        Transactor tx = connectionSource.transactor(Transactor.defaultStrategy());

        // Everything inside runs in one transaction: begin, commit, close
        return Fragment.interpolate("SELECT * FROM product WHERE price > ")
            .param(PgTypes.numeric, minPrice)
            .done()
            .query(rowParser.all())
            .transact(tx);
    }
    //stop
}
