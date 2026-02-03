package dev.typr.foundations.docs.postgresql

import dev.typr.foundations.{PgType, PgTypes}
import dev.typr.foundations.data.{Json, Jsonb}

@SuppressWarnings(Array("unused"))
object JsonTypes:
  //start
  val jsonType: PgType[Json] = PgTypes.json
  val jsonbType: PgType[Jsonb] = PgTypes.jsonb

  // Parse and use JSON
  val data: Json = new Json("{\"name\": \"John\"}")
  //stop
