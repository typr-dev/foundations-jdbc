package dev.typr.foundations.docs.oracle
import dev.typr.scalafoundations.*
import dev.typr.scalafoundations.data.*



@SuppressWarnings(Array("unused"))
object XmlJsonTypes:
  //start
  val xmlType: OracleType[String] = OracleTypes.xmlType
  val jsonType: OracleType[Json] = OracleTypes.json

  val data: Json = new Json("{\"name\": \"Oracle\"}")
  //stop
