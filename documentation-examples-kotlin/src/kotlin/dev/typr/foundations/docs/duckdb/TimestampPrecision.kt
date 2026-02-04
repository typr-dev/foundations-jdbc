package dev.typr.foundations.docs.duckdb

import dev.typr.kotlinfoundations.*
import dev.typr.kotlinfoundations.data.*
import java.time.LocalDateTime

@Suppress("unused")
class TimestampPrecision {
    //start
    val tsSeconds: DuckDbType<LocalDateTime> = DuckDbTypes.timestamp_s
    val tsMillis: DuckDbType<LocalDateTime> = DuckDbTypes.timestamp_ms
    val tsNanos: DuckDbType<LocalDateTime> = DuckDbTypes.timestamp_ns
    //stop
}
