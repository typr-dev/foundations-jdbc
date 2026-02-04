package dev.typr.foundations.docs.oracle
import dev.typr.scalafoundations.*
import dev.typr.scalafoundations.data.*


import java.time.{LocalDateTime, OffsetDateTime}

@SuppressWarnings(Array("unused"))
object DateTimeTypes:
  //start
  val dateType: OracleType[LocalDateTime] = OracleTypes.date
  val tsType: OracleType[LocalDateTime] = OracleTypes.timestamp
  val ts3: OracleType[LocalDateTime] = OracleTypes.timestamp(3)  // TIMESTAMP(3)
  val tstz: OracleType[OffsetDateTime] = OracleTypes.timestampWithTimeZone
  val tsltz: OracleType[OffsetDateTime] = OracleTypes.timestampWithLocalTimeZone
  //stop
