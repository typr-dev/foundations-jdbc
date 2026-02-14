---
title: Result Parsers
---

import Snippet from '@site/src/components/Snippet';

# Result Parsers

Result set parsers handle the full lifecycle of reading from a ResultSet. They build on top of [row parsers](/) to provide convenient ways to consume query results.

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

## Transforming Results with `.map()`

`ResultSetParser` supports `.map()` for type transformations. This is used by the Kotlin and Scala wrappers to convert between language-specific types — for example, `Optional<T>` to nullable `T?` in Kotlin, or `java.util.List<T>` to `scala.List[T]` in Scala.

You can also use `.map()` for your own transformations:

```java
// Transform the result after parsing
var parser = rowParser.all().map(list -> list.stream()
    .filter(user -> user.active())
    .toList());
```
