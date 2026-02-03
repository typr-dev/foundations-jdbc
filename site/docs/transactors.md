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

## Custom Strategies

Define your own with explicit hooks:

<Snippet file="core/TransactorCustomStrategy" />
