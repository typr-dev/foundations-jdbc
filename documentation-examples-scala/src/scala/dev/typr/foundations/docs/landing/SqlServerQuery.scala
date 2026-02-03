package dev.typr.foundations.docs.landing

import dev.typr.foundations.{Fragment, SqlServerTypes}
import dev.typr.foundations.scala.RowParser
import java.math.BigDecimal
import java.sql.Connection
import java.util.List as JList
import scala.jdk.CollectionConverters.*

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
  val filters: JList[Fragment] = JList.of(
    List(
      Some(byName("%widget%")),
      maxPrice.map(cheaperThan)
    ).flatten*
  )

  val orders: List[OrderRow] = Fragment.lit("SELECT * FROM orders ")
    .append(Fragment.whereAnd(filters))
    .query(orderRowParser.all().underlying)
    .runUnchecked(conn)
  //stop
