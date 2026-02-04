package dev.typr.foundations.docs.sqlserver

import dev.typr.kotlinfoundations.*
import dev.typr.kotlinfoundations.data.*

@Suppress("unused")
class StringTypes {
    //start
    val charType: SqlServerType<String> = SqlServerTypes.char_(10)
    val varcharType: SqlServerType<String> = SqlServerTypes.varchar(255)
    val varcharMax: SqlServerType<String> = SqlServerTypes.varcharMax
    //stop
}
