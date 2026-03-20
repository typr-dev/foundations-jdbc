package dev.typr.foundationskt.docs.mariadb

import dev.typr.foundationskt.MariaType
import dev.typr.foundationskt.MariaTypes
import java.util.UUID

@Suppress("unused")
class UuidType {
    //start
    val uuidType: MariaType<UUID> = MariaTypes.uuid
    //stop
}
