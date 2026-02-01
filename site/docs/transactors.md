---
title: Transactors
---

import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

# Transactors

A `Transactor` manages database connections and transactions. It provides a clean API for executing database operations with automatic connection and transaction lifecycle management.

## Setting Up a Transactor

<Tabs groupId="language">
<TabItem value="java" label="Java">

```java
// The Transactor manages connections and transactions
// You choose the strategy — it handles the lifecycle
var tx = connectionSource.transactor(Transactor.defaultStrategy());

// Everything inside runs in one transaction: begin, commit, close
List<ProductRow> products = tx.execute(conn ->
    Fragment.of("SELECT * FROM product WHERE price > ?")
        .param(PgTypes.numeric, minPrice)
        .query(ProductRow.rowParser.list())
        .runUnchecked(conn)
);
```

</TabItem>
<TabItem value="kotlin" label="Kotlin">

```kotlin
// The Transactor manages connections and transactions
val tx = connectionSource.transactor(Transactor.defaultStrategy())

// Everything inside runs in one transaction: begin, commit, close
val products: List<ProductRow> = tx.execute { conn ->
    Fragment.of("SELECT * FROM product WHERE price > ?")
        .param(PgTypes.numeric, minPrice)
        .query(ProductRow.rowParser.list())
        .runUnchecked(conn)
}
```

</TabItem>
<TabItem value="scala" label="Scala">

```scala
// The Transactor manages connections and transactions
val tx = connectionSource.transactor(Transactor.defaultStrategy())

// Everything inside runs in one transaction: begin, commit, close
val products: List[ProductRow] = tx.execute(conn =>
    sql"SELECT * FROM product WHERE price > ${PgTypes.numeric.encode(minPrice)}"
        .query(ProductRow.rowParser.list())
        .runUnchecked(conn)
)
```

</TabItem>
</Tabs>

## Built-in Strategies

| Strategy | Description |
|----------|-------------|
| `Transactor.defaultStrategy()` | begin, commit, close |
| `Transactor.autoCommitStrategy()` | no transaction, just close |
| `Transactor.rollbackOnErrorStrategy()` | begin, commit on success, rollback on error, close |
| `Transactor.testStrategy()` | begin, rollback, close (for tests) |

## Custom Strategies

Define your own with explicit hooks:

<Tabs groupId="language">
<TabItem value="java" label="Java">

```java
new Transactor.Strategy(
    conn -> conn.setAutoCommit(false),  // before
    Connection::commit,                  // after (success)
    throwable -> { /* handle error */ }, // oops
    Connection::close                    // always (finally)
);
```

</TabItem>
<TabItem value="kotlin" label="Kotlin">

```kotlin
Transactor.Strategy(
    { conn -> conn.autoCommit = false },  // before
    { conn -> conn.commit() },             // after (success)
    { throwable -> /* handle error */ },   // oops
    { conn -> conn.close() }               // always (finally)
)
```

</TabItem>
<TabItem value="scala" label="Scala">

```scala
Transactor.Strategy(
    conn => conn.setAutoCommit(false),  // before
    conn => conn.commit(),               // after (success)
    throwable => (),                     // oops
    conn => conn.close()                 // always (finally)
)
```

</TabItem>
</Tabs>
