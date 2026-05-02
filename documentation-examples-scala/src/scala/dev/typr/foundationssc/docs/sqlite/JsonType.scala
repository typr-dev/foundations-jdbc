package dev.typr.foundationssc.docs.sqlite
import dev.typr.foundationssc.*
import dev.typr.foundations.data.Json

@SuppressWarnings(Array("unused"))
object JsonType:
  // start
  val jsonType: SqliteType[Json] = SqliteTypes.json

  val data: Json = new Json("{\"name\": \"SQLite\"}")
  // stop
