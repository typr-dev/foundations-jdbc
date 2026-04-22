package dev.typr.foundationssc.docs.core
import dev.typr.foundationssc.*
import dev.typr.foundationssc.Fragment.sql
import dev.typr.foundationssc.data.*

@SuppressWarnings(Array("unused"))
object ComposingWith:
  // start
  case class User(id: Int, name: String)
  case class Order(id: Int, userId: Int, product: String)
  case class Dashboard(userCount: Long, recentOrders: List[Order])
  case class Stats(userCount: Long, orderCount: Long, revenue: Long)

  val orderCodec: RowCodec[Order] = RowCodec
    .builder[Order]()
    .field(PgTypes.int4)(_.id)
    .field(PgTypes.int4)(_.userId)
    .field(PgTypes.text)(_.product)
    .build(Order.apply)

  var tx: Transactor = null // placeholder

  // Combine two independent queries in one transaction
  val countUsers: OperationRead[Long] =
    sql"SELECT count(*) FROM users"
      .query(RowCodec.of(PgTypes.int8).exactlyOne())
  val recentOrders: OperationRead[List[Order]] =
    sql"SELECT * FROM orders ORDER BY id DESC LIMIT 10"
      .query(orderCodec.all())

  def dashboard(): Dashboard =
    countUsers
      .combineWith(recentOrders)(Dashboard.apply)
      .transact(tx)

  // Three-way: all run in one transaction
  val countOrders: OperationRead[Long] =
    sql"SELECT count(*) FROM orders"
      .query(RowCodec.of(PgTypes.int8).exactlyOne())
  val totalRevenue: OperationRead[Long] =
    sql"SELECT coalesce(sum(amount), 0) FROM orders"
      .query(RowCodec.of(PgTypes.int8).exactlyOne())

  def stats(): Stats =
    countUsers
      .combineWith(countOrders, totalRevenue)(Stats.apply)
      .transact(tx)
  // stop
