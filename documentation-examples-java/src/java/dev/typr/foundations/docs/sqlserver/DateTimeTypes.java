package dev.typr.foundations.docs.sqlserver;

import dev.typr.foundations.SqlServerType;
import dev.typr.foundations.SqlServerTypes;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;

@SuppressWarnings("unused")
public class DateTimeTypes {
  // start
  SqlServerType<LocalDate> dateType = SqlServerTypes.date;
  SqlServerType<LocalTime> timeType = SqlServerTypes.time;
  SqlServerType<LocalTime> time3 = SqlServerTypes.timeOf(3); // TIME(3)

  // Legacy types
  SqlServerType<LocalDateTime> datetimeType = SqlServerTypes.datetime;
  SqlServerType<LocalDateTime> smalldtType = SqlServerTypes.smalldatetime;

  // Modern types (recommended)
  SqlServerType<LocalDateTime> datetime2Type = SqlServerTypes.datetime2;
  SqlServerType<LocalDateTime> datetime2_3 = SqlServerTypes.datetime2Of(3);

  // Timezone-aware
  SqlServerType<OffsetDateTime> dtoType = SqlServerTypes.datetimeoffset;
  SqlServerType<OffsetDateTime> dto3 = SqlServerTypes.datetimeoffsetOf(3);
  // stop
}
