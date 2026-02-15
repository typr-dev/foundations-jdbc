package dev.typr.foundations.docs.mariadb
import dev.typr.foundationssc.*
import dev.typr.foundationssc.data.*



@SuppressWarnings(Array("unused"))
object JsonType:
  //start
  val jsonType: MariaType[Json] = MariaTypes.json

  val data: Json = new Json("{\"name\": \"John\", \"age\": 30}")
  //stop
