package dev.typr.foundationssc.docs.mariadb
import dev.typr.foundationssc.*
import dev.typr.foundationssc.data.*

@SuppressWarnings(Array("unused"))
object FixedPointTypes:
  //start
  val decimalType: MariaType[BigDecimal] = MariaTypes.decimal
  val preciseDecimal: MariaType[BigDecimal] = MariaTypes.decimal(10, 2)
  //stop
