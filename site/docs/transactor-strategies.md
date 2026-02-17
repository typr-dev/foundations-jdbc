---
title: Transactor Strategies
---

import Snippet from '@site/src/components/Snippet';

# Transactor Strategies

A `Transactor.Strategy` defines four hooks that wrap every execution:

| Hook | When it runs |
|------|-------------|
| `before` | Before your code — typically `setAutoCommit(false)` |
| `after` | After your code succeeds — typically `commit` |
| `oops` | When an exception is thrown (catch) |
| `always` | In all cases (finally) — typically `close` |

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
