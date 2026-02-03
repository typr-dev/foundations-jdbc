package dev.typr.foundations.docs.oracle

import dev.typr.foundations.{OracleType, OracleTypes}
import java.math.BigDecimal

@SuppressWarnings(Array("unused"))
object NumericTypes:
  //start
  val numberType: OracleType[BigDecimal] = OracleTypes.number
  val decimal: OracleType[BigDecimal] = OracleTypes.number(10, 2)  // NUMBER(10,2)
  val intType: OracleType[Integer] = OracleTypes.numberAsInt(9)    // NUMBER(9)
  val longType: OracleType[java.lang.Long] = OracleTypes.numberAsLong(18)  // NUMBER(18)
  //stop
