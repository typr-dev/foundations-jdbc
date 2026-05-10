---
title: Read-Only Transactions
---

import Snippet from '@site/src/components/Snippet';

# Read-Only Transactions

foundations-jdbc separates read and write paths at the type level. Queries that only read data can use `transactRead`, which skips transaction overhead and restricts what code can do with the connection.

## Two transaction modes

`transact` opens a full read-write transaction (auto-commit off, explicit BEGIN/COMMIT). `transactRead` uses auto-commit mode: no BEGIN, no COMMIT, no rollback.

## One-liner convenience

Every `OperationRead` has a `.transactRead(tx)` method that executes the operation directly:

<Snippet file="core/ReadonlyTransaction" />

This is equivalent to `tx.transactRead(conn -> conn.execute(findAll))`.

## Multiple reads in one session

`transactRead` reuses a single connection for the duration of the block, which avoids repeated connection acquisition:

<Snippet file="core/ReadonlyTransactionMulti" />

Each query runs on the same connection in auto-commit mode. There is no transaction coordination, but connection-level settings (schema, timeout, etc.) stay consistent.

## Type safety

The two modes use different connection types. `ConnectionRead` exposes only read methods:

```java
public interface ConnectionRead {
    <T> T execute(OperationRead<T> op);
    <T> List<T> query(Fragment sql, RowCodec<T> codec);
    <T> Optional<T> queryFirst(Fragment sql, RowCodec<T> codec);
}

public interface Connection extends ConnectionRead {
    <T> T execute(Operation<T> op);
    int update(Fragment sql);
    java.sql.Connection unwrap();
}
```

Inside a `transactRead` block, the compiler prevents writes:

```java
tx.transactRead(conn -> {
    conn.query(selectSql, codec);       // OK
    conn.execute(readOp);               // OK — OperationRead accepted

    conn.update(insertSql);             // compile error — no update() on ConnectionRead
    conn.unwrap();                      // compile error — no unwrap() on ConnectionRead
    conn.execute(writeOp);              // compile error — Operation rejected, OperationRead required
    return null;
});
```

## The variance model

The type hierarchy follows a deliberate variance pattern:

- `OperationRead <: Operation`: read-only is a subtype of general operation. A read operation works anywhere an operation is expected.
- `Connection extends ConnectionRead`: a read-write connection can do everything a read-only connection can.

Operations require capabilities (fewer requirements = more general = subtype). Connections grant capabilities (more capabilities = more powerful = subtype). So a `Connection` can always be passed where a `ConnectionRead` is expected, and an `OperationRead` can always be passed where an `Operation` is expected.

## When to use which

| Mode | Use for |
|------|---------|
| `transactRead` | SELECT queries, reports, dashboards, read replicas |
| `transact` | INSERT, UPDATE, DELETE, DDL, anything that modifies data |
