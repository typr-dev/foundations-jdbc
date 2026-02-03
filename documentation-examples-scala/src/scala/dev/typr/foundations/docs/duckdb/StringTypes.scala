package dev.typr.foundations.docs.duckdb

import dev.typr.foundations.{DuckDbType, DuckDbTypes}

@SuppressWarnings(Array("unused"))
object StringTypes:
  //start
  val varcharType: DuckDbType[String] = DuckDbTypes.varchar
  val charType: DuckDbType[String] = DuckDbTypes.char_(10)
  //stop
