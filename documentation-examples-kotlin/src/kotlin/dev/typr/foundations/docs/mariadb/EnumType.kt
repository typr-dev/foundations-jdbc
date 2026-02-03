package dev.typr.foundations.docs.mariadb

import dev.typr.foundations.MariaType
import dev.typr.foundations.MariaTypes

@Suppress("unused")
class EnumType {
    //start
    // Define your Kotlin enum
    enum class Status { PENDING, ACTIVE, COMPLETED }

    // Create MariaType for it
    val statusType: MariaType<Status> = MariaTypes.ofEnum("status", Status::valueOf)
    //stop
}
