---
title: Transactors
---

import Snippet from '@site/src/components/Snippet';

# Transactors

A `Transactor` manages database connections and transactions. It provides a clean API for executing database operations with automatic connection and transaction lifecycle management.

## Setting Up a Transactor

<Snippet file="core/TransactorSetup" />

## Built-in Strategies

| Strategy | Description |
|----------|-------------|
| `Transactor.defaultStrategy()` | begin, commit, close |
| `Transactor.autoCommitStrategy()` | no transaction, just close |
| `Transactor.rollbackOnErrorStrategy()` | begin, commit on success, rollback on error, close |
| `Transactor.testStrategy()` | begin, rollback, close (for tests) |

## Multi-Operation Transactions

Compose multiple operations with `.with()` to run them in a single transaction:

<Snippet file="core/ComposingWith" />

See [Composing Operations](./composing-operations) for the full set of combinators.

## Custom Strategies

Define your own with explicit hooks:

<Snippet file="core/TransactorCustomStrategy" />
