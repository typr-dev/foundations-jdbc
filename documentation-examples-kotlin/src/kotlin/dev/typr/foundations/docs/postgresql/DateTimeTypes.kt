package dev.typr.foundations.docs.postgresql

import dev.typr.foundations.PgType
import dev.typr.foundations.PgTypes
import org.postgresql.util.PGInterval
import java.time.Instant
import java.time.LocalDate

@Suppress("unused")
class DateTimeTypes {
    //start
    val dateType: PgType<LocalDate> = PgTypes.date
    val timestamptzType: PgType<Instant> = PgTypes.timestamptz
    val intervalType: PgType<PGInterval> = PgTypes.interval
    //stop
}
