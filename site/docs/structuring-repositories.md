---
title: Structuring Repositories
---

import Snippet from '@site/src/components/Snippet';

# Structuring Repositories

A common pattern is to define queries as public vals on a repository object with declared types. Fixed queries become `Operation`s (name them for analysis reports), parameterized queries become `Template`s. Both implement `Analyzable`, so you can collect them into a single list for batch verification:

<Snippet file="core/UserRepo" />

Exposing `Operation` and `Template` directly — rather than wrapping them in methods — gives callers maximum flexibility. They can compose, batch, name, or analyze these values however they like, without the repository dictating execution strategy.

This also means the repository stays in the database layer: it knows _what_ to query, but not _when_ or _how_ to run it. The service layer owns the transaction boundary by calling `.transact(tx)`:

<Snippet file="core/UserService" />

Pass the `analyzables` list to `QueryChecker.checkAll` in a test to verify every query against the database schema at once.
