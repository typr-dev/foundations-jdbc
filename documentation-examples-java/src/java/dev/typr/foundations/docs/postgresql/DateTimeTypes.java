package dev.typr.foundations.docs.postgresql;

import dev.typr.foundations.PgType;
import dev.typr.foundations.PgTypes;
import java.time.Instant;
import java.time.LocalDate;
import org.postgresql.util.PGInterval;

@SuppressWarnings("unused")
public class DateTimeTypes {
  // start
  PgType<LocalDate> dateType = PgTypes.date;
  PgType<Instant> timestamptzType = PgTypes.timestamptz;
  PgType<PGInterval> intervalType = PgTypes.interval;
  // stop
}
