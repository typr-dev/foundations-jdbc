package dev.typr.foundations.docs.core
import dev.typr.foundationssc.*
import dev.typr.foundationssc.Fragment.sql
import dev.typr.foundationssc.data.*


@SuppressWarnings(Array("unused"))
object TemplateThen:
  case class Order(id: Int, userId: Int, product: String)

  val orderCodec: RowCodec[Order] = RowCodec.builder[Order]()
    .field(PgTypes.int4)(_.id)
    .field(PgTypes.int4)(_.userId)
    .field(PgTypes.text)(_.product)
    .build(Order.apply)

  var tx: Transactor = null // placeholder

  //start
  // Define templates
  val insertUser: Template[String, Int] =
    sql"INSERT INTO users(name) VALUES("
      .param(PgTypes.text)
      .append(") RETURNING id")
      .query(RowCodec.of(PgTypes.int4).exactlyOne())

  val ordersByUser: Template[Int, List[Order]] =
    sql"SELECT id, user_id, product FROM orders WHERE user_id = "
      .param(PgTypes.int4)
      .query(orderCodec.all())

  // Chain: insert user, then fetch their orders
  def insertAndFetchOrders(): List[Order] =
    insertUser.on("Alice")
      .andThen(ordersByUser)
      .transact(tx)
  //stop
