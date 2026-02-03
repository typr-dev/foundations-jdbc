package dev.typr.foundations.docs.sqlserver

import dev.typr.foundations.SqlServerType
import dev.typr.foundations.SqlServerTypes
import dev.typr.foundations.data.Uint1

@Suppress("unused")
class IntegerTypes {
    //start
    val tinyType: SqlServerType<Uint1> = SqlServerTypes.tinyint   // Note: unsigned!
    val intType: SqlServerType<Int> = SqlServerTypes.int_
    val bigType: SqlServerType<Long> = SqlServerTypes.bigint
    //stop
}
