package dev.typr.foundations.docs.postgresql

import dev.typr.kotlinfoundations.*
import dev.typr.kotlinfoundations.data.*
import java.util.UUID

@Suppress("unused")
class UuidType {
    //start
    val uuidType: PgType<UUID> = PgTypes.uuid
    //stop
}
