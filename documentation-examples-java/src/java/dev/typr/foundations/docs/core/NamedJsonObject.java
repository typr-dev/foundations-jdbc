package dev.typr.foundations.docs.core;

import dev.typr.foundations.*;
import dev.typr.foundations.data.Json;
import dev.typr.foundations.data.JsonValue;

import java.math.BigDecimal;
import java.util.List;

@SuppressWarnings("unused")
public class NamedJsonObject {
    //start
    record OrderLine(String product, int qty, BigDecimal price) {}

    static final RowParserNamed<OrderLine> lineParser = RowParser.<OrderLine>namedBuilder()
        .field("product", DuckDbTypes.varchar, OrderLine::product)
        .field("qty", DuckDbTypes.integer, OrderLine::qty)
        .field("price", DuckDbTypes.decimal(10, 2), OrderLine::price)
        .build(OrderLine::new);

    // JSON array codec — positional: [["Widget", 3, 9.99], ...]
    static final DbJson<List<OrderLine>> arrayCodec =
        DbJsonRow.jsonArray(lineParser).list();

    // JSON object codec — named: [{"product": "Widget", "qty": 3, "price": 9.99}, ...]
    // Column names come from the parser — no redundant list to maintain
    static final DbJson<List<OrderLine>> objectCodec =
        DbJsonRow.jsonObject(lineParser).list();
    //stop
}
