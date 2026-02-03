package dev.typr.foundations.docs.duckdb

import dev.typr.foundations.{DuckDbType, DuckDbTypes}
import dev.typr.foundations.data.Json

@SuppressWarnings(Array("unused"))
object JsonType:
  //start
  val jsonType: DuckDbType[Json] = DuckDbTypes.json

  val data: Json = Json("{\"name\": \"DuckDB\"}")
  //stop
