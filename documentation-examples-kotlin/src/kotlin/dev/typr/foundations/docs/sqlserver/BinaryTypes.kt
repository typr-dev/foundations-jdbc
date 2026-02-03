package dev.typr.foundations.docs.sqlserver

import dev.typr.foundations.SqlServerType
import dev.typr.foundations.SqlServerTypes

@Suppress("unused")
class BinaryTypes {
    //start
    val binaryType: SqlServerType<ByteArray> = SqlServerTypes.binary(16)
    val varbinaryType: SqlServerType<ByteArray> = SqlServerTypes.varbinary(255)
    val varbinaryMax: SqlServerType<ByteArray> = SqlServerTypes.varbinaryMax
    //stop
}
