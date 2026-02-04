package dev.typr.foundations.docs.sqlserver

import dev.typr.kotlinfoundations.*
import dev.typr.kotlinfoundations.data.*

@Suppress("unused")
class RowversionType {
    //start
    val rowversionType: SqlServerType<ByteArray> = SqlServerTypes.rowversion
    //stop
}
