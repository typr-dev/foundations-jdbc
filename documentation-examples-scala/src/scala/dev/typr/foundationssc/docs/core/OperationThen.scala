package dev.typr.foundationssc.docs.core
import dev.typr.foundationssc.*
import dev.typr.foundationssc.Fragment.sql
import dev.typr.foundationssc.data.*

@SuppressWarnings(Array("unused"))
object OperationThen:
  case class Order(id: Int, userId: Int, product: String)

  val orderCodec: RowCodec[Order] = RowCodec
    .builder[Order]()
    .field(PgTypes.int4)(_.id)
    .field(PgTypes.int4)(_.userId)
    .field(PgTypes.text)(_.product)
    .build(Order.apply)

  var tx: Transactor = null // placeholder

  // start
  // Reusable queries as methods
  def insertUser(name: String): OperationRead[Int] =
    Fragment
      .of("INSERT INTO users(name) VALUES(")
      .value(PgTypes.text, name)
      .append(") RETURNING id")
      .query(RowCodec.of(PgTypes.int4).exactlyOne())

  def ordersByUser(userId: Int): OperationRead[List[Order]] =
    Fragment
      .of("SELECT id, user_id, product FROM orders WHERE user_id = ")
      .value(PgTypes.int4, userId)
      .query(orderCodec.all())

  // Chain: insert user, then use returned id to fetch their orders.
  def insertAndFetchOrders(): List[Order] =
    insertUser("Alice").andThen(id => ordersByUser(id)).transact(tx)
  // stop
