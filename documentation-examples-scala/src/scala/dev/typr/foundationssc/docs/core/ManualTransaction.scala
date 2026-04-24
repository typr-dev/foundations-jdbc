package dev.typr.foundationssc.docs.core
import dev.typr.foundationssc.*
import dev.typr.foundationssc.Fragment.sql
import dev.typr.foundationssc.data.*

@SuppressWarnings(Array("unused"))
object ManualTransaction:
  case class Order(id: Int, userId: Int, product: String)
  case class Dashboard(userCount: Long, recentOrders: List[Order])

  val orderCodec: RowCodec[Order] = RowCodec
    .builder[Order]()
    .field(PgTypes.int4)(_.id)
    .field(PgTypes.int4)(_.userId)
    .field(PgTypes.text)(_.product)
    .build(Order.apply)

  var tx: Transactor = null // placeholder

  // start
  val countUsers: OperationRead[Long] =
    sql"SELECT count(*) FROM users"
      .query(RowCodec.of(PgTypes.int8).exactlyOne())
  val recentOrders: OperationRead[List[Order]] =
    sql"SELECT * FROM orders ORDER BY id DESC LIMIT 10"
      .query(orderCodec.all())

  // Run both in one transaction using a transact block
  // Connection is available implicitly inside transact { }
  def dashboard(): Dashboard = tx.transact {
    val count = countUsers.run
    val orders = recentOrders.run
    Dashboard(count, orders)
  }
  // stop
