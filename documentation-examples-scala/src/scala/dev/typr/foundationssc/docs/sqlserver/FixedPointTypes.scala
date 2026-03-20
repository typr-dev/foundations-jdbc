package dev.typr.foundationssc.docs.sqlserver
import dev.typr.foundationssc.*
import dev.typr.foundationssc.data.*

@SuppressWarnings(Array("unused"))
object FixedPointTypes:
  // start
  val decimalType: SqlServerType[BigDecimal] = SqlServerTypes.decimal
  val precise: SqlServerType[BigDecimal] = SqlServerTypes.decimal(18, 4)
  val moneyType: SqlServerType[BigDecimal] = SqlServerTypes.money
  // stop
