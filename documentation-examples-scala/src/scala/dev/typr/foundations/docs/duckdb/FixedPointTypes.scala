package dev.typr.foundations.docs.duckdb

import dev.typr.foundations.{DuckDbType, DuckDbTypes}
import java.math.BigDecimal

@SuppressWarnings(Array("unused"))
object FixedPointTypes:
  //start
  val decimalType: DuckDbType[BigDecimal] = DuckDbTypes.decimal
  val precise: DuckDbType[BigDecimal] = DuckDbTypes.decimal(18, 6)  // DECIMAL(18,6)
  //stop
