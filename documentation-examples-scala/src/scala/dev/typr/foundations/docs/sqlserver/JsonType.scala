package dev.typr.foundations.docs.sqlserver
import dev.typr.scalafoundations.*
import dev.typr.scalafoundations.data.*



@SuppressWarnings(Array("unused"))
object JsonType:
  //start
  val jsonType: SqlServerType[Json] = SqlServerTypes.json
  //stop
