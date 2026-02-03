package dev.typr.foundations.docs.sqlserver

import dev.typr.foundations.SqlServerType
import dev.typr.foundations.SqlServerTypes
import java.util.Optional

@Suppress("unused")
class NullableTypes {
    //start
    val notNull: SqlServerType<Int> = SqlServerTypes.int_
    val nullable: SqlServerType<Optional<Int>> = SqlServerTypes.int_.opt()
    //stop
}
