package dev.typr.foundations.docs.sqlserver

import dev.typr.kotlinfoundations.*
import dev.typr.kotlinfoundations.data.*

@Suppress("unused")
class BoolType {
    //start
    val bitType: SqlServerType<Boolean> = SqlServerTypes.bit
    //stop
}
