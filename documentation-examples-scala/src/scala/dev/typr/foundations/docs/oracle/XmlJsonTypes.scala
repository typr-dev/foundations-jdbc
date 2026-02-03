package dev.typr.foundations.docs.oracle

import dev.typr.foundations.{OracleType, OracleTypes}
import dev.typr.foundations.data.Json

@SuppressWarnings(Array("unused"))
object XmlJsonTypes:
  //start
  val xmlType: OracleType[String] = OracleTypes.xmlType
  val jsonType: OracleType[Json] = OracleTypes.json

  val data: Json = Json("{\"name\": \"Oracle\"}")
  //stop
