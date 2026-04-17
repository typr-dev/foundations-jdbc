package dev.typr.foundationskt.docs.mariadb

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*

@Suppress("unused")
class EnumType {
    //start
    // Define your Kotlin enum
    enum class Status { PENDING, ACTIVE, COMPLETED }

    // Create MariaType — reified, derives ENUM literal automatically
    val statusType: MariaType<Status> = MariaTypes.ofEnum<Status>()
    //stop
}
