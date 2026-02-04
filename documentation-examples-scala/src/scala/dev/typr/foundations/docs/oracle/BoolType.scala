package dev.typr.foundations.docs.oracle
import dev.typr.scalafoundations.*
import dev.typr.scalafoundations.data.*



@SuppressWarnings(Array("unused"))
object BoolType:
  //start
  val boolNative: OracleType[Boolean] = OracleTypes.boolean_        // Oracle 23c+
  val boolNumber: OracleType[Boolean] = OracleTypes.numberAsBoolean // NUMBER(1)
  //stop
