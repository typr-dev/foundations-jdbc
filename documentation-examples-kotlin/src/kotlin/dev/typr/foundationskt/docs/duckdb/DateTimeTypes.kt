package dev.typr.foundationskt.docs.duckdb

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.OffsetDateTime

@Suppress("unused")
class DateTimeTypes {
    //start
    val dateType: DuckDbType<LocalDate> = DuckDbTypes.date
    val tsType: DuckDbType<LocalDateTime> = DuckDbTypes.timestamp
    val tstzType: DuckDbType<OffsetDateTime> = DuckDbTypes.timestamptz
    val intervalType: DuckDbType<Duration> = DuckDbTypes.interval
    //stop
}
