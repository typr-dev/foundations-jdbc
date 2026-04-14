package dev.typr.foundations.docs.otel;

import dev.typr.foundations.QueryListener;
import dev.typr.foundations.Transactor;
import dev.typr.foundations.connect.PostgresConfig;
import dev.typr.foundations.otel.OtelQueryListener;
import dev.typr.foundations.otel.TelemetryConfig;
import io.opentelemetry.api.GlobalOpenTelemetry;

@SuppressWarnings("unused")
public class OtelSetup {
  // start
  PostgresConfig config = PostgresConfig.builder("localhost", 5432, "mydb", "user", "pass").build();

  TelemetryConfig telemetryConfig = TelemetryConfig.builder(config).build();

  QueryListener otelListener = OtelQueryListener.create(GlobalOpenTelemetry.get(), telemetryConfig);

  Transactor tx =
      Transactor.create(config, Transactor.defaultStrategy().mergeListener(otelListener));
  // stop
}
