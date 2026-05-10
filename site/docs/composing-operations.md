---
title: Composing Operations
---

import Snippet from '@site/src/components/Snippet';

# Composing operations

Operations compose as values, so multiple database actions run in a single transaction without manual connection handling.

## Combining independent operations

`.combineWith()` combines two operations that don't depend on each other. Both run in the same transaction, and a function combines their results:

<Snippet file="core/ComposingWith" />

## Running multiple writes

When you have several write operations and only care about completion (not individual results), use `Operation.allOf()`:

<Snippet file="core/ComposingAllOf" />

## Sequencing a list

When you have a dynamic list of operations, `OperationRead.sequence()` runs them all and collects the results:

<Snippet file="core/ComposingSequence" />

## Data flow between operations

Use `.then()` to feed one operation's result into a continuation function that returns the next operation. The first operation runs, and its result becomes the input to the function:

<Snippet file="core/OperationThen" />

When the first operation returns a record, you can destructure inside the continuation:

<Snippet file="core/OperationThenRecord" />

## Conditional execution

`OperationRead.ifEmpty()` implements the find-or-create pattern: run the first operation, and if it returns empty (empty Optional, null, or None), run the fallback instead:

<Snippet file="core/ComposingIfEmpty" />

## Read-only composition

When you compose `OperationRead` values, the result is always `OperationRead`. Mix in a single write `Operation`, and the result becomes `Operation`. The type system tracks this automatically:

<Snippet file="core/ReadonlyComposition" />

This means `transactRead` works for any tree of pure reads, and the compiler enforces it. See [Read-only transactions](./readonly-transactions) for more.

## Performance: why composition matters

How you compose operations determines whether the execution engine can optimize them.

### `combine()` is parallel, `then()` is sequential

When you write `a.combine(b)`, you're telling the execution engine that `a` and `b` are independent: neither needs the other's result. When you write `a.then(continuation)`, the continuation depends on `a`'s result.

The distinction affects execution:

| Combinator | Dependency | Execution |
| :--- | :--- | :--- |
| `combine()` | Independent | Parallelizable — backend decides strategy |
| `combineWith()` | Independent | Same as combine |
| `sequence()` | Independent | Decomposes into combine tree |
| `allOf()` | Independent | Decomposes into combine tree |
| `then()` | Dependent | Sequential (must be) |
| `ifEmpty()` | Conditional | Sequential (check, then maybe fallback) |
| `map()` | Transform | No I/O (pure function) |

The `OperationRunner` delegates `Combine` nodes to a pluggable `CombineStrategy`. JDBC uses `SEQUENTIAL` (one query at a time on the same connection). Other backends can implement `PARALLEL` to execute both halves concurrently.

Since `combine()` nests, `a.combine(b).combine(c).combine(d)` creates a tree of `Combine` nodes, and the parallelization is recursive. All leaf operations are submitted concurrently when the strategy supports it.

### When to use which

**Use `combine()` / `combineWith()` when queries are independent:**
```java
// Dashboard: load user + orders + preferences in ~1 RTT
var page = tx.execute(
    findUser.on(userId)
        .combineWith(getOrders.on(userId), getPrefs.on(userId),
            (user, orders, prefs) -> new DashboardPage(user, orders, prefs))
);
```

**Use `sequence()` for dynamic lists of independent operations:**
```java
// Fan-out: load N items in ~1 RTT
var items = tx.execute(
    OperationRead.sequence(ids.stream()
        .map(id -> findItem.on(id))
        .toList())
);
```

**Use `then()` when the next query depends on the previous result:**
```java
// Chain: insert, then read back with generated ID
var created = tx.execute(
    insertUser.on(newUser).then(findUserById)
);
```

**Use `ifEmpty()` for find-or-create patterns:**
```java
// Conditional: find existing, or create if missing
var user = tx.execute(
    OperationRead.ifEmpty(findUser.on(email), createUser.on(newUser))
);
```

:::info Applicative vs monadic
This design is the applicative functor pattern from functional programming. `combine()` is the applicative product: it declares that two computations are independent, so the runtime can execute them in parallel. `then()` is the monadic bind: it declares a dependency, forcing sequential execution.

Many database libraries only offer monadic composition (each query depends on the previous connection state). By also offering applicative composition, foundations-jdbc lets the pipeline optimizer batch independent queries into a single network round-trip.
:::

## Analyzing composed operations

`QueryAnalyzer` walks the entire operation tree and analyzes every SQL statement in one call. See [Query analysis](./query-analysis#analyzing-composed-operations) for details.

<Snippet file="analysis/QueryAnalysisAll" />
