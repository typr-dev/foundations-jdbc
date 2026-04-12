package dev.typr.foundations.docs.oracle;

import dev.typr.foundations.OracleNestedTable;
import dev.typr.foundations.OracleType;
import dev.typr.foundations.OracleTypes;
import dev.typr.foundations.RowCodec;
import java.math.BigDecimal;
import java.util.List;

@SuppressWarnings("unused")
public class NestedTableTypes {
  // start
  // CREATE TYPE order_item_t AS OBJECT (
  //     product_name VARCHAR2(100),
  //     quantity     NUMBER(10),
  //     unit_price   NUMBER(12,2)
  // );
  record OrderItem(String productName, int quantity, BigDecimal unitPrice) {}

  static final OracleType<OrderItem> orderItemType =
      OracleTypes.compositeOf(
          "ORDER_ITEM_T",
          RowCodec.<OrderItem>namedBuilder()
              .field("PRODUCT_NAME", OracleTypes.varchar2(100), OrderItem::productName)
              .field("QUANTITY", OracleTypes.numberAsInt(10), OrderItem::quantity)
              .field("UNIT_PRICE", OracleTypes.number(12, 2), OrderItem::unitPrice)
              .build(OrderItem::new));

  // CREATE TYPE order_items_t AS TABLE OF order_item_t;
  static final OracleType<List<OrderItem>> orderItems =
      OracleNestedTable.of("ORDER_ITEMS_T", orderItemType);
  // stop
}
