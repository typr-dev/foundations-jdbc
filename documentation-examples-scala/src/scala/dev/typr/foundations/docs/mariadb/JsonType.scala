package dev.typr.foundations.docs.mariadb
import dev.typr.scalafoundations.*
import dev.typr.scalafoundations.data.*



@SuppressWarnings(Array("unused"))
object JsonType:
  //start
  val jsonType: MariaType[Json] = MariaTypes.json

  val data: Json = new Json("{\"name\": \"John\", \"age\": 30}")
  //stop
