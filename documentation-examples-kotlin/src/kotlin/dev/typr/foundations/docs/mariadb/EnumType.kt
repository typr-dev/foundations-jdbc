package dev.typr.foundations.docs.mariadb

import dev.typr.kotlinfoundations.*
import dev.typr.kotlinfoundations.data.*

@Suppress("unused")
class EnumType {
    //start
    // Define your Kotlin enum
    enum class Status { PENDING, ACTIVE, COMPLETED }

    // Create MariaType for it
    val statusType: MariaType<Status> = MariaTypes.ofEnum("status", Status::valueOf)
    //stop
}
