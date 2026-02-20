package dev.typr.foundationskt.docs.sqlserver

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*

@Suppress("unused")
class NullableTypes {
    //start
    val notNull: SqlServerType<Int> = SqlServerTypes.int_
    val nullable: SqlServerType<Int?> = SqlServerTypes.int_.opt()
    //stop
}
