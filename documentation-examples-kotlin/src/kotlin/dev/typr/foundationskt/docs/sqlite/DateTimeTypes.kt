package dev.typr.foundationskt.docs.sqlite

import dev.typr.foundationskt.*

@Suppress("unused")
class DateTimeTypes {
    //start
    val dateType: SqliteType<java.time.LocalDate> = SqliteTypes.date
    val timeType: SqliteType<java.time.LocalTime> = SqliteTypes.time
    val datetimeType: SqliteType<java.time.LocalDateTime> = SqliteTypes.datetime
    val timestampType: SqliteType<java.time.LocalDateTime> = SqliteTypes.timestamp
    val instantType: SqliteType<java.time.Instant> = SqliteTypes.instant
    //stop
}
