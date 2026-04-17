package dev.typr.foundationssc.docs.oracle
import dev.typr.foundationssc.*
import dev.typr.foundationssc.data.*

import java.time.{Instant, LocalDateTime, ZonedDateTime}

@SuppressWarnings(Array("unused"))
object DateTimeTypes:
  // start
  val dateType: OracleType[LocalDateTime] = OracleTypes.date
  val tsType: OracleType[LocalDateTime] = OracleTypes.timestamp
  val ts3: OracleType[LocalDateTime] = OracleTypes.timestampOf(3) // TIMESTAMP(3)
  val tstz: OracleType[ZonedDateTime] = OracleTypes.timestampWithTimeZone // preserves zone regions
  val tsltz: OracleType[Instant] = OracleTypes.timestampWithLocalTimeZone
  // stop
