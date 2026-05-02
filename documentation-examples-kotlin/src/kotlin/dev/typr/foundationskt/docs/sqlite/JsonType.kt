package dev.typr.foundationskt.docs.sqlite

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.Json

@Suppress("unused")
class JsonType {
    //start
    val jsonType: SqliteType<Json> = SqliteTypes.json

    val data = Json("{\"name\": \"SQLite\"}")
    //stop
}
