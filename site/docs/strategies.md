---
title: Listener & Test Mode
---

import Snippet from '@site/src/components/Snippet';

# Listener & Test Mode

A `Transactor` supports two cross-cutting concerns that work regardless of backend:

## Test Mode (Rollback Only)

Call `.rollbackOnly()` to make `transact()` roll back instead of committing. Your tests run real SQL without leaving data behind:

<Snippet file="core/TransactorConfig" />

## Attaching a Listener

`withListener()` sets the listener. `mergeListener()` composes with an existing one:

<Snippet file="core/TransactorListener" />

The listener receives both query-level and transaction-level callbacks. See [Observability](observability) for the full `QueryListener` interface.
