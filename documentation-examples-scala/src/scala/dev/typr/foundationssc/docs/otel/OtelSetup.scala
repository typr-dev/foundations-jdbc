package dev.typr.foundationssc.docs.otel
import dev.typr.foundationssc.Transactor
import dev.typr.foundationssc.connect.*

import dev.typr.foundations.QueryListener
import dev.typr.foundations.otel.{OtelQueryListener, TelemetryConfig}
import io.opentelemetry.api.GlobalOpenTelemetry

@SuppressWarnings(Array("unused"))
object OtelSetup:
  // start
  val config =
    PgConfig
      .builder("localhost", 5432, "mydb", "user", "pass")
      .build()

  val telemetryConfig = TelemetryConfig.builder(config).build()

  val otelListener: QueryListener =
    OtelQueryListener.create(GlobalOpenTelemetry.get(), telemetryConfig)

  val tx = Transactor.create(config, Transactor.defaultStrategy().mergeListener(otelListener))
  // stop
