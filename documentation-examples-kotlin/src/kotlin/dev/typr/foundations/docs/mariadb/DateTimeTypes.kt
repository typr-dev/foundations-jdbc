package dev.typr.foundations.docs.mariadb

import dev.typr.kotlinfoundations.*
import dev.typr.kotlinfoundations.data.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.Year

@Suppress("unused")
class DateTimeTypes {
    //start
    val dateType: MariaType<LocalDate> = MariaTypes.date
    val timeType: MariaType<LocalTime> = MariaTypes.time
    val datetimeType: MariaType<LocalDateTime> = MariaTypes.datetime
    val yearType: MariaType<Year> = MariaTypes.year

    // With fractional seconds precision
    val timeFsp: MariaType<LocalTime> = MariaTypes.time(6)
    val dtFsp: MariaType<LocalDateTime> = MariaTypes.datetime(3)
    //stop
}
