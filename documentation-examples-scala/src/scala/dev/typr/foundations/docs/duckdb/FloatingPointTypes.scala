package dev.typr.foundations.docs.duckdb

import dev.typr.foundations.{DuckDbType, DuckDbTypes}

@SuppressWarnings(Array("unused"))
object FloatingPointTypes:
  //start
  val floatType: DuckDbType[java.lang.Float] = DuckDbTypes.float_
  val doubleType: DuckDbType[java.lang.Double] = DuckDbTypes.double_
  //stop
