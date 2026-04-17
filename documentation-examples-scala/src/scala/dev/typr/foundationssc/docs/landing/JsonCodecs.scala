package dev.typr.foundationssc.docs.landing
import dev.typr.foundationssc.*
import dev.typr.foundationssc.Fragment.*

@SuppressWarnings(Array("unused"))
object JsonCodecs:
  var tx: Transactor = null

  case class OrderLine(product: String, qty: Int, price: BigDecimal)

  val lineCodec: RowCodec[OrderLine] =
    RowCodec
      .builder[OrderLine]()
      .field(DuckDbTypes.varchar)(_.product)
      .field(DuckDbTypes.integer)(_.qty)
      .field(DuckDbTypes.decimalOf(10, 2))(_.price)
      .build(OrderLine.apply)

  // start
  // RowCodec → JSON column type, zero extra code
  val linesType: DuckDbType[List[OrderLine]] =
    DuckDbTypes.jsonArrayEncodedList(lineCodec)

  def getOrderLines(customerId: Int): List[OrderLine] =
    sql"""SELECT json_group_array(
              json_array(product, qty, price))
          FROM order_lines
          WHERE customer_id =
              ${DuckDbTypes.integer(customerId)}"""
      .query(RowCodec.of(linesType).exactlyOne())
      .transact(tx)
  // stop
