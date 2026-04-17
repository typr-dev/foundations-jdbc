package dev.typr.foundationskt.docs.sqlserver

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*

@Suppress("unused")
class BinaryTypes {
    //start
    val binaryType: SqlServerType<ByteArray> = SqlServerTypes.binaryOf(16)
    val varbinaryType: SqlServerType<ByteArray> = SqlServerTypes.varbinaryOf(255)
    val varbinaryMax: SqlServerType<ByteArray> = SqlServerTypes.varbinaryMax
    //stop
}
