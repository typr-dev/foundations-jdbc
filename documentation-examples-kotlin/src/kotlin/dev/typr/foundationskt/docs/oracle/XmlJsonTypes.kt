package dev.typr.foundationskt.docs.oracle

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*

@Suppress("unused")
class XmlJsonTypes {
    //start
    val xmlType: OracleType<String> = OracleTypes.xmlType
    val jsonType: OracleType<Json> = OracleTypes.json

    val data: Json = Json("{\"name\": \"Oracle\"}")
    //stop
}
