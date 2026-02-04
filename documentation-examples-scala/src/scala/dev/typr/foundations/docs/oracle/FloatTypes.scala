package dev.typr.foundations.docs.oracle
import dev.typr.scalafoundations.*
import dev.typr.scalafoundations.data.*



@SuppressWarnings(Array("unused"))
object FloatTypes:
  //start
  val binaryFloat: OracleType[Float] = OracleTypes.binaryFloat
  val binaryDouble: OracleType[Double] = OracleTypes.binaryDouble
  val floatType: OracleType[Double] = OracleTypes.float_(126)  // FLOAT(126)
  //stop
