---
title: Error Handling
---

# Error Handling

All execution-level methods (`transact()`, `transactRead()`, `execute()`, `run()`) throw `DatabaseException` -- an **unchecked** exception. You never need `throws` declarations on your method signatures.

## Sealed Exception Hierarchy

`DatabaseException` is a sealed class with three subtypes:

| Subtype | Carries | When |
|---------|---------|------|
| `DatabaseException.Postgres` | `PgError` with 17 structured fields | PostgreSQL connections |
| `DatabaseException.SqlServer` | `SqlServerError` with 7 structured fields | SQL Server connections |
| `DatabaseException.Jdbc` | `SQLException` | All other databases |

All three share a `sqlState()` method on the base class, so you can handle errors generically or match on the specific subtype.

## Pattern Matching

Use Java 21 pattern matching to handle each database differently:

```java
try {
    insertUser.on(user).transact(tx);
} catch (DatabaseException.Postgres pg) {
    if ("23505".equals(pg.sqlState())) {
        String constraint = pg.pgError().constraintName();
        // handle unique violation
    }
} catch (DatabaseException.SqlServer ss) {
    System.out.println("Error " + ss.sqlServerError().errorNumber());
} catch (DatabaseException.Jdbc e) {
    System.out.println("SQL state: " + e.sqlState());
}
```

## SQL State Handling

When you only care about the error category and not the database, catch the base class and switch on `sqlState()`:

```java
try {
    insertUser.on(user).transact(tx);
} catch (DatabaseException e) {
    switch (e.sqlState()) {
        case "23505" -> handleDuplicate();
        case "23503" -> handleForeignKey();
        default -> throw e;
    }
}
```

Common SQL state classes:

| Class | Meaning |
|-------|---------|
| `"23"` | Integrity constraint violation |
| `"42"` | Syntax error or access rule violation |
| `"08"` | Connection exception |
| `"40"` | Transaction rollback |

## Rich PostgreSQL Errors

`DatabaseException.Postgres` carries a `PgError` record with all fields from the PostgreSQL [ErrorResponse wire protocol message](https://www.postgresql.org/docs/current/protocol-error-fields.html):

The required fields are `severity`, `message`, and `sqlState`. Nullable fields include `detail`, `hint`, `position`, `where`, `schemaName`, `tableName`, `columnName`, `dataTypeName`, and `constraintName`. Additional internal fields (`internalPosition`, `internalQuery`, `file`, `line`, `routine`) are available for PL/pgSQL debugging.

## Formatted Error Messages

`getMessage()` produces multi-line formatted output. For a syntax error with a position field, the output includes a caret pointing to the error location:

<img src="/img/pg-error-syntax.png" alt="PostgreSQL syntax error with caret" style={{borderRadius: '8px', boxShadow: '0 2px 8px rgba(0,0,0,0.15)', maxWidth: '500px', margin: '0.5rem 0'}} />

For a unique constraint violation, detail and constraint fields are appended:

<img src="/img/pg-error-unique-violation.png" alt="PostgreSQL unique constraint violation with structured fields" style={{borderRadius: '8px', boxShadow: '0 2px 8px rgba(0,0,0,0.15)', maxWidth: '500px', margin: '0.5rem 0'}} />

When the driver provides a hint, it's included along with the caret:

<img src="/img/pg-error-column-hint.png" alt="PostgreSQL column not found with hint" style={{borderRadius: '8px', boxShadow: '0 2px 8px rgba(0,0,0,0.15)', maxWidth: '500px', margin: '0.5rem 0'}} />

When a `ResultSet` value doesn't match the expected type at runtime, the error includes the column position, expected and actual types, the offending value, and the root cause:

<img src="/img/query-analysis-runtime-error.png" alt="Runtime parse error with detailed context" style={{borderRadius: '8px', boxShadow: '0 2px 8px rgba(0,0,0,0.15)', maxWidth: '500px', margin: '1rem 0'}} />

## SQL Server Errors

`DatabaseException.SqlServer` carries a `SqlServerError` record with fields from the TDS ERROR token:

| Field | Type | Description |
|-------|------|-------------|
| `message()` | `@Nullable String` | Error message text |
| `errorNumber()` | `int` | Vendor error number (e.g. 2627 for unique violation) |
| `errorSeverity()` | `int` | Severity level (0-25) |
| `errorState()` | `int` | Error state |
| `serverName()` | `@Nullable String` | Server that raised the error |
| `procedureName()` | `@Nullable String` | Stored procedure name, if applicable |
| `lineNumber()` | `long` | Line number within the batch or procedure |

## Where Checked Exceptions Remain

`SQLException` is still used in **implementation interfaces** -- `SqlFunction`, `DbRead`, `DbWrite`, `ResultSetParser`. These are where your code interacts with JDBC directly. The framework catches `SQLException` at the execution boundary and wraps it in the appropriate `DatabaseException` subtype.

## Spring Integration

`DatabaseException` is unchecked, so Spring's `@Transactional` rolls back automatically -- no `rollbackFor` needed:

```java
@Transactional
public void placeOrder(Order order) {
    insertOrder.on(order).transact(tx);
    updateInventory.on(order.itemId()).transact(tx);
}
```

See [Spring Boot](spring-boot.md) for full integration details.
