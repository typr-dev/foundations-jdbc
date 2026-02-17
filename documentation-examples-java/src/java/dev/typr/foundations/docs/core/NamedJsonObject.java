package dev.typr.foundations.docs.core;

import dev.typr.foundations.*;

import java.math.BigDecimal;
import java.util.List;

@SuppressWarnings("unused")
public class NamedJsonObject {
    //start
    record OrderLine(String product, int qty, BigDecimal price) {}

    static final RowParserNamed<OrderLine> lineParser =
        RowParser.<OrderLine>namedBuilder()
            .field("product", DuckDbTypes.varchar, OrderLine::product)
            .field("qty", DuckDbTypes.integer, OrderLine::qty)
            .field("price", DuckDbTypes.decimal(10, 2), OrderLine::price)
            .build(OrderLine::new);

    // Stores rows as positional JSON arrays: [["Widget", 3, 9.99], ...]
    static final DuckDbType<List<OrderLine>> arrayType =
        DuckDbTypes.jsonArrayEncodedList(lineParser);

    // Stores rows as named JSON objects: [{"product": "Widget", "qty": 3, "price": 9.99}, ...]
    // Column names come from the parser — no redundant list to maintain
    static final DuckDbType<List<OrderLine>> objectType =
        DuckDbTypes.jsonObjectEncodedList(lineParser);
    //stop
}
