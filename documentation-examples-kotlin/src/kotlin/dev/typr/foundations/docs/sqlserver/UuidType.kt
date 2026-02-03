package dev.typr.foundations.docs.sqlserver

import dev.typr.foundations.SqlServerType
import dev.typr.foundations.SqlServerTypes
import java.util.UUID

@Suppress("unused")
class UuidType {
    //start
    val uuidType: SqlServerType<UUID> = SqlServerTypes.uniqueidentifier
    //stop
}
