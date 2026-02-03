package dev.typr.foundations.docs.db2;

import dev.typr.foundations.Db2Type;
import dev.typr.foundations.Db2Types;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@SuppressWarnings("unused")
public class DateTimeTypes {
    //start
    Db2Type<LocalDate> dateType = Db2Types.date;
    Db2Type<LocalTime> timeType = Db2Types.time;
    Db2Type<LocalDateTime> tsType = Db2Types.timestamp;
    Db2Type<LocalDateTime> ts6 = Db2Types.timestamp(6);
    //stop
}
