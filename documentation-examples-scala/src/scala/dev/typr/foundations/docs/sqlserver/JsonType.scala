package dev.typr.foundations.docs.sqlserver

import dev.typr.foundations.{SqlServerType, SqlServerTypes}
import dev.typr.foundations.data.Json

@SuppressWarnings(Array("unused"))
object JsonType:
  //start
  val jsonType: SqlServerType[Json] = SqlServerTypes.json
  //stop
