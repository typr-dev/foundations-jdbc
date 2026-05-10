---
title: Transactors
---

import Snippet from '@site/src/components/Snippet';

# Transactors

A `Transactor` runs database operations. It obtains a connection, runs your code inside a transaction, and handles commit, rollback, and cleanup.

By default, each call is wrapped in a transaction: auto-commit off, commit on success, rollback on error, close always.

## Setting up

Each supported database has a typed config builder. Pass the config to `Transactor.create()`:

<Snippet file="core/TransactorSetup" />

## Connection settings

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

## Connection pooling

For production, use `HikariDataSourceFactory` from the `foundations-jdbc-hikari` module:

```java
var pool = HikariDataSourceFactory.create(config);
var tx = pool.transactor();
```

## Single connection mode

`SingleConnectionDataSource` reuses one connection across all calls — needed for DuckDB in-memory, where each new connection creates a separate database:

```java
var ds = SingleConnectionDataSource.create(config);
var tx = ds.transactor();
```

## Test mode

Call `.rollbackOnly()` to roll back instead of committing. Useful for test isolation:

```java
var tx = Transactor.create(config).rollbackOnly();
```

## Observability

Attach a `QueryListener` to observe all queries and transactions:

```java
var tx = Transactor.create(config).withListener(myListener);
```

See [Observability](./observability) for details.

## Raw JDBC access

`Transactor.create()` returns `TransactorJdbc` — a subtype of `Transactor` that exposes the underlying JDBC connection:

```java
TransactorJdbc tx = Transactor.create(config);

// Raw JDBC when you need it
tx.executeJdbc(conn -> {
    var meta = conn.getMetaData();
    return meta.getTables(null, null, "%", null);
});
```

This is an escape hatch for vendor-specific JDBC extensions, `DatabaseMetaData`, advisory locks, or migration tooling. `executeJdbc` is only available on `TransactorJdbc`.

## Error handling

Database errors are thrown as `DatabaseException` — a sealed class with dialect-specific subtypes:

- `DatabaseException.Postgres` — structured PostgreSQL error with all ErrorResponse fields (schema, table, constraint, position caret, etc.)
- `DatabaseException.SqlServer` — structured SQL Server error with severity, procedure name, line number
- `DatabaseException.Jdbc` — wraps `SQLException` for other databases

See [Error Handling](./error-handling) for pattern matching examples and field details.
