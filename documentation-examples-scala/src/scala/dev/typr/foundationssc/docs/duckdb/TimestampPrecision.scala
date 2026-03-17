package dev.typr.foundationssc.docs.duckdb
import dev.typr.foundationssc.*
import dev.typr.foundationssc.data.*

import java.time.LocalDateTime

@SuppressWarnings(Array("unused"))
object TimestampPrecision:
  // start
  val tsSeconds: DuckDbType[LocalDateTime] = DuckDbTypes.timestamp_s
  val tsMillis: DuckDbType[LocalDateTime] = DuckDbTypes.timestamp_ms
  val tsNanos: DuckDbType[LocalDateTime] = DuckDbTypes.timestamp_ns
  // stop
