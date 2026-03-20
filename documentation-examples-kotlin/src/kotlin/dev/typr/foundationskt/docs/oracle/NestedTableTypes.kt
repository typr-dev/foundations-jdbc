package dev.typr.foundationskt.docs.oracle

import dev.typr.foundations.OracleNestedTable
import dev.typr.foundations.OracleObject
import dev.typr.foundations.OracleType
import dev.typr.foundations.OracleTypes
import java.math.BigDecimal

@Suppress("unused")
class NestedTableTypes {
    //start
    // CREATE TYPE order_item_t AS OBJECT (
    //     product_name VARCHAR2(100),
    //     quantity     NUMBER(10),
    //     unit_price   NUMBER(12,2)
    // );
    data class OrderItem(val productName: String, val quantity: Int, val unitPrice: BigDecimal)

    val orderItemType: OracleType<OrderItem> =
        OracleObject.builder<OrderItem>("ORDER_ITEM_T")
            .field("PRODUCT_NAME", OracleTypes.varchar2(100), OrderItem::productName)
            .field("QUANTITY", OracleTypes.numberAsInt(10), OrderItem::quantity)
            .field("UNIT_PRICE", OracleTypes.number(12, 2), OrderItem::unitPrice)
            .build(::OrderItem)
            .asType()

    // CREATE TYPE order_items_t AS TABLE OF order_item_t;
    val orderItems: OracleType<List<OrderItem>> =
        OracleNestedTable.of("ORDER_ITEMS_T", orderItemType)
    //stop
}
