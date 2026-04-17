package dev.typr.foundationssc.docs.core
import dev.typr.foundationssc.*
import dev.typr.foundationssc.Fragment.*

@SuppressWarnings(Array("unused"))
object JsonAggregation:
  var tx: Transactor = null

  case class OrderLine(product: String, qty: Int, price: BigDecimal)

  val lineCodec: RowCodec[OrderLine] =
    RowCodec
      .builder[OrderLine]()
      .field(DuckDbTypes.varchar)(_.product)
      .field(DuckDbTypes.integer)(_.qty)
      .field(DuckDbTypes.decimalOf(10, 2))(_.price)
      .build(OrderLine.apply)

  // A column type that stores rows as positional JSON arrays
  val linesType: DuckDbType[List[OrderLine]] =
    DuckDbTypes.jsonArrayEncodedList(lineCodec)

  // start
  // Aggregate child rows as JSON in a single query
  def getOrderLines(customerId: Int): List[OrderLine] =
    sql"""SELECT json_group_array(
              json_array(product, qty, price))
          FROM order_lines
          WHERE customer_id =
              ${DuckDbTypes.integer(customerId)}"""
      .query(RowCodec.of(linesType).exactlyOne())
      .transact(tx)
  // stop
