package dev.typr.foundationskt.docs.duckdb

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*

@Suppress("unused")
class JsonType {
    //start
    val jsonType: DuckDbType<Json> = DuckDbTypes.json

    val data: Json = Json("{\"name\": \"DuckDB\"}")
    //stop
}
