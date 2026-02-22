package dev.typr.foundationskt.docs.postgresql

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*

@Suppress("unused")
class EnumType {
    //start
    // Define your Kotlin enum
    enum class Status { PENDING, ACTIVE, COMPLETED }

    // Create a PgType for it
    val statusType: PgType<Status> = PgTypes.ofEnum("status", Status::valueOf)
    //stop
}
