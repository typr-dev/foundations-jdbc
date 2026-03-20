package dev.typr.foundationssc.docs.db2
import dev.typr.foundationssc.*
import dev.typr.foundationssc.data.*

@SuppressWarnings(Array("unused"))
object FixedPointTypes:
  // start
  val decType: Db2Type[BigDecimal] = Db2Types.decimal
  val preciseType: Db2Type[BigDecimal] = Db2Types.decimal(10, 2)
  val decfloatType: Db2Type[BigDecimal] = Db2Types.decfloat
  val decfloat16: Db2Type[BigDecimal] = Db2Types.decfloat(16)
  // stop
