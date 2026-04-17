---
title: Transactors
---

import Snippet from '@site/src/components/Snippet';

# Transactors

A `Transactor` is how you run database operations. It obtains a connection, runs your code inside a transaction, and handles commit, rollback, and cleanup automatically.

By default, each call is wrapped in a transaction: auto-commit off, commit on success, rollback on error, close always.

## Setting Up

Each supported database has a typed config builder — your IDE will autocomplete all available options. Pass the config to `Transactor.create()`:

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

var tx = Transactor.create(config, settings);
// or: Transactor.create(config, settings, strategy)
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

The default strategy wraps each call in a transaction. Pass a different built-in strategy to `Transactor.create()`:

| Strategy | Behavior |
|----------|----------|
| `defaultStrategy()` | begin, commit on success, rollback on error, close |
| `autoCommitStrategy()` | no transaction management, just close |
| `testStrategy()` | begin, **rollback** on success or error, close — keeps test data isolated |

```java
var tx = Transactor.create(config, Transactor.testStrategy());
```

:::warning testStrategy is for tests only
`testStrategy()` rolls back every `execute` / `transact`, including ones that succeeded — **nothing persists**. If you copy this into a script, a tutorial, a migration, or any production code, your DDL and INSERTs will silently disappear. For anything that should persist, use `defaultStrategy()`.
:::

Strategies can be thoroughly customized with composable hooks for transaction lifecycle and observability. See [Strategies](./strategies) for details.
