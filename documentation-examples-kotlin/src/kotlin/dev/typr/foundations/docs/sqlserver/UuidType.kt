package dev.typr.foundations.docs.sqlserver

import dev.typr.kotlinfoundations.*
import dev.typr.kotlinfoundations.data.*
import java.util.UUID

@Suppress("unused")
class UuidType {
    //start
    val uuidType: SqlServerType<UUID> = SqlServerTypes.uniqueidentifier
    //stop
}
