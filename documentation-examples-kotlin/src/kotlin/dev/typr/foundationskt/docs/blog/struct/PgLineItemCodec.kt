package dev.typr.foundationskt.docs.blog.struct

import dev.typr.foundationskt.*
import java.math.BigDecimal

@Suppress("unused")
class PgLineItemCodec {
    //start
    data class LineItem(val productName: String, val quantity: Int, val unitPrice: BigDecimal)

    val lineItemStruct = PgStruct.builder<LineItem>("line_item")
        .field("product_name", PgTypes.text, LineItem::productName)
        .field("quantity", PgTypes.int4, LineItem::quantity)
        .field("unit_price", PgTypes.numeric, LineItem::unitPrice)
        .build(::LineItem)

    val lineItemType: PgType<LineItem> = lineItemStruct.asType()
    val lineItemArrayType: PgType<Array<LineItem>> = lineItemStruct.asArrayType { arrayOfNulls<LineItem>(it) }
    //stop
}
