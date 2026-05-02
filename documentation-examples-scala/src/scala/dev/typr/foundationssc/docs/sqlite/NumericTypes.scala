package dev.typr.foundationssc.docs.sqlite
import dev.typr.foundationssc.*

@SuppressWarnings(Array("unused"))
object NumericTypes:
  // start
  val numericType: SqliteType[BigDecimal] = SqliteTypes.numeric
  val decimalType: SqliteType[BigDecimal] = SqliteTypes.decimal
  val precise: SqliteType[BigDecimal] = SqliteTypes.decimalOf(18, 6)
  // stop
