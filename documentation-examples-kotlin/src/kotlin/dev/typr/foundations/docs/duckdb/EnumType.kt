package dev.typr.foundations.docs.duckdb

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*

@Suppress("unused")
class EnumType {
    //start
    // Define your Kotlin enum
    enum class Status { PENDING, ACTIVE, COMPLETED }

    // Create DuckDbType for it
    val statusType: DuckDbType<Status> = DuckDbTypes.ofEnum("status", Status::valueOf)
    //stop
}
