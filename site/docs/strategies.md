---
title: Strategies
---

import Snippet from '@site/src/components/Snippet';

# Strategies

A `Transactor.Strategy` defines hooks that wrap every execution. The [built-in strategies](./transactors#strategies) cover common patterns — this page covers customization.

## Hook Lifecycle

| Hook | When it runs |
|------|-------------|
| `onBegin` | Before your code — typically `setAutoCommit(false)` |
| `onSuccess` | After your code succeeds — typically `commit` |
| `onFailure` | When an exception is thrown (catch) — receives the connection and the throwable |
| `onComplete` | In all cases (finally) — typically `close` |
| `listener` | A `QueryListener` for observability (see [Observability](observability)) |

## Custom Strategies

Build a strategy from scratch using `replaceX` methods — each one sets a single hook:

<Snippet file="core/TransactorCustomStrategy" />

## Strategy Merging

Use `mergeX` methods to compose hooks — both the existing and new hook run in order. `mergeListener` composes listeners:

<Snippet file="core/StrategyMerge" />

The `mergeListener` convenience on `Transactor` creates a derived transactor with the listener merged into its strategy:

<Snippet file="core/StrategyOverride" />
