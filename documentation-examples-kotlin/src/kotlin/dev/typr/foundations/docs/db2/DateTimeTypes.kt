package dev.typr.foundations.docs.db2

import dev.typr.foundations.Db2Type
import dev.typr.foundations.Db2Types
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@Suppress("unused")
class DateTimeTypes {
    //start
    val dateType: Db2Type<LocalDate> = Db2Types.date
    val timeType: Db2Type<LocalTime> = Db2Types.time
    val tsType: Db2Type<LocalDateTime> = Db2Types.timestamp
    val ts6: Db2Type<LocalDateTime> = Db2Types.timestamp(6)
    //stop
}
