package dev.typr.foundations.docs.landing;

import dev.typr.foundations.*;
import java.math.BigDecimal;
import java.util.List;

@SuppressWarnings("unused")
public class JsonCodecs {
  Transactor tx = null;

  record OrderLine(String product, int qty, BigDecimal price) {}

  static final RowCodec<OrderLine> lineCodec =
      RowCodec.<OrderLine>builder()
          .field(DuckDbTypes.varchar, OrderLine::product)
          .field(DuckDbTypes.integer, OrderLine::qty)
          .field(DuckDbTypes.decimal(10, 2), OrderLine::price)
          .build(OrderLine::new);

  // start
  // RowCodec → JSON column type, zero extra code
  DuckDbType<List<OrderLine>> linesType = DuckDbTypes.jsonArrayEncodedList(lineCodec);

  List<OrderLine> getOrderLines(int customerId) {
    return Fragment.of(
            """
            SELECT json_group_array(\
            json_array(product, qty, price))
            FROM order_lines
            WHERE customer_id =\
            """)
        .value(DuckDbTypes.integer, customerId)
        .query(RowCodec.of(linesType).exactlyOne())
        .transact(tx);
  }
  // stop
}
