package dev.typr.foundations.docs.sqlserver

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*

@Suppress("unused")
class RowversionType {
    //start
    val rowversionType: SqlServerType<ByteArray> = SqlServerTypes.rowversion
    //stop
}
