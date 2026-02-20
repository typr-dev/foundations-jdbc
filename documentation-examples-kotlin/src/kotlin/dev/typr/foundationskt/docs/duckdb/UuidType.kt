package dev.typr.foundationskt.docs.duckdb

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*
import java.util.UUID

@Suppress("unused")
class UuidType {
    //start
    val uuidType: DuckDbType<UUID> = DuckDbTypes.uuid
    //stop
}
