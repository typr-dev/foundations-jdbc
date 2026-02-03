package dev.typr.foundations.docs.postgresql

import dev.typr.foundations.PgType
import dev.typr.foundations.PgTypes
import java.util.UUID

@Suppress("unused")
class UuidType {
    //start
    val uuidType: PgType<UUID> = PgTypes.uuid
    //stop
}
