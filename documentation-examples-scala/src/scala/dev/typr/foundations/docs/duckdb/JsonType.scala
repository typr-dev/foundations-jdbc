package dev.typr.foundations.docs.duckdb
import dev.typr.scalafoundations.*
import dev.typr.scalafoundations.data.*



@SuppressWarnings(Array("unused"))
object JsonType:
  //start
  val jsonType: DuckDbType[Json] = DuckDbTypes.json

  val data: Json = new Json("{\"name\": \"DuckDB\"}")
  //stop
