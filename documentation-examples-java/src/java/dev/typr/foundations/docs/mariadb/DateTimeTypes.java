package dev.typr.foundations.docs.mariadb;

import dev.typr.foundations.MariaType;
import dev.typr.foundations.MariaTypes;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Year;

@SuppressWarnings("unused")
public class DateTimeTypes {
  // start
  MariaType<LocalDate> dateType = MariaTypes.date;
  MariaType<LocalTime> timeType = MariaTypes.time;
  MariaType<LocalDateTime> datetimeType = MariaTypes.datetime;
  MariaType<Year> yearType = MariaTypes.year;

  // With fractional seconds precision
  MariaType<LocalTime> timeFsp = MariaTypes.time(6);
  MariaType<LocalDateTime> dtFsp = MariaTypes.datetime(3);
  // stop
}
