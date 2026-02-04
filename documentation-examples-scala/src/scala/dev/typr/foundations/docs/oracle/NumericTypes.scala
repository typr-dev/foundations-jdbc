package dev.typr.foundations.docs.oracle
import dev.typr.scalafoundations.*
import dev.typr.scalafoundations.data.*

@SuppressWarnings(Array("unused"))
object NumericTypes:
  //start
  val numberType: OracleType[BigDecimal] = OracleTypes.number
  val decimal: OracleType[BigDecimal] = OracleTypes.number(10, 2)  // NUMBER(10,2)
  val intType: OracleType[Int] = OracleTypes.numberAsInt(9)    // NUMBER(9)
  val longType: OracleType[Long] = OracleTypes.numberAsLong(18)  // NUMBER(18)
  //stop
