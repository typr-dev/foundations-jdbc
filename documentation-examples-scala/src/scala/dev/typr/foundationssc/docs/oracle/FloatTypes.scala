package dev.typr.foundationssc.docs.oracle
import dev.typr.foundationssc.*
import dev.typr.foundationssc.data.*

@SuppressWarnings(Array("unused"))
object FloatTypes:
  // start
  val binaryFloat: OracleType[Float] = OracleTypes.binaryFloat
  val binaryDouble: OracleType[Double] = OracleTypes.binaryDouble
  val floatType: OracleType[Double] = OracleTypes.float_(126) // FLOAT(126)
  // stop
