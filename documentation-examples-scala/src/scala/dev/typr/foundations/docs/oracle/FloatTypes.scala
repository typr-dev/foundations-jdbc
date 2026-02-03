package dev.typr.foundations.docs.oracle

import dev.typr.foundations.{OracleType, OracleTypes}

@SuppressWarnings(Array("unused"))
object FloatTypes:
  //start
  val binaryFloat: OracleType[java.lang.Float] = OracleTypes.binaryFloat
  val binaryDouble: OracleType[java.lang.Double] = OracleTypes.binaryDouble
  val floatType: OracleType[java.lang.Double] = OracleTypes.float_(126)  // FLOAT(126)
  //stop
