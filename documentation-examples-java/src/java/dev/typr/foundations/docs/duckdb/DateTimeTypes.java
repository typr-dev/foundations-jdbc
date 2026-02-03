package dev.typr.foundations.docs.duckdb;

import dev.typr.foundations.DuckDbType;
import dev.typr.foundations.DuckDbTypes;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@SuppressWarnings("unused")
public class DateTimeTypes {
    //start
    DuckDbType<LocalDate> dateType = DuckDbTypes.date;
    DuckDbType<LocalDateTime> tsType = DuckDbTypes.timestamp;
    DuckDbType<OffsetDateTime> tstzType = DuckDbTypes.timestamptz;
    DuckDbType<Duration> intervalType = DuckDbTypes.interval;
    //stop
}
