package dev.typr.foundations.docs.duckdb

import dev.typr.foundations.{DuckDbOptType, DuckDbType, DuckDbTypes}

@SuppressWarnings(Array("unused"))
object NullableTypes:
  //start
  val notNull: DuckDbType[Integer] = DuckDbTypes.integer
  val nullable: DuckDbOptType[Integer] = DuckDbTypes.integer.opt()
  //stop
