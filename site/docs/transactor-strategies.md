---
title: Transactor Strategies
---

import Snippet from '@site/src/components/Snippet';

# Transactor Strategies

A `Transactor.Strategy` defines hooks that wrap every execution:

| Hook | When it runs |
|------|-------------|
| `before` | Before your code — typically `setAutoCommit(false)` |
| `after` | After your code succeeds — typically `commit` |
| `oops` | When an exception is thrown (catch) — receives the connection and the throwable |
| `always` | In all cases (finally) — typically `close` |
| `listener` | A `QueryListener` for observability (see [Observability](observability)) |

## Built-in Strategies

| Strategy | Behavior |
|----------|----------|
| `defaultStrategy()` | begin, commit, close |
| `autoCommitStrategy()` | no transaction management, just close |
| `rollbackOnErrorStrategy()` | begin, commit on success, rollback on error, close |
| `testStrategy()` | begin, **rollback** (not commit), close — keeps test data isolated |

Pass a strategy to `.transactor()`:

```java
var tx = config.transactor(Transactor.testStrategy());
```

## Custom Strategies

<Snippet file="core/TransactorCustomStrategy" />

## Strategy Merging

Strategies can be merged — all hooks compose (both run in order), and listeners are combined:

<Snippet file="core/StrategyMerge" />

Use `withStrategy()` on a Transactor to create a derived transactor with merged strategy:

<Snippet file="core/StrategyOverride" />
