package dev.typr.foundations.docs.duckdb

import dev.typr.kotlinfoundations.*
import dev.typr.kotlinfoundations.data.*
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

@Suppress("unused")
class MapTypes {
    //start
    // Create map types using the mapTo() combinator
    val mapStrInt: DuckDbType<Map<String, Int>> = DuckDbTypes.varchar.mapTo(DuckDbTypes.integer)
    val mapStrStr: DuckDbType<Map<String, String>> = DuckDbTypes.varchar.mapTo(DuckDbTypes.varchar)
    val mapUuidTime: DuckDbType<Map<UUID, LocalTime>> = DuckDbTypes.uuid.mapTo(DuckDbTypes.time)

    // Works with any combination of types
    val mapStrDate: DuckDbType<Map<String, LocalDate>> = DuckDbTypes.varchar.mapTo(DuckDbTypes.date)
    //stop
}
