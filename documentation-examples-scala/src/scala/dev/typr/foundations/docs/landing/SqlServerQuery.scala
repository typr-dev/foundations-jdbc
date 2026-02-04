package dev.typr.foundations.docs.landing
import dev.typr.scalafoundations.*
import dev.typr.scalafoundations.data.*

import java.sql.Connection

@SuppressWarnings(Array("unused"))
object SqlServerQuery:
  case class OrderRow(id: Int, name: String, price: BigDecimal)
  val orderRowParser: RowParser[OrderRow] = null // placeholder
  val maxPrice: Option[BigDecimal] = None
  val conn: Connection = null // placeholder

  //start
  // Build small reusable filters - SQL Server example
  def byName(name: String): Fragment =
    Fragment.interpolate("name LIKE ")
      .param(SqlServerTypes.nvarchar, name).done()

  def cheaperThan(max: BigDecimal): Fragment =
    Fragment.interpolate("price < ")
      .param(SqlServerTypes.decimal, max).done()

  // Compose dynamically - only include the filters that are present
  val filters: List[Fragment] = List(
    Some(byName("%widget%")),
    maxPrice.map(cheaperThan)
  ).flatten

  val orders: List[OrderRow] = Fragment.lit("SELECT * FROM orders ")
    .append(Fragment.whereAnd(filters))
    .query(orderRowParser.all())
    .runUnchecked(conn)
  //stop
