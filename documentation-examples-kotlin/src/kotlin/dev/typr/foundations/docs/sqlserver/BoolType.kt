package dev.typr.foundations.docs.sqlserver

import dev.typr.foundations.SqlServerType
import dev.typr.foundations.SqlServerTypes

@Suppress("unused")
class BoolType {
    //start
    val bitType: SqlServerType<Boolean> = SqlServerTypes.bit
    //stop
}
