---
title: Transactors
---

import Snippet from '@site/src/components/Snippet';

# Transactors

A `Transactor` is how you run database operations. It obtains a connection, runs your code inside a transaction, and handles commit, rollback, and cleanup automatically.

The default [strategy](#strategies) wraps each call in a transaction: auto-commit off, commit on success, rollback on error, close always. You can customize this behavior by passing a different [strategy](#strategies) to `.transactor(strategy)`.

## Setting Up

Each supported database has a typed config builder — your IDE will autocomplete all available options. Call `.transactor()` at the end of the chain:

<Snippet file="core/TransactorSetup" />

## Connection Settings

Override connection-level defaults by passing `ConnectionSettings`:

```java
var settings = ConnectionSettings.builder()
    .transactionIsolation(TransactionIsolation.READ_COMMITTED)
    .readOnly(true)
    .schema("app")
    .connectionInitSql("SET search_path TO app")
    .build();

var tx = config.transactor(settings);
// or: config.transactor(settings, strategy)
```

| Setting | Description |
|---------|-------------|
| `transactionIsolation` | `READ_UNCOMMITTED`, `READ_COMMITTED`, `REPEATABLE_READ`, `SERIALIZABLE` |
| `autoCommit` | Override the driver's default auto-commit mode |
| `readOnly` | Hint to the driver that connections are read-only |
| `catalog` | Set the default catalog |
| `schema` | Set the default schema |
| `connectionInitSql` | SQL executed once when each connection is created |

## Connection Pooling

For production, use `HikariDataSourceFactory` from the `foundations-jdbc-hikari` module:

```java
var pool = HikariDataSourceFactory.create(config);
var tx = pool.transactor();
```

## Single Connection Mode

`SingleConnectionDataSource` reuses one connection across all calls — needed for DuckDB in-memory, where each new connection creates a separate database:

```java
var ds = SingleConnectionDataSource.create(config);
var tx = ds.transactor();
```

## Strategies

A `Transactor.Strategy` defines hooks that wrap every execution:

| Hook | When it runs |
|------|-------------|
| `before` | Before your code — typically `setAutoCommit(false)` |
| `after` | After your code succeeds — typically `commit` |
| `oops` | When an exception is thrown (catch) — receives the connection and the throwable |
| `always` | In all cases (finally) — typically `close` |
| `listener` | A `QueryListener` for observability (see [Observability](observability)) |

### Built-in Strategies

| Strategy | Behavior |
|----------|----------|
| `defaultStrategy()` | begin, commit, close |
| `autoCommitStrategy()` | no transaction management, just close |
| `rollbackOnErrorStrategy()` | begin, commit on success, rollback on error, close |
| `testStrategy()` | begin, **rollback** (not commit), close — keeps test data isolated |

Pass a strategy to `.transactor()`:

```java
var tx = config.transactor(Transactor.testStrategy());
```

### Custom Strategies

Build a strategy from scratch using `replaceX` methods — each one sets a single hook:

<Snippet file="core/TransactorCustomStrategy" />

### Strategy Merging

Use `mergeX` methods to compose hooks — both the existing and new hook run in order. `mergeListener` composes listeners:

<Snippet file="core/StrategyMerge" />

The `mergeListener` convenience on `Transactor` creates a derived transactor with the listener merged into its strategy:

<Snippet file="core/StrategyOverride" />
