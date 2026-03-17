package dev.typr.foundationssc.docs.duckdb
import dev.typr.foundationssc.*
import dev.typr.foundationssc.data.*

@SuppressWarnings(Array("unused"))
object StringTypes:
  // start
  val varcharType: DuckDbType[String] = DuckDbTypes.varchar
  val charType: DuckDbType[String] = DuckDbTypes.char_(10)
  // stop
