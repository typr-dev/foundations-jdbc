package dev.typr.foundationskt.docs.duckdb

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*

@Suppress("unused")
class EnumType {
    //start
    // Define your Kotlin enum
    enum class Status { PENDING, ACTIVE, COMPLETED }

    // Create DuckDbType — reified, no arguments beyond the SQL type name
    val statusType: DuckDbType<Status> = DuckDbTypes.ofEnum<Status>("status")
    //stop
}
