package dev.typr.foundations.docs.mariadb
import dev.typr.scalafoundations.*
import dev.typr.scalafoundations.data.*

@SuppressWarnings(Array("unused"))
object FixedPointTypes:
  //start
  val decimalType: MariaType[BigDecimal] = MariaTypes.decimal
  val preciseDecimal: MariaType[BigDecimal] = MariaTypes.decimal(10, 2)
  //stop
