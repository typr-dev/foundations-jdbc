package dev.typr.foundations.docs.postgresql
import dev.typr.scalafoundations.*
import dev.typr.scalafoundations.data.*

@SuppressWarnings(Array("unused"))
object NumericTypes:
  //start
  val intType: PgType[Int] = PgTypes.int4
  val decimalType: PgType[BigDecimal] = PgTypes.numeric
  val moneyType: PgType[Money] = PgTypes.money
  //stop
