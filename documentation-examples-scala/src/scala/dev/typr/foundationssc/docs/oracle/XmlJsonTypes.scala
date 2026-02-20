package dev.typr.foundationssc.docs.oracle
import dev.typr.foundationssc.*
import dev.typr.foundationssc.data.*



@SuppressWarnings(Array("unused"))
object XmlJsonTypes:
  //start
  val xmlType: OracleType[String] = OracleTypes.xmlType
  val jsonType: OracleType[Json] = OracleTypes.json

  val data: Json = new Json("{\"name\": \"Oracle\"}")
  //stop
