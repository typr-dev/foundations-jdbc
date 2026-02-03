package dev.typr.foundations.docs.oracle;

import dev.typr.foundations.OracleType;
import dev.typr.foundations.OracleTypes;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@SuppressWarnings("unused")
public class DateTimeTypes {
    //start
    OracleType<LocalDateTime> dateType = OracleTypes.date;
    OracleType<LocalDateTime> tsType = OracleTypes.timestamp;
    OracleType<LocalDateTime> ts3 = OracleTypes.timestamp(3);  // TIMESTAMP(3)
    OracleType<OffsetDateTime> tstz = OracleTypes.timestampWithTimeZone;
    OracleType<OffsetDateTime> tsltz = OracleTypes.timestampWithLocalTimeZone;
    //stop
}
