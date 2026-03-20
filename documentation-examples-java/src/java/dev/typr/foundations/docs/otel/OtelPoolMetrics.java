package dev.typr.foundations.docs.otel;

import com.zaxxer.hikari.HikariDataSource;
import dev.typr.foundations.otel.PoolMetrics;
import io.opentelemetry.api.GlobalOpenTelemetry;
import java.util.Optional;

@SuppressWarnings("unused")
public class OtelPoolMetrics {
  HikariDataSource dataSource = null; // placeholder

  // start
  PoolMetrics poolMetrics =
      PoolMetrics.register(GlobalOpenTelemetry.get(), dataSource, Optional.of("main-pool"));

  void onShutdown() {
    poolMetrics.close();
  }
  // stop
}
