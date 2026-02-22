package dev.typr.foundationskt.docs.postgresql

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*

@Suppress("unused")
class JsonTypes {
    //start
    val jsonType: PgType<Json> = PgTypes.json
    val jsonbType: PgType<Jsonb> = PgTypes.jsonb

    // Parse and use JSON
    val data: Json = Json("{\"name\": \"John\"}")
    //stop
}
