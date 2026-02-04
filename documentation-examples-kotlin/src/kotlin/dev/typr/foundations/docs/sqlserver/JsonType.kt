package dev.typr.foundations.docs.sqlserver

import dev.typr.kotlinfoundations.*
import dev.typr.kotlinfoundations.data.*
import dev.typr.foundations.data.Json

@Suppress("unused")
class JsonType {
    //start
    val jsonType: SqlServerType<Json> = SqlServerTypes.json
    //stop
}
