package dev.typr.foundationskt.docs.otel

import com.zaxxer.hikari.HikariDataSource
import dev.typr.foundations.otel.PoolMetrics
import io.opentelemetry.api.GlobalOpenTelemetry
import java.util.Optional

@Suppress("unused")
class OtelPoolMetrics {
    lateinit var dataSource: HikariDataSource

    //start
    val poolMetrics: PoolMetrics =
        PoolMetrics.register(GlobalOpenTelemetry.get(), dataSource, Optional.of("main-pool"))

    fun onShutdown() {
        poolMetrics.close()
    }
    //stop
}
