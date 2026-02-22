package dev.typr.foundationssc.docs.duckdb
import dev.typr.foundationssc.*
import dev.typr.foundationssc.data.*



@SuppressWarnings(Array("unused"))
object JsonType:
  //start
  val jsonType: DuckDbType[Json] = DuckDbTypes.json

  val data: Json = new Json("{\"name\": \"DuckDB\"}")
  //stop
