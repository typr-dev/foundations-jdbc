package dev.typr.foundations.docs.postgresql

import dev.typr.foundations.{PgType, PgTypes}
import org.postgresql.util.PGInterval
import java.time.{Instant, LocalDate}

@SuppressWarnings(Array("unused"))
object DateTimeTypes:
  //start
  val dateType: PgType[LocalDate] = PgTypes.date
  val timestamptzType: PgType[Instant] = PgTypes.timestamptz
  val intervalType: PgType[PGInterval] = PgTypes.interval
  //stop
