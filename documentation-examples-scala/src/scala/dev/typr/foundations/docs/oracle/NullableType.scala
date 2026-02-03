package dev.typr.foundations.docs.oracle

import dev.typr.foundations.{OracleType, OracleTypes}
import java.util.Optional

@SuppressWarnings(Array("unused"))
object NullableType:
  //start
  val notNull: OracleType[Integer] = OracleTypes.numberInt
  val nullable: OracleType[Optional[Integer]] = OracleTypes.numberInt.opt()
  //stop
