---
title: Observability
---

import Snippet from '@site/src/components/Snippet';

# Observability

foundations-jdbc has four observability hooks: query listeners, named operations, query timeouts, and interpolated SQL for debugging. They cost nothing when no listener is configured.

## Query listeners

A `QueryListener` receives callbacks before and after every query. Implement the interface to add logging, metrics, or alerting:

<Snippet file="core/QueryListenerBasic" />

### Attaching to a transactor

Attach a listener to a `Transactor` so all operations are observed:

<Snippet file="core/TransactorListener" />

### Per-operation listeners

You can also attach a listener to a specific operation:

```java
operation.withListener(myListener).transactRead(tx);
```

## Named operations

Give operations a name with `.named()`. The name appears as a SQL comment prefix (`/* name */`) visible in `pg_stat_activity`, slow query logs, and listener callbacks:

<Snippet file="core/OperationNamed" />

For composite operations (e.g. `a.combine(b).named("dashboard")`), each leaf query gets a unique suffix: `dashboard#1`, `dashboard#2`, etc. Single queries get no suffix.

## Query timeouts

`.timeout()` sets a query timeout on the database via `setQueryTimeout()`:

```java
Fragment.of("SELECT * FROM large_table")
    .query(codec.all())
    .timeout(Duration.ofSeconds(10))
    .transactRead(tx);
```

## Interpolated SQL

`QueryEvent.interpolatedSql()` inlines parameter values into the SQL string for debugging. String values are quoted, numeric values are bare, and nulls render as `NULL`:

<Snippet file="core/InterpolatedSql" />

For listener configuration, see [Transactors](./transactors#observability).

## Patterns

### Slow query detection

<Snippet file="core/QueryListenerSlowQuery" />

### Micrometer integration

<Snippet file="core/QueryListenerMetrics" />

## OpenTelemetry

For tracing and metrics, see the [OpenTelemetry](./opentelemetry) module. It provides `OtelQueryListener` (automatic span creation with semantic conventions), `PoolMetrics` (HikariCP gauges), and a Grafana dashboard template.
