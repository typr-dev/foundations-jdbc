---
title: Fragments
---

import Snippet from '@site/src/components/Snippet';

# Fragments

Fragments let you build SQL queries safely with type-checked parameters. Parameters are always bound via prepared statements — never interpolated into the SQL string.

## Building Fragments

<Snippet file="core/FragmentBuilding" />

## Composing Fragments

Fragments can be combined to build dynamic queries:

<Snippet file="core/FragmentComposing" />

## Executing Fragments

A fragment can be executed with a row parser to produce results:

| Method | Description |
|--------|-------------|
| `.query(parser).run(tx)` | Execute a SELECT query within a transactor |
| `.query(parser).runUnchecked(conn)` | Execute a SELECT query with a raw connection |
| `.update().run(tx)` | Execute an INSERT/UPDATE/DELETE |
