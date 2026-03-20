package dev.typr.foundationssc.docs.mariadb
import dev.typr.foundationssc.*
import dev.typr.foundationssc.data.*

@SuppressWarnings(Array("unused"))
object FloatingPointTypes:
  // start
  val floatType: MariaType[Float] = MariaTypes.float_
  val doubleType: MariaType[Double] = MariaTypes.double_
  // stop
