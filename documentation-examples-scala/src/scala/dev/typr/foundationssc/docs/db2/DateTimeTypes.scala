package dev.typr.foundationssc.docs.db2
import dev.typr.foundationssc.*
import dev.typr.foundationssc.data.*


import java.time.{LocalDate, LocalDateTime, LocalTime}

@SuppressWarnings(Array("unused"))
object DateTimeTypes:
  //start
  val dateType: Db2Type[LocalDate] = Db2Types.date
  val timeType: Db2Type[LocalTime] = Db2Types.time
  val tsType: Db2Type[LocalDateTime] = Db2Types.timestamp
  val ts6: Db2Type[LocalDateTime] = Db2Types.timestamp(6)
  //stop
