package dev.typr.foundationssc.docs.oracle
import dev.typr.foundationssc.*
import dev.typr.foundationssc.data.*

@SuppressWarnings(Array("unused"))
object NumericTypes:
  // start
  val numberType: OracleType[BigDecimal] = OracleTypes.number
  val decimal: OracleType[BigDecimal] = OracleTypes.numberOf(10, 2) // NUMBER(10,2)
  val intType: OracleType[Int] = OracleTypes.numberAsInt(9) // NUMBER(9)
  val longType: OracleType[Long] = OracleTypes.numberAsLong(18) // NUMBER(18)
  // stop
