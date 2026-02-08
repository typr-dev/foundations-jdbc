package dev.typr.foundations.docs.landing;

import dev.typr.foundations.*;
import dev.typr.foundations.data.Json;
import dev.typr.foundations.data.JsonValue;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

@SuppressWarnings("unused")
public class JsonCodecs {
    Transactor tx = null; // placeholder

    //start
    record OrderLine(String product, int qty, BigDecimal price) {}

    // Define once — reads from ResultSet AND JSON
    static final RowParser<OrderLine> lineParser = RowParser.<OrderLine>builder()
        .field(DuckDbTypes.varchar, OrderLine::product)
        .field(DuckDbTypes.integer, OrderLine::qty)
        .field(DuckDbTypes.decimal(10, 2), OrderLine::price)
        .build(OrderLine::new);

    // Same RowParser, now as a JSON codec — zero extra code
    static final DbJson<List<OrderLine>> linesCodec =
        DbJsonRow.jsonArray(lineParser).list();

    // Aggregate child rows as JSON in a single query
    List<OrderLine> getOrderLines(int customerId) throws SQLException {
        Json json = Fragment.interpolate(
            "SELECT json_group_array(json_array(product, qty, price)) "
            + "FROM order_lines WHERE customer_id = ")
            .param(DuckDbTypes.integer, customerId)
            .done()
            .query(RowParser.of(DuckDbTypes.json).exactlyOne())
            .transact(tx);

        return linesCodec.fromJson(JsonValue.parse(json.value()));
    }
    //stop
}
