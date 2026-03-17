---
title: OpenTelemetry
---

import Snippet from '@site/src/components/Snippet';

# OpenTelemetry

The `foundations-jdbc-otel` module provides OpenTelemetry instrumentation for database operations. It plugs into the existing `QueryListener` mechanism to produce spans and metrics with zero overhead when no SDK is configured.

## Setup

Add the `foundations-jdbc-otel` module to your project alongside the core library. Then create an `OtelQueryListener` and attach it to your transactor's strategy:

<Snippet file="otel/OtelSetup" />

`TelemetryConfig.builder(config)` extracts the database system, host, port, and database name from the `DatabaseConfig`. You can also build it manually:

```java
var telemetryConfig = TelemetryConfig.builder(DatabaseSystem.POSTGRESQL)
    .serverAddress("db.example.com")
    .serverPort(5432)
    .dbNamespace("orders")
    .build();
```

## Span Attributes

Each query produces a span with attributes following [OTel Semantic Conventions v1.39.0](https://opentelemetry.io/docs/specs/semconv/database/):

| Attribute | Source |
|-----------|--------|
| `db.system.name` | From `TelemetryConfig` |
| `db.query.text` | Raw SQL text (when `recordQueryText` is enabled) |
| `server.address` / `server.port` | From config |
| `db.namespace` | Database name from config |
| `error.type` | Exception class on failure |

The span name is the operation's `.named()` value if set, otherwise `"DB query"`.

## TelemetryConfig Options

| Option | Default | Description |
|--------|---------|-------------|
| `recordQueryText` | `true` | Include `db.query.text` attribute |

## No ThreadLocal

The listener uses **backdated spans** rather than ThreadLocal state. `beforeQuery` is a no-op. `afterQuery` and `failedQuery` each create a complete span by computing `Instant.now() - elapsed`. This is safe for virtual threads, nested queries, and composed listeners.

## Pool Metrics

If you use HikariCP, register connection pool metrics:

<Snippet file="otel/OtelPoolMetrics" />

Metrics follow the OTel database pool semantic conventions:

| Metric | Description |
|--------|-------------|
| `db.client.connection.count` | Active and idle connections (by `state`) |
| `db.client.connection.pending_requests` | Threads waiting for a connection |
| `db.client.connection.max` | Maximum pool size |

## Grafana Dashboard

A pre-built Grafana dashboard is included at `foundations-jdbc-otel/grafana/foundations-jdbc-dashboard.json`. Import it into Grafana and configure:

- **Tempo / Jaeger** datasource for trace panels (latency percentiles, slow queries)
- **Prometheus** datasource for metric panels (throughput, pool status)

Panels include:
- Query latency p50/p95/p99 by operation
- Error rate
- Throughput by database system
- Connection pool active/idle/pending
- Pool usage percentage gauge
- Slow queries table
