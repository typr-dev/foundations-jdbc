package dev.typr.foundations.docs.core
import dev.typr.foundationssc.*
import dev.typr.foundationssc.Fragment.sql
import dev.typr.foundationssc.data.*


@SuppressWarnings(Array("unused"))
object ExecuteComposed:
  case class Order(id: Int, userId: Int, product: String)
  case class Dashboard(userCount: Long, recentOrders: List[Order])

  val orderParser: RowParser[Order] = RowParser.builder[Order]()
    .field(PgTypes.int4)(_.id)
    .field(PgTypes.int4)(_.userId)
    .field(PgTypes.text)(_.product)
    .build(Order.apply)

  var tx: Transactor = null // placeholder

  val countUsers: Operation[Long] =
    sql"SELECT count(*) FROM users"
      .query(RowParser.of(PgTypes.int8).exactlyOne())
  val recentOrders: Operation[List[Order]] =
    sql"SELECT * FROM orders ORDER BY id DESC LIMIT 10"
      .query(orderParser.all())

  //start
  def dashboard(): Dashboard = tx.transact { conn =>
    countUsers.`with`(recentOrders)(Dashboard.apply)
      .run(conn)
  }
  //stop
