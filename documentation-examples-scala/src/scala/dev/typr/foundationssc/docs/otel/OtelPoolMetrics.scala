package dev.typr.foundationssc.docs.otel

import com.zaxxer.hikari.HikariDataSource
import dev.typr.foundations.otel.PoolMetrics
import io.opentelemetry.api.GlobalOpenTelemetry
import java.util.Optional

@SuppressWarnings(Array("unused"))
object OtelPoolMetrics:
  val dataSource: HikariDataSource = null // placeholder

  // start
  val poolMetrics: PoolMetrics =
    PoolMetrics.register(GlobalOpenTelemetry.get(), dataSource, Optional.of("main-pool"))

  def onShutdown(): Unit =
    poolMetrics.close()
  // stop
