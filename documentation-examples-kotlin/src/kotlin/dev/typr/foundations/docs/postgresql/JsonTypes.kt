package dev.typr.foundations.docs.postgresql

import dev.typr.foundations.PgType
import dev.typr.foundations.PgTypes
import dev.typr.foundations.data.Json
import dev.typr.foundations.data.Jsonb

@Suppress("unused")
class JsonTypes {
    //start
    val jsonType: PgType<Json> = PgTypes.json
    val jsonbType: PgType<Jsonb> = PgTypes.jsonb

    // Parse and use JSON
    val data: Json = Json("{\"name\": \"John\"}")
    //stop
}
