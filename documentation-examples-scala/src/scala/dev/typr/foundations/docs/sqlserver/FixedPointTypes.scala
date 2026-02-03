package dev.typr.foundations.docs.sqlserver

import dev.typr.foundations.{SqlServerType, SqlServerTypes}
import java.math.BigDecimal

@SuppressWarnings(Array("unused"))
object FixedPointTypes:
  //start
  val decimalType: SqlServerType[BigDecimal] = SqlServerTypes.decimal
  val precise: SqlServerType[BigDecimal] = SqlServerTypes.decimal(18, 4)
  val moneyType: SqlServerType[BigDecimal] = SqlServerTypes.money
  //stop
