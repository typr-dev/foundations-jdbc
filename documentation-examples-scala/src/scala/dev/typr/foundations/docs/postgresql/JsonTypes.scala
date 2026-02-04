package dev.typr.foundations.docs.postgresql
import dev.typr.scalafoundations.*
import dev.typr.scalafoundations.data.*



@SuppressWarnings(Array("unused"))
object JsonTypes:
  //start
  val jsonType: PgType[Json] = PgTypes.json
  val jsonbType: PgType[Jsonb] = PgTypes.jsonb

  // Parse and use JSON
  val data: Json = new Json("{\"name\": \"John\"}")
  //stop
