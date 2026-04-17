package dev.typr.foundationskt.docs.postgresql

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*

@Suppress("unused")
class EnumType {
    //start
    // Define your Kotlin enum
    enum class Status { PENDING, ACTIVE, COMPLETED }

    // Create a PgType — reified, no arguments beyond the SQL type name
    val statusType: PgType<Status> = PgTypes.ofEnum<Status>("status")
    //stop
}
