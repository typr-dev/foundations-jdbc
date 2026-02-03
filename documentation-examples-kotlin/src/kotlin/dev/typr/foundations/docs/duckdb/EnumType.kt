package dev.typr.foundations.docs.duckdb

import dev.typr.foundations.DuckDbType
import dev.typr.foundations.DuckDbTypes

@Suppress("unused")
class EnumType {
    //start
    // Define your Kotlin enum
    enum class Status { PENDING, ACTIVE, COMPLETED }

    // Create DuckDbType for it
    val statusType: DuckDbType<Status> = DuckDbTypes.ofEnum("status", Status::valueOf)
    //stop
}
