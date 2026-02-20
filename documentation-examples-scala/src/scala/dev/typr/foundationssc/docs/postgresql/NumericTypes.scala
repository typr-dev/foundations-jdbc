package dev.typr.foundationssc.docs.postgresql
import dev.typr.foundationssc.*
import dev.typr.foundationssc.data.*

@SuppressWarnings(Array("unused"))
object NumericTypes:
  //start
  val intType: PgType[Int] = PgTypes.int4
  val decimalType: PgType[BigDecimal] = PgTypes.numeric
  val moneyType: PgType[Money] = PgTypes.money
  //stop
