package dev.typr.foundations.otel;

import dev.typr.foundations.QueryEvent;
import dev.typr.foundations.QueryListener;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.api.trace.Tracer;
import java.time.Instant;
import org.jetbrains.annotations.Nullable;

/**
 * A {@link QueryListener} that creates OpenTelemetry spans for database queries.
 *
 * <p>Key design: No ThreadLocal state. Both {@link #afterQuery} and {@link #failedQuery} create a
 * complete span using backdated timestamps (current time minus elapsed). The {@link #beforeQuery}
 * method is a no-op. This design is safe for virtual threads, nested queries, and composed
 * listeners.
 *
 * <p>When no OpenTelemetry SDK is configured, {@code GlobalOpenTelemetry.get()} returns a noop
 * tracer with zero overhead.
 *
 * <p>Span attributes follow OpenTelemetry Semantic Conventions v1.39.0.
 */
public final class OtelQueryListener implements QueryListener {

  private static final String INSTRUMENTATION_NAME = "foundations-jdbc";

  // Semantic convention attribute keys
  private static final AttributeKey<String> DB_SYSTEM_NAME =
      AttributeKey.stringKey("db.system.name");
  private static final AttributeKey<String> DB_QUERY_TEXT =
      AttributeKey.stringKey("db.query.text");
  private static final AttributeKey<String> DB_NAMESPACE =
      AttributeKey.stringKey("db.namespace");
  private static final AttributeKey<String> SERVER_ADDRESS =
      AttributeKey.stringKey("server.address");
  private static final AttributeKey<Long> SERVER_PORT = AttributeKey.longKey("server.port");
  private static final AttributeKey<String> ERROR_TYPE = AttributeKey.stringKey("error.type");

  private final Tracer tracer;
  private final TelemetryConfig config;

  private OtelQueryListener(Tracer tracer, TelemetryConfig config) {
    this.tracer = tracer;
    this.config = config;
  }

  /** Create a new OtelQueryListener. */
  public static OtelQueryListener create(OpenTelemetry otel, TelemetryConfig config) {
    Tracer tracer = otel.getTracer(INSTRUMENTATION_NAME);
    return new OtelQueryListener(tracer, config);
  }

  @Override
  public void beforeQuery(String sql, @Nullable String name) {
    // No-op: spans are created entirely in afterQuery/failedQuery with backdated timestamps.
  }

  @Override
  public void afterQuery(QueryEvent event) {
    buildAndEndSpan(event, false);
  }

  @Override
  public void failedQuery(QueryEvent event) {
    buildAndEndSpan(event, true);
  }

  private void buildAndEndSpan(QueryEvent event, boolean isError) {
    Instant endTime = Instant.now();
    Instant startTime = endTime.minus(event.elapsed());

    String spanName =
        (event.name() != null && !event.name().isEmpty()) ? event.name() : "DB query";

    var spanBuilder =
        tracer
            .spanBuilder(spanName)
            .setStartTimestamp(startTime)
            .setAttribute(DB_SYSTEM_NAME, config.dbSystem().otelName());

    // Query text
    if (config.recordQueryText()) {
      spanBuilder.setAttribute(DB_QUERY_TEXT, event.sql());
    }

    // Server attributes
    config.serverAddress().ifPresent(addr -> spanBuilder.setAttribute(SERVER_ADDRESS, addr));
    config
        .serverPort()
        .ifPresent(port -> spanBuilder.setAttribute(SERVER_PORT, port.longValue()));
    config.dbNamespace().ifPresent(ns -> spanBuilder.setAttribute(DB_NAMESPACE, ns));

    Span span = spanBuilder.startSpan();

    if (isError && event.error() != null) {
      span.setStatus(StatusCode.ERROR, event.error().getMessage());
      span.recordException(event.error());
      span.setAttribute(ERROR_TYPE, event.error().getClass().getName());
    }

    span.end(endTime);
  }
}
