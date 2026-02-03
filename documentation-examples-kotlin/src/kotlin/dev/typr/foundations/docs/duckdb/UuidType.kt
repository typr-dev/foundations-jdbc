package dev.typr.foundations.docs.duckdb

import dev.typr.foundations.DuckDbType
import dev.typr.foundations.DuckDbTypes
import java.util.UUID

@Suppress("unused")
class UuidType {
    //start
    val uuidType: DuckDbType<UUID> = DuckDbTypes.uuid
    //stop
}
