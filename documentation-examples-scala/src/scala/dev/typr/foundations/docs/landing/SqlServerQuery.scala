package dev.typr.foundations.docs.landing
import dev.typr.scalafoundations.*
import dev.typr.scalafoundations.Fragment.sql
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
    sql"name LIKE ${Fragment.encode(SqlServerTypes.nvarchar, name)}"

  def cheaperThan(max: BigDecimal): Fragment =
    sql"price < ${Fragment.encode(SqlServerTypes.decimal, max)}"

  // Compose dynamically - only include the filters that are present
  val filters: List[Fragment] = List(
    Some(byName("%widget%")),
    maxPrice.map(cheaperThan)
  ).flatten

  val orders: List[OrderRow] =
    sql"SELECT * FROM orders ${Fragment.whereAnd(filters)}"
      .query(orderRowParser.all())
      .run(conn)
  //stop
