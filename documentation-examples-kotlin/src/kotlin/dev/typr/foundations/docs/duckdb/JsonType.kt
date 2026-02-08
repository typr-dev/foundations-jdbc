package dev.typr.foundations.docs.duckdb

import dev.typr.kotlinfoundations.*
import dev.typr.kotlinfoundations.data.*

@Suppress("unused")
class JsonType {
    //start
    val jsonType: DuckDbType<Json> = DuckDbTypes.json

    val data: Json = Json("{\"name\": \"DuckDB\"}")
    //stop
}
