package dev.typr.foundations.docs.postgresql

import dev.typr.foundations.{PgType, PgTypes}
import dev.typr.foundations.data.Money
import java.math.BigDecimal

@SuppressWarnings(Array("unused"))
object NumericTypes:
  //start
  val intType: PgType[Integer] = PgTypes.int4
  val decimalType: PgType[BigDecimal] = PgTypes.numeric
  val moneyType: PgType[Money] = PgTypes.money
  //stop
