package dev.typr.foundations.docs.sqlserver

import dev.typr.kotlinfoundations.*
import dev.typr.kotlinfoundations.data.*
import dev.typr.foundations.data.Uint1

@Suppress("unused")
class IntegerTypes {
    //start
    val tinyType: SqlServerType<Uint1> = SqlServerTypes.tinyint   // Note: unsigned!
    val intType: SqlServerType<Int> = SqlServerTypes.int_
    val bigType: SqlServerType<Long> = SqlServerTypes.bigint
    //stop
}
