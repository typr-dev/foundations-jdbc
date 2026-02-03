---
title: Result Sets
---

import Snippet from '@site/src/components/Snippet';

# Result Sets

Result set parsers handle the full lifecycle of reading from a ResultSet. They build on top of [row parsers](./row-types) to provide convenient ways to consume query results.

## ResultSetParser

A `ResultSetParser<T>` reads a complete ResultSet and produces a value of type `T`. You typically create one from a `RowParser`:

<Snippet file="core/ResultSetParserUsage" />

## Available Parsers

From any `RowParser<T>` you can create:

| Method | Returns | Description |
|--------|---------|-------------|
| `.all()` | `List<T>` | All rows as a list |
| `.maxOne()` | `Optional<T>` / `T?` / `Option[T]` | Zero or one row (throws if more than one) |
| `.exactlyOne()` | `T` | Exactly one row (throws otherwise) |
