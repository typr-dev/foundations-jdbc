package dev.typr.foundationssc.docs.landing
import dev.typr.foundationssc.*
import dev.typr.foundationssc.Fragment.*
import dev.typr.foundationssc.data.*

@SuppressWarnings(Array("unused"))
object SqlServerQuery:
  case class OrderRow(id: Int, name: String, price: BigDecimal)
  val orderRowCodec: RowCodec[OrderRow] = null // placeholder
  val name: Option[String] = None
  val maxPrice: Option[BigDecimal] = None
  val onlyActive: Boolean = false

  // start
  // Reusable conditional filters as Fragment extensions —
  // each wraps `.optionally().append(...)` so calls read like domain verbs.
  // Query Analysis still expands every branch at test time.
  extension (f: Fragment)
    def matchingName(n: Option[String]): Fragment =
      f.optionally(n).append(" AND name LIKE ", SqlServerTypes.nvarchar)
    def cheaperThan(max: Option[BigDecimal]): Fragment =
      f.optionally(max).append(" AND price < ", SqlServerTypes.decimal)
    def activeOnly(active: Boolean): Fragment =
      f.optionally(active).append(" AND active = 1")

  def orders(using Connection): List[OrderRow] =
    Fragment.of("SELECT id, name, price FROM orders WHERE 1 = 1")
      .matchingName(name)
      .cheaperThan(maxPrice)
      .activeOnly(onlyActive)
      .query(orderRowCodec.all())
      .run
  // stop
