---
title: Error Handling
---

# Error Handling

All execution-level methods (`transact()`, `transactRead()`, `execute()`, `run()`) throw `DatabaseException` — an **unchecked** exception that wraps the underlying `SQLException`. You never need `throws SQLException` on your method signatures.

```java
// No throws declaration needed
public List<User> listUsers() {
    return selectAll.transactRead(tx);
}
```

## Why unchecked?

JDBC errors at the execution level (network failures, constraint violations, syntax errors) are almost never recoverable by application code. Wrapping in a checked exception forces every caller to handle or propagate something they can't fix, adding boilerplate without safety.

This is the same approach used by Spring JDBC, JOOQ, JDBI, and Doobie.

## DatabaseException API

`DatabaseException` wraps a `SQLException` and exposes its key fields:

| Method | Returns | Description |
|--------|---------|-------------|
| `sqlState()` | `@Nullable String` | The [SQL state](https://en.wikipedia.org/wiki/SQLSTATE) code (e.g. `"23505"` for unique violation) |
| `vendorCode()` | `int` | The vendor-specific error code |
| `sqlException()` | `SQLException` | The original `SQLException` |
| `getMessage()` | `String` | The error message from the driver |

## Handling specific errors

Use the SQL state class (first two characters) to distinguish error categories:

```java
try {
    insertUser.on(user).transact(tx);
} catch (DatabaseException e) {
    if (e.sqlState() != null && e.sqlState().startsWith("23")) {
        // Integrity constraint violation (unique, foreign key, check, etc.)
        throw new DuplicateUserException(user.email());
    }
    throw e;
}
```

Common SQL state classes:

| Class | Meaning |
|-------|---------|
| `"23"` | Integrity constraint violation |
| `"42"` | Syntax error or access rule violation |
| `"08"` | Connection exception |
| `"40"` | Transaction rollback |
| `"57"` | Operator intervention (e.g. query cancelled) |

## Where checked exceptions remain

`SQLException` is still used in **implementation interfaces** (`DbRead`, `DbWrite`, `ResultSetParser`, `SqlFunction`) — these are where you interact with JDBC directly. The framework catches `SQLException` at the execution boundary (`transact()`, `transactRead()`, `execute()`, `run()`) and wraps it in `DatabaseException`.

## Spring integration

`DatabaseException` is unchecked, so Spring's `@Transactional` rolls back automatically — no `rollbackFor` needed:

```java
@Transactional
public void placeOrder(Order order) {
    insertOrder.on(order).transact(tx);
    updateInventory.on(order.itemId()).transact(tx);
}
```

See [Spring Boot](spring-boot.md) for full integration details.
