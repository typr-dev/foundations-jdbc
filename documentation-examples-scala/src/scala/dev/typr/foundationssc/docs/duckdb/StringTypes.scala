package dev.typr.foundationssc.docs.duckdb
import dev.typr.foundationssc.*
import dev.typr.foundationssc.data.*

@SuppressWarnings(Array("unused"))
object StringTypes:
  // start
  val varcharType: DuckDbType[String] = DuckDbTypes.varchar
  val charType: DuckDbType[String] = DuckDbTypes.char_Of(10)
  // stop
