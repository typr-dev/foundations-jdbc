package dev.typr.foundations.docs.oracle

import dev.typr.kotlinfoundations.*
import dev.typr.kotlinfoundations.data.*

@Suppress("unused")
class XmlJsonTypes {
    //start
    val xmlType: OracleType<String> = OracleTypes.xmlType
    val jsonType: OracleType<Json> = OracleTypes.json

    val data: Json = Json("{\"name\": \"Oracle\"}")
    //stop
}
