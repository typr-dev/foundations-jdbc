---
title: Structuring Repositories
---

import Snippet from '@site/src/components/Snippet';

# Structuring Repositories

A common pattern is to define queries as public vals on a repository object with declared types. Fixed queries become `OperationRead`s (name them for analysis reports), parameterized queries become `Template`s:

<Snippet file="core/UserRepo" />

Exposing `OperationRead` and `Template` directly — rather than wrapping them in methods — gives callers maximum flexibility. They can compose, batch, name, or analyze these values however they like, without the repository dictating execution strategy.

This also means the repository stays in the database layer: it knows _what_ to query, but not _when_ or _how_ to run it. The service layer owns the transaction boundary by calling `.transactRead(tx)` or `.transact(tx)`:

<Snippet file="core/UserService" />

Both `OperationRead` and `Template` implement `Analyzable`, so `AnalyzableScanner` discovers them automatically — no manual list needed. See [Query Analysis](./query-analysis) for details.
