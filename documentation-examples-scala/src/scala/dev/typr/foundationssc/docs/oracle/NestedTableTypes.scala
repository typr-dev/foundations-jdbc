package dev.typr.foundationssc.docs.oracle
import dev.typr.foundations.{OracleNestedTable, OracleObject, OracleType, OracleTypes}

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
    OracleObject
      .builder[OrderItem]("ORDER_ITEM_T")
      .field("PRODUCT_NAME", OracleTypes.varchar2(100), (o: OrderItem) => o.productName)
      .field("QUANTITY", OracleTypes.numberAsInt(10), (o: OrderItem) => o.quantity)
      .field("UNIT_PRICE", OracleTypes.number(12, 2), (o: OrderItem) => o.unitPrice)
      .build((name: String, qty: Integer, price: java.math.BigDecimal) => OrderItem(name, qty, price))
      .asType()

  // CREATE TYPE order_items_t AS TABLE OF order_item_t;
  val orderItems: OracleType[java.util.List[OrderItem]] =
    OracleNestedTable.of("ORDER_ITEMS_T", orderItemType)
  // stop
