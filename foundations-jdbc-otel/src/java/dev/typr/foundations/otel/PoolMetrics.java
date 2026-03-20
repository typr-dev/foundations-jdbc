package dev.typr.foundations.otel;

import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.HikariPoolMXBean;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.metrics.Meter;
import io.opentelemetry.api.metrics.ObservableLongGauge;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Registers OpenTelemetry metrics for a HikariCP connection pool. Follows the OTel database pool
 * semantic conventions.
 *
 * <p>HikariCP is a {@code provided} dependency — this class is only usable when HikariCP is on the
 * classpath.
 *
 * <p>Example:
 *
 * <pre>{@code
 * var poolMetrics = PoolMetrics.register(otel, hikariDataSource, Optional.of("mypool"));
 * // ... on shutdown:
 * poolMetrics.close();
 * }</pre>
 */
public final class PoolMetrics implements AutoCloseable {

  private static final String INSTRUMENTATION_NAME = "foundations-jdbc";
  private static final AttributeKey<String> POOL_NAME = AttributeKey.stringKey("pool.name");
  private static final AttributeKey<String> STATE = AttributeKey.stringKey("state");

  private final List<ObservableLongGauge> gauges;

  private PoolMetrics(List<ObservableLongGauge> gauges) {
    this.gauges = gauges;
  }

  /**
   * Register pool metrics for the given HikariDataSource.
   *
   * @param otel the OpenTelemetry instance
   * @param dataSource the HikariCP data source
   * @param poolName optional pool name for the {@code pool.name} attribute
   * @return an AutoCloseable that unregisters the gauge callbacks
   */
  public static PoolMetrics register(
      OpenTelemetry otel, HikariDataSource dataSource, Optional<String> poolName) {

    Meter meter = otel.getMeter(INSTRUMENTATION_NAME);
    String name = poolName.orElseGet(dataSource::getPoolName);
    Attributes poolAttrs = Attributes.of(POOL_NAME, name);
    Attributes usedAttrs = poolAttrs.toBuilder().put(STATE, "used").build();
    Attributes idleAttrs = poolAttrs.toBuilder().put(STATE, "idle").build();

    List<ObservableLongGauge> gauges = new ArrayList<>();

    gauges.add(
        meter
            .gaugeBuilder("db.client.connection.count")
            .setDescription("The number of connections that are currently in state described by the state attribute")
            .setUnit("{connection}")
            .ofLongs()
            .buildWithCallback(
                measurement -> {
                  HikariPoolMXBean pool = dataSource.getHikariPoolMXBean();
                  if (pool != null) {
                    measurement.record(pool.getActiveConnections(), usedAttrs);
                    measurement.record(pool.getIdleConnections(), idleAttrs);
                  }
                }));

    gauges.add(
        meter
            .gaugeBuilder("db.client.connection.pending_requests")
            .setDescription("The number of current pending requests for an open connection")
            .setUnit("{request}")
            .ofLongs()
            .buildWithCallback(
                measurement -> {
                  HikariPoolMXBean pool = dataSource.getHikariPoolMXBean();
                  if (pool != null) {
                    measurement.record(pool.getThreadsAwaitingConnection(), poolAttrs);
                  }
                }));

    gauges.add(
        meter
            .gaugeBuilder("db.client.connection.max")
            .setDescription("The maximum number of open connections allowed")
            .setUnit("{connection}")
            .ofLongs()
            .buildWithCallback(
                measurement ->
                    measurement.record(dataSource.getMaximumPoolSize(), poolAttrs)));

    return new PoolMetrics(gauges);
  }

  @Override
  public void close() {
    for (var gauge : gauges) {
      gauge.close();
    }
  }
}
