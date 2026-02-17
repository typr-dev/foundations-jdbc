---
title: Transactors
---

import Snippet from '@site/src/components/Snippet';

# Transactors

A `Transactor` is how you run database operations. It obtains a connection, runs your code inside a transaction, and handles commit, rollback, and cleanup automatically.

The default [strategy](./transactor-strategies) wraps each call in a transaction: auto-commit off, commit on success, rollback on error, close always. You can customize this behavior by passing a different [strategy](./transactor-strategies) to `.transactor(strategy)`.

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

For production, use `PooledDataSource` from the `foundations-jdbc-hikari` module:

```java
var pool = PooledDataSource.create(config);
var tx = pool.transactor();
```

## Single Connection Mode

`SingleConnectionDataSource` reuses one connection across all calls — needed for DuckDB in-memory, where each new connection creates a separate database:

```java
var ds = SingleConnectionDataSource.create(config);
var tx = ds.transactor();
```
