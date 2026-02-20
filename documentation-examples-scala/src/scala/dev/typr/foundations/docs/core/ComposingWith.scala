package dev.typr.foundations.docs.core
import dev.typr.foundationssc.*
import dev.typr.foundationssc.Fragment.sql
import dev.typr.foundationssc.data.*


@SuppressWarnings(Array("unused"))
object ComposingWith:
  case class User(id: Int, name: String)
  case class Order(id: Int, userId: Int, product: String)
  case class Dashboard(userCount: Long, recentOrders: List[Order])
  case class Stats(userCount: Long, orderCount: Long, revenue: Long)

  val orderParser: RowCodec[Order] = RowCodec.builder[Order]()
    .field(PgTypes.int4)(_.id)
    .field(PgTypes.int4)(_.userId)
    .field(PgTypes.text)(_.product)
    .build(Order.apply)

  var tx: Transactor = null // placeholder

  //start
  // Combine two independent queries in one transaction
  val countUsers: Operation[Long] =
    sql"SELECT count(*) FROM users"
      .query(RowCodec.of(PgTypes.int8).exactlyOne())
  val recentOrders: Operation[List[Order]] =
    sql"SELECT * FROM orders ORDER BY id DESC LIMIT 10"
      .query(orderParser.all())

  def dashboard(): Dashboard =
    countUsers.`with`(recentOrders)(Dashboard.apply)
      .transact(tx)

  // Three-way: all run in one transaction
  val countOrders: Operation[Long] =
    sql"SELECT count(*) FROM orders"
      .query(RowCodec.of(PgTypes.int8).exactlyOne())
  val totalRevenue: Operation[Long] =
    sql"SELECT coalesce(sum(amount), 0) FROM orders"
      .query(RowCodec.of(PgTypes.int8).exactlyOne())

  def stats(): Stats =
    countUsers
      .`with`(countOrders, totalRevenue)(Stats.apply)
      .transact(tx)
  //stop
