---
title: Composing Operations
---

import Snippet from '@site/src/components/Snippet';

# Composing Operations

Operations can be composed as values — combined, sequenced, and chained — so that multiple database actions run in a single transaction without manual connection handling.

## Combining Independent Operations

`.combineWith()` combines two operations that don't depend on each other. Both run in the same transaction, and a function combines their results:

<Snippet file="core/ComposingWith" />

## Running Multiple Writes

When you have several write operations and only care about completion (not individual results), use `Operation.allOf()`:

<Snippet file="core/ComposingAllOf" />

## Sequencing a List

When you have a dynamic list of operations, `OperationRead.sequence()` runs them all and collects the results:

<Snippet file="core/ComposingSequence" />

## Data Flow Between Operations

Use `.then()` to feed one operation's result into the next operation's [template](./templates). The first operation runs, and its result becomes the input to the template:

<Snippet file="core/TemplateThen" />

When the first operation returns a record and the template uses `.from()`, use `.then()` with the `Template.From` directly:

<Snippet file="core/TemplateThenFrom" />

## Conditional Execution

`OperationRead.ifEmpty()` implements the find-or-create pattern: run the first operation, and if it returns empty (empty Optional, null, or None), run the fallback instead:

<Snippet file="core/ComposingIfEmpty" />

## Performance: Why Composition Matters

Operation composition isn't just an API convenience — it's a **performance primitive**. The way you compose operations determines whether the execution engine can optimize them.

### The key insight: `combine()` is parallel, `then()` is sequential

When you write `a.combine(b)`, you're telling the execution engine that `a` and `b` are **independent** — neither needs the other's result. When you write `a.then(template)`, you're saying the continuation **depends** on `a`'s result.

This distinction matters for execution:

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

Since `combine()` nests — `a.combine(b).combine(c).combine(d)` creates a tree of `Combine` nodes — the parallelization is **recursive**. All leaf operations end up submitted concurrently when the strategy supports it.

### When to use which combinator

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

:::info Applicative vs Monadic
This design is an instance of the **applicative functor** pattern from functional programming. `combine()` is the applicative product — it declares that two computations are independent, enabling the runtime to execute them in parallel. `then()` is the monadic bind — it declares a dependency, forcing sequential execution.

Many database libraries only offer monadic composition (each query depends on the previous connection state). By also offering applicative composition, foundations-jdbc gives the pipeline optimizer the freedom to batch independent queries into a single network round-trip. This distinction is invisible at the API level but has a **5x performance impact** under network latency.
:::

## Analyzing Composed Operations

`QueryAnalyzer` can walk the entire operation tree and analyze every SQL statement in one call. See [Query Analysis](./query-analysis#analyzing-composed-operations) for details.

<Snippet file="analysis/QueryAnalysisAll" />
