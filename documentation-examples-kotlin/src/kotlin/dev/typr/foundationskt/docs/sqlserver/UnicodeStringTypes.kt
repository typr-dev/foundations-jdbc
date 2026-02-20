package dev.typr.foundationskt.docs.sqlserver

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*

@Suppress("unused")
class UnicodeStringTypes {
    //start
    val ncharType: SqlServerType<String> = SqlServerTypes.nchar(10)
    val nvarcharType: SqlServerType<String> = SqlServerTypes.nvarchar(255)
    val nvarcharMax: SqlServerType<String> = SqlServerTypes.nvarcharMax
    //stop
}
