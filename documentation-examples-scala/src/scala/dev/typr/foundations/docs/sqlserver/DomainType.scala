package dev.typr.foundations.docs.sqlserver
import dev.typr.foundationssc.*
import dev.typr.foundationssc.data.*



@SuppressWarnings(Array("unused"))
object DomainType:
  //start
  // Wrapper type
  case class OrderId(value: Int)

  // Create SqlServerType from INT
  val orderIdType: SqlServerType[OrderId] =
    SqlServerTypes.int_.bimap(OrderId.apply, _.value)
  //stop
