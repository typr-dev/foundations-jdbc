package dev.typr.foundations.docs.oracle;

import dev.typr.foundations.OracleType;
import dev.typr.foundations.OracleTypes;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@SuppressWarnings("unused")
public class DateTimeTypes {
  // start
  OracleType<LocalDateTime> dateType = OracleTypes.date;
  OracleType<LocalDateTime> tsType = OracleTypes.timestamp;
  OracleType<LocalDateTime> ts3 = OracleTypes.timestampOf(3); // TIMESTAMP(3)
  OracleType<ZonedDateTime> tstz = OracleTypes.timestampWithTimeZone; // preserves zone regions
  OracleType<Instant> tsltz = OracleTypes.timestampWithLocalTimeZone;
  // stop
}
