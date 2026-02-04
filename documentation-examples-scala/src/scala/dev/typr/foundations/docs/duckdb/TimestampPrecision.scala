package dev.typr.foundations.docs.duckdb
import dev.typr.scalafoundations.*
import dev.typr.scalafoundations.data.*


import java.time.LocalDateTime

@SuppressWarnings(Array("unused"))
object TimestampPrecision:
  //start
  val tsSeconds: DuckDbType[LocalDateTime] = DuckDbTypes.timestamp_s
  val tsMillis: DuckDbType[LocalDateTime] = DuckDbTypes.timestamp_ms
  val tsNanos: DuckDbType[LocalDateTime] = DuckDbTypes.timestamp_ns
  //stop
