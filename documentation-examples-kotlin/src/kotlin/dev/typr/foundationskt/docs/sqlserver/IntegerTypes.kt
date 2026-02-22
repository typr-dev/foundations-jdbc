package dev.typr.foundationskt.docs.sqlserver

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*

@Suppress("unused")
class IntegerTypes {
    //start
    val tinyType: SqlServerType<Uint1> = SqlServerTypes.tinyint   // Note: unsigned!
    val intType: SqlServerType<Int> = SqlServerTypes.int_
    val bigType: SqlServerType<Long> = SqlServerTypes.bigint
    //stop
}
