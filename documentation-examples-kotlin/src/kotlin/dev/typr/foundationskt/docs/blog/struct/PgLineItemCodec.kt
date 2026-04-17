package dev.typr.foundationskt.docs.blog.struct

import dev.typr.foundationskt.*
import java.math.BigDecimal

@Suppress("unused")
class PgLineItemCodec {
    //start
    data class LineItem(val productName: String, val quantity: Int, val unitPrice: BigDecimal)

    val lineItemCodec = RowCodec.namedBuilder<LineItem>()
        .field("product_name", PgTypes.text, LineItem::productName)
        .field("quantity", PgTypes.int4, LineItem::quantity)
        .field("unit_price", PgTypes.numeric, LineItem::unitPrice)
        .build(::LineItem)

    val lineItemType: PgType<LineItem> = PgTypes.compositeOf(lineItemCodec)
    val lineItemArrayType: PgType<List<LineItem>> = lineItemType.array()
    //stop
}
