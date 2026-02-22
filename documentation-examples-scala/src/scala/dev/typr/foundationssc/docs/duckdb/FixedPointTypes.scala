package dev.typr.foundationssc.docs.duckdb
import dev.typr.foundationssc.*
import dev.typr.foundationssc.data.*

@SuppressWarnings(Array("unused"))
object FixedPointTypes:
  //start
  val decimalType: DuckDbType[BigDecimal] = DuckDbTypes.decimal
  val precise: DuckDbType[BigDecimal] = DuckDbTypes.decimal(18, 6)  // DECIMAL(18,6)
  //stop
