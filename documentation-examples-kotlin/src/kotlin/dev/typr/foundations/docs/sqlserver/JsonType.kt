package dev.typr.foundations.docs.sqlserver

import dev.typr.foundations.SqlServerType
import dev.typr.foundations.SqlServerTypes
import dev.typr.foundations.data.Json

@Suppress("unused")
class JsonType {
    //start
    val jsonType: SqlServerType<Json> = SqlServerTypes.json
    //stop
}
