---
title: Structuring Repositories
---

import Snippet from '@site/src/components/Snippet';

# Structuring repositories

A common pattern is to define queries on a repository object. Fixed queries become public vals returning `OperationRead` (name them for analysis reports). Parameterized queries become methods that take their parameters and return `OperationRead` or `Operation`:

<Snippet file="core/UserRepo" />

Exposing `OperationRead` and `Operation` directly, rather than executing them inside the repository, lets callers compose, batch, name, or analyze these values however they like. The repository stops dictating execution strategy.

The repository then stays in the database layer: it knows _what_ to query, but not _when_ or _how_ to run it. The service layer owns the transaction boundary by calling `.transactRead(tx)` or `.transact(tx)`:

<Snippet file="core/UserService" />

Both `OperationRead` and `Operation` implement `Analyzable`, so `AnalyzableScanner` discovers them automatically, both as fields and methods. See [Query Analysis](./query-analysis) for details.
