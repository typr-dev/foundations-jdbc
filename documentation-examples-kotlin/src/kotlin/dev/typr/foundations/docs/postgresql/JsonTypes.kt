package dev.typr.foundations.docs.postgresql

import dev.typr.kotlinfoundations.*
import dev.typr.kotlinfoundations.data.*
import dev.typr.kotlinfoundations.data.Json
import dev.typr.kotlinfoundations.data.Jsonb

@Suppress("unused")
class JsonTypes {
    //start
    val jsonType: PgType<Json> = PgTypes.json
    val jsonbType: PgType<Jsonb> = PgTypes.jsonb

    // Parse and use JSON
    val data: Json = Json("{\"name\": \"John\"}")
    //stop
}
