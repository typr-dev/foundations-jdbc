package dev.typr.foundations.docs.landing
import dev.typr.foundationssc.*
import dev.typr.foundationssc.Fragment.*
import dev.typr.foundationssc.data.*

import java.sql.Connection

@SuppressWarnings(Array("unused"))
object SqlServerQuery:
  case class OrderRow(id: Int, name: String, price: BigDecimal)
  val orderRowCodec: RowCodec[OrderRow] = null // placeholder
  val maxPrice: Option[BigDecimal] = None
  val conn: Connection = null // placeholder

  //start
  // Build small reusable filters - SQL Server example
  val nvarchar = SqlServerTypes.nvarchar
  val decimal = SqlServerTypes.decimal

  def byName(name: String): Fragment =
    sql"name LIKE ${nvarchar(name)}"

  def cheaperThan(max: BigDecimal): Fragment =
    sql"price < ${decimal(max)}"

  // Compose dynamically
  val filters: List[Fragment] =
    List(
      Some(byName("%widget%")),
      maxPrice.map(cheaperThan)
    ).flatten

  val orders: List[OrderRow] =
    sql"SELECT * FROM orders ${Fragment.whereAnd(filters)}"
      .query(orderRowCodec.all())
      .run(conn)
  //stop
