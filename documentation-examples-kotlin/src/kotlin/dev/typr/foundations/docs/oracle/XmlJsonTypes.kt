package dev.typr.foundations.docs.oracle

import dev.typr.foundations.OracleType
import dev.typr.foundations.OracleTypes
import dev.typr.foundations.data.Json

@Suppress("unused")
class XmlJsonTypes {
    //start
    val xmlType: OracleType<String> = OracleTypes.xmlType
    val jsonType: OracleType<Json> = OracleTypes.json

    val data: Json = Json("{\"name\": \"Oracle\"}")
    //stop
}
