package dev.typr.foundationssc.docs.postgresql
import dev.typr.foundationssc.*
import dev.typr.foundationssc.data.*



@SuppressWarnings(Array("unused"))
object JsonTypes:
  //start
  val jsonType: PgType[Json] = PgTypes.json
  val jsonbType: PgType[Jsonb] = PgTypes.jsonb

  // Parse and use JSON
  val data: Json = new Json("{\"name\": \"John\"}")
  //stop
