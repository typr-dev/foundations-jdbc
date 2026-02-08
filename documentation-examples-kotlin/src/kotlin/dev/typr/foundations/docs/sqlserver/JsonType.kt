package dev.typr.foundations.docs.sqlserver

import dev.typr.kotlinfoundations.*
import dev.typr.kotlinfoundations.data.*

@Suppress("unused")
class JsonType {
    //start
    val jsonType: SqlServerType<Json> = SqlServerTypes.json
    //stop
}
