package dev.typr.foundations.docs.duckdb

import dev.typr.foundations.{DuckDbType, DuckDbTypes}

@SuppressWarnings(Array("unused"))
object BoolType:
  //start
  val boolType: DuckDbType[java.lang.Boolean] = DuckDbTypes.boolean_
  //stop
