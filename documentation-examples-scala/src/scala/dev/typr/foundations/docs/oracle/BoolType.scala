package dev.typr.foundations.docs.oracle

import dev.typr.foundations.{OracleType, OracleTypes}

@SuppressWarnings(Array("unused"))
object BoolType:
  //start
  val boolNative: OracleType[java.lang.Boolean] = OracleTypes.boolean_        // Oracle 23c+
  val boolNumber: OracleType[java.lang.Boolean] = OracleTypes.numberAsBoolean // NUMBER(1)
  //stop
