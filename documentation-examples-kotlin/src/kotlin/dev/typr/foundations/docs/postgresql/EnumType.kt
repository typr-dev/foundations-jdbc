package dev.typr.foundations.docs.postgresql

import dev.typr.foundations.PgType
import dev.typr.foundations.PgTypes

@Suppress("unused")
class EnumType {
    //start
    // Define your Kotlin enum
    enum class Status { PENDING, ACTIVE, COMPLETED }

    // Create a PgType for it
    val statusType: PgType<Status> = PgTypes.ofEnum("status", Status::valueOf)
    //stop
}
