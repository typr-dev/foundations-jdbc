package dev.typr.foundationskt.docs.otel

import dev.typr.foundations.QueryListener
import dev.typr.foundationskt.Transactor
import dev.typr.foundationskt.connect.*
import dev.typr.foundations.otel.OtelQueryListener
import dev.typr.foundations.otel.TelemetryConfig
import io.opentelemetry.api.GlobalOpenTelemetry

@Suppress("unused")
class OtelSetup {
    //start
    val config =
        PgConfig.builder("localhost", 5432, "mydb", "user", "pass").build()

    val telemetryConfig = TelemetryConfig.builder(config).build()

    val otelListener: QueryListener =
        OtelQueryListener.create(GlobalOpenTelemetry.get(), telemetryConfig)

    val tx = Transactor.create(config).withListener(otelListener)
    //stop
}
