package dev.typr.foundationskt.docs.oracle

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZonedDateTime

@Suppress("unused")
class DateTimeTypes {
    //start
    val dateType: OracleType<LocalDateTime> = OracleTypes.date
    val tsType: OracleType<LocalDateTime> = OracleTypes.timestamp
    val ts3: OracleType<LocalDateTime> = OracleTypes.timestampOf(3)  // TIMESTAMP(3)
    val tstz: OracleType<ZonedDateTime> = OracleTypes.timestampWithTimeZone // preserves zone regions
    val tsltz: OracleType<Instant> = OracleTypes.timestampWithLocalTimeZone
    //stop
}
