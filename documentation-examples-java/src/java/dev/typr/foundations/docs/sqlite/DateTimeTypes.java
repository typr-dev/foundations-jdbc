package dev.typr.foundations.docs.sqlite;

import dev.typr.foundations.SqliteType;
import dev.typr.foundations.SqliteTypes;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@SuppressWarnings("unused")
public class DateTimeTypes {
  // start
  // SQLite stores date/time as ISO-8601 TEXT (the xerial driver default).
  SqliteType<LocalDate> dateType = SqliteTypes.date;
  SqliteType<LocalTime> timeType = SqliteTypes.time;
  SqliteType<LocalDateTime> datetimeType = SqliteTypes.datetime;
  SqliteType<LocalDateTime> timestampType = SqliteTypes.timestamp;
  SqliteType<Instant> instantType = SqliteTypes.instant; // ISO-8601 with `Z` suffix
  // stop
}
