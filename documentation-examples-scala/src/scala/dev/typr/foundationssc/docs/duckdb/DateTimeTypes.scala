package dev.typr.foundationssc.docs.duckdb
import dev.typr.foundationssc.*
import dev.typr.foundationssc.data.*

import java.time.{Duration, Instant, LocalDate, LocalDateTime}

@SuppressWarnings(Array("unused"))
object DateTimeTypes:
  // start
  val dateType: DuckDbType[LocalDate] = DuckDbTypes.date
  val tsType: DuckDbType[LocalDateTime] = DuckDbTypes.timestamp
  val tstzType: DuckDbType[Instant] = DuckDbTypes.timestamptz
  val intervalType: DuckDbType[Duration] = DuckDbTypes.interval
  // stop
