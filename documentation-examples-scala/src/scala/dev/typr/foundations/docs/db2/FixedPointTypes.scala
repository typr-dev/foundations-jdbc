package dev.typr.foundations.docs.db2

import dev.typr.foundations.{Db2Type, Db2Types}
import java.math.BigDecimal

@SuppressWarnings(Array("unused"))
object FixedPointTypes:
  //start
  val decType: Db2Type[BigDecimal] = Db2Types.decimal
  val preciseType: Db2Type[BigDecimal] = Db2Types.decimal(10, 2)
  val decfloatType: Db2Type[BigDecimal] = Db2Types.decfloat
  val decfloat16: Db2Type[BigDecimal] = Db2Types.decfloat(16)
  //stop
