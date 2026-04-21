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

<Snippet file="core/ConnectionSettingsSetup" />

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

## Test Mode

Call `.rollbackOnly()` to roll back instead of committing — ideal for test isolation:

```java
var tx = Transactor.create(config).rollbackOnly();
```

## Observability

Attach a `QueryListener` to observe all queries and transactions:

```java
var tx = Transactor.create(config).withListener(myListener);
```

See [Listener & Test Mode](./strategies) and [Observability](./observability) for details.

## Raw JDBC Access

`Transactor.create()` returns `TransactorJdbc` — a subtype of `Transactor` that exposes the underlying JDBC connection:

```java
TransactorJdbc tx = Transactor.create(config);

// Raw JDBC when you need it
tx.executeJdbc(conn -> {
    var meta = conn.getMetaData();
    return meta.getTables(null, null, "%", null);
});
```

This is an escape hatch for vendor-specific JDBC extensions, `DatabaseMetaData`, advisory locks, or migration tooling. `executeJdbc` is only available on `TransactorJdbc` — [PgPipe](./pgpipe) does not use JDBC connections, so `PgPipelinePool` does not implement `TransactorJdbc` and will not expose this method. If you accidentally call `Connection.unwrap()` inside a PgPipe transaction, it throws at runtime.

For normal queries and updates, use the typed `OperationRead` / `Operation` API — it works identically across all backends.

## Error Handling

Database errors are thrown as `DatabaseException` — a sealed class with dialect-specific subtypes:

- `DatabaseException.Postgres` — structured PostgreSQL error with all ErrorResponse fields (schema, table, constraint, position caret, etc.)
- `DatabaseException.SqlServer` — structured SQL Server error with severity, procedure name, line number
- `DatabaseException.Jdbc` — wraps `SQLException` for other databases

See [Error Handling](./error-handling) for pattern matching examples and field details.
