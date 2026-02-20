package dev.typr.foundationskt.docs.sqlserver

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*
import java.util.UUID

@Suppress("unused")
class UuidType {
    //start
    val uuidType: SqlServerType<UUID> = SqlServerTypes.uniqueidentifier
    //stop
}
