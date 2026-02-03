package dev.typr.foundations.docs.duckdb

import dev.typr.foundations.DuckDbType
import dev.typr.foundations.DuckDbTypes
import dev.typr.foundations.data.Json

@Suppress("unused")
class JsonType {
    //start
    val jsonType: DuckDbType<Json> = DuckDbTypes.json

    val data: Json = Json("{\"name\": \"DuckDB\"}")
    //stop
}
