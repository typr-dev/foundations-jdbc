package dev.typr.foundations.docs.core
import dev.typr.foundationssc.*
import dev.typr.foundationssc.data.*

import java.sql.SQLException

@SuppressWarnings(Array("unused"))
object SqlTemplateThen:
  case class Order(id: Int, userId: Int, product: String)

  val orderParser: RowParser[Order] = RowParser.builder[Order]()
    .field(PgTypes.int4)(_.id)
    .field(PgTypes.int4)(_.userId)
    .field(PgTypes.text)(_.product)
    .build(Order.apply)

  var tx: Transactor = null // placeholder

  //start
  // Define templates
  val insertUser: SqlTemplate[String, Int] =
    Fragment.of("INSERT INTO users(name) VALUES(")
      .param(PgTypes.text)
      .append(") RETURNING id")
      .query(RowParser.of(PgTypes.int4).exactlyOne())

  val ordersByUser: SqlTemplate[Int, List[Order]] =
    Fragment.of(
      "SELECT id, user_id, product FROM orders WHERE user_id = "
    ).param(PgTypes.int4)
      .query(orderParser.all())

  // Chain: insert user, then fetch their orders
  @throws[SQLException]
  def insertAndFetchOrders(): List[Order] =
    insertUser.on("Alice")
      .andThen(ordersByUser)
      .transact(tx)
  //stop
