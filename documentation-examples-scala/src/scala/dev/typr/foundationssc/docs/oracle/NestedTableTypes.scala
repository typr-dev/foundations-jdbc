package dev.typr.foundationssc.docs.oracle
import dev.typr.foundations.{OracleNestedTable, OracleType, OracleTypes, RowCodec}

@SuppressWarnings(Array("unused"))
object NestedTableTypes:
  // start
  // CREATE TYPE order_item_t AS OBJECT (
  //     product_name VARCHAR2(100),
  //     quantity     NUMBER(10),
  //     unit_price   NUMBER(12,2)
  // );
  case class OrderItem(productName: String, quantity: Integer, unitPrice: java.math.BigDecimal)

  val orderItemType: OracleType[OrderItem] =
    OracleTypes.compositeOf(
      "ORDER_ITEM_T",
      RowCodec
        .namedBuilder[OrderItem]()
        .field("PRODUCT_NAME", OracleTypes.varchar2Of(100), (o: OrderItem) => o.productName)
        .field("QUANTITY", OracleTypes.numberAsInt(10), (o: OrderItem) => o.quantity)
        .field("UNIT_PRICE", OracleTypes.numberOf(12, 2), (o: OrderItem) => o.unitPrice)
        .build((name: String, qty: Integer, price: java.math.BigDecimal) => OrderItem(name, qty, price))
    )

  // CREATE TYPE order_items_t AS TABLE OF order_item_t;
  val orderItems: OracleType[java.util.List[OrderItem]] =
    OracleNestedTable.of("ORDER_ITEMS_T", orderItemType)
  // stop
