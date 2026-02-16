package dev.typr.foundations.docs.postgresql

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*
import java.util.UUID

@Suppress("unused")
class UuidType {
    //start
    val uuidType: PgType<UUID> = PgTypes.uuid
    //stop
}
