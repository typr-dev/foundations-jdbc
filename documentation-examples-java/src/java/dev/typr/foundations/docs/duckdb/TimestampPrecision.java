package dev.typr.foundations.docs.duckdb;

import dev.typr.foundations.DuckDbType;
import dev.typr.foundations.DuckDbTypes;

import java.time.LocalDateTime;

@SuppressWarnings("unused")
public class TimestampPrecision {
    //start
    DuckDbType<LocalDateTime> tsSeconds = DuckDbTypes.timestamp_s;
    DuckDbType<LocalDateTime> tsMillis = DuckDbTypes.timestamp_ms;
    DuckDbType<LocalDateTime> tsNanos = DuckDbTypes.timestamp_ns;
    //stop
}
