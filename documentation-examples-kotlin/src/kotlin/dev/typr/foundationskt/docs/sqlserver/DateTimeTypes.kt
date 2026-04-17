package dev.typr.foundationskt.docs.sqlserver

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.OffsetDateTime

@Suppress("unused")
class DateTimeTypes {
    //start
    val dateType: SqlServerType<LocalDate> = SqlServerTypes.date
    val timeType: SqlServerType<LocalTime> = SqlServerTypes.time
    val time3: SqlServerType<LocalTime> = SqlServerTypes.timeOf(3)  // TIME(3)

    // Legacy types
    val datetimeType: SqlServerType<LocalDateTime> = SqlServerTypes.datetime
    val smalldtType: SqlServerType<LocalDateTime> = SqlServerTypes.smalldatetime

    // Modern types (recommended)
    val datetime2Type: SqlServerType<LocalDateTime> = SqlServerTypes.datetime2
    val datetime2_3: SqlServerType<LocalDateTime> = SqlServerTypes.datetime2Of(3)

    // Timezone-aware
    val dtoType: SqlServerType<OffsetDateTime> = SqlServerTypes.datetimeoffset
    val dto3: SqlServerType<OffsetDateTime> = SqlServerTypes.datetimeoffsetOf(3)
    //stop
}
