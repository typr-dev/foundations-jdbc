package dev.typr.foundations.docs.oracle

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*
import java.time.LocalDateTime
import java.time.OffsetDateTime

@Suppress("unused")
class DateTimeTypes {
    //start
    val dateType: OracleType<LocalDateTime> = OracleTypes.date
    val tsType: OracleType<LocalDateTime> = OracleTypes.timestamp
    val ts3: OracleType<LocalDateTime> = OracleTypes.timestamp(3)  // TIMESTAMP(3)
    val tstz: OracleType<OffsetDateTime> = OracleTypes.timestampWithTimeZone
    val tsltz: OracleType<OffsetDateTime> = OracleTypes.timestampWithLocalTimeZone
    //stop
}
