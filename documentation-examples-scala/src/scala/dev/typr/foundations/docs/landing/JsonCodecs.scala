package dev.typr.foundations.docs.landing
import dev.typr.foundationssc.*
import dev.typr.foundationssc.Fragment.*
import dev.typr.foundationssc.data.*

import java.sql.SQLException

@SuppressWarnings(Array("unused"))
object JsonCodecs:
  var tx: Transactor = null // placeholder

  //start
  case class OrderLine(product: String, qty: Int, price: BigDecimal)

  // Define once — reads from ResultSet AND JSON
  val lineParser: RowParser[OrderLine] =
    RowParser.builder[OrderLine]()
      .field(DuckDbTypes.varchar)(_.product)
      .field(DuckDbTypes.integer)(_.qty)
      .field(DuckDbTypes.decimal(10, 2))(_.price)
      .build(OrderLine.apply)

  // Same RowParser, now as a JSON codec — zero extra code
  val linesCodec: DbJson[List[OrderLine]] =
    lineParser.jsonArray().asList

  // Aggregate child rows as JSON in a single query
  @throws[SQLException]
  def getOrderLines(customerId: Int): List[OrderLine] =
    val json: Json =
      sql"""SELECT json_group_array(
                json_array(product, qty, price))
            FROM order_lines
            WHERE customer_id =
                ${DuckDbTypes.integer(customerId)}"""
        .query(RowParser.of(DuckDbTypes.json).exactlyOne())
        .transact(tx)

    linesCodec.fromJson(JsonValue.parse(json.value()))
  //stop
