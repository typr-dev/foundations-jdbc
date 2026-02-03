package dev.typr.foundations.docs.duckdb

import dev.typr.foundations.{DuckDbType, DuckDbTypes}
import java.time.{Duration, LocalDate, LocalDateTime, OffsetDateTime}

@SuppressWarnings(Array("unused"))
object DateTimeTypes:
  //start
  val dateType: DuckDbType[LocalDate] = DuckDbTypes.date
  val tsType: DuckDbType[LocalDateTime] = DuckDbTypes.timestamp
  val tstzType: DuckDbType[OffsetDateTime] = DuckDbTypes.timestamptz
  val intervalType: DuckDbType[Duration] = DuckDbTypes.interval
  //stop
