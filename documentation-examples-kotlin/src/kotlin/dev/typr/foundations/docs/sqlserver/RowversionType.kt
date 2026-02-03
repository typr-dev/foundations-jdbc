package dev.typr.foundations.docs.sqlserver

import dev.typr.foundations.SqlServerType
import dev.typr.foundations.SqlServerTypes

@Suppress("unused")
class RowversionType {
    //start
    val rowversionType: SqlServerType<ByteArray> = SqlServerTypes.rowversion
    //stop
}
