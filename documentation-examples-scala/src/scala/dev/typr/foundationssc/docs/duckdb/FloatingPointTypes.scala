package dev.typr.foundationssc.docs.duckdb
import dev.typr.foundationssc.*
import dev.typr.foundationssc.data.*

@SuppressWarnings(Array("unused"))
object FloatingPointTypes:
  // start
  val floatType: DuckDbType[Float] = DuckDbTypes.float_
  val doubleType: DuckDbType[Double] = DuckDbTypes.double_
  // stop
