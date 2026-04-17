package dev.typr.foundationskt.docs.duckdb

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime

@Suppress("unused")
class DateTimeTypes {
    //start
    val dateType: DuckDbType<LocalDate> = DuckDbTypes.date
    val tsType: DuckDbType<LocalDateTime> = DuckDbTypes.timestamp
    val tstzType: DuckDbType<Instant> = DuckDbTypes.timestamptz
    val intervalType: DuckDbType<Duration> = DuckDbTypes.interval
    //stop
}
