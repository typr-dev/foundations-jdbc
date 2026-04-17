package dev.typr.foundationskt.docs.sqlserver

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*

@Suppress("unused")
class StringTypes {
    //start
    val charType: SqlServerType<String> = SqlServerTypes.char_Of(10)
    val varcharType: SqlServerType<String> = SqlServerTypes.varcharOf(255)
    val varcharMax: SqlServerType<String> = SqlServerTypes.varcharMax
    //stop
}
