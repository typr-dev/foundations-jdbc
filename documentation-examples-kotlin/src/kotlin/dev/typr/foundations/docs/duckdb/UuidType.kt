package dev.typr.foundations.docs.duckdb

import dev.typr.kotlinfoundations.*
import dev.typr.kotlinfoundations.data.*
import java.util.UUID

@Suppress("unused")
class UuidType {
    //start
    val uuidType: DuckDbType<UUID> = DuckDbTypes.uuid
    //stop
}
