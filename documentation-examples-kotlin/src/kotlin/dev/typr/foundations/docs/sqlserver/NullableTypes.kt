package dev.typr.foundations.docs.sqlserver

import dev.typr.kotlinfoundations.*
import dev.typr.kotlinfoundations.data.*
import java.util.Optional

@Suppress("unused")
class NullableTypes {
    //start
    val notNull: SqlServerType<Int> = SqlServerTypes.int_
    val nullable: SqlServerType<Optional<Int>> = SqlServerTypes.int_.opt()
    //stop
}
