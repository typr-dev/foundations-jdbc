---
title: Observability
---

import Snippet from '@site/src/components/Snippet';

# Observability

foundations-jdbc provides lightweight observability hooks: query listeners, named operations, timeouts, and interpolated SQL for debugging. Zero overhead when no listener is configured.

## Query Listeners

A `QueryListener` receives callbacks before and after every query. Implement the interface to add logging, metrics, or alerting:

<Snippet file="core/QueryListenerBasic" />

### Attaching to a Strategy

Attach a listener to a `Strategy` so all operations through that transactor are observed:

<Snippet file="core/QueryListenerStrategy" />

### Per-Operation Listeners

You can also attach a listener to a specific operation:

```java
operation.withListener(myListener).transact(tx);
```

## Named Operations

Give operations a name with `.named()`. The name appears as a SQL comment prefix (`/* name */`) visible in `pg_stat_activity`, slow query logs, and listener callbacks:

<Snippet file="core/OperationNamed" />

For composite operations (e.g. `a.with(b).named("dashboard")`), each leaf query gets a unique suffix: `dashboard#1`, `dashboard#2`, etc. Single queries get no suffix.

## Query Timeouts

`.timeout()` transfers a timeout to the database via `setQueryTimeout()`:

```java
Fragment.of("SELECT * FROM large_table")
    .query(parser.all())
    .timeout(Duration.ofSeconds(10))
    .transact(tx);
```

## Interpolated SQL

`QueryEvent.interpolatedSql()` inlines parameter values into the SQL string for debugging. String values are quoted, numeric values are bare, and nulls render as `NULL`:

<Snippet file="core/InterpolatedSql" />

## Strategy Merging

Strategies can be merged. All hooks compose (both run), and listeners are combined:

<Snippet file="core/StrategyMerge" />

### Per-Transaction Override

Use `withStrategy()` to create a new transactor with a merged strategy:

<Snippet file="core/StrategyOverride" />

## Patterns

### Slow Query Detection

<Snippet file="core/QueryListenerSlowQuery" />

### Micrometer Integration

<Snippet file="core/QueryListenerMetrics" />
