---
title: Dynamic SQL
---

import Snippet from '@site/src/components/Snippet';

# Dynamic SQL

Build dynamic queries with optional predicates — and have every structural variant verified automatically.

## The Problem

Search forms and filters often produce dynamic SQL. Each combination of present/absent predicates is a structurally different query. Without tooling, you either:

- **Build SQL strings manually** and lose type safety
- **Test only the combinations you think of**, missing edge cases
- **Analyze each permutation individually**, which doesn't scale

With N optional predicates, there are 2^N possible query structures. `.optionally()` lets you declare them inline, and [Query Analysis](./query-analysis) verifies all of them with a single `checker.check()` call.

## Single Optional Parameter

Use `.optionally()` with a parameterized fragment to create a filter that is included when a value is provided and skipped when absent:

<Snippet file="core/OptionalQueryBasic" />

The template parameter type reflects the optionality — `Optional<String>` in Java, `String?` in Kotlin, `Option[String]` in Scala.

## Boolean Flags

For SQL chunks without parameters (e.g., `AND active = TRUE`), pass a plain `Fragment` to `.optionally()`. The template parameter becomes a `Boolean` — `true` includes the chunk, `false` skips it:

```java
Fragment.of("SELECT * FROM users WHERE 1=1")
    .optionally(Fragment.of(" AND active = TRUE"))
    .query(userParser.all());
// SqlTemplate<Boolean, List<User>>
```

## Multiple Optional Parameters

Chain multiple `.optionally()` calls to build queries with many independent filters. Each adds a template parameter:

<Snippet file="core/OptionalQueryMulti" />

## How Analysis Works

When you call `checker.check()` on a template with N optional predicates, Query Analysis automatically expands all 2^N structural variants. Each variant is prepared against the database and verified independently.

For the example above with 3 optional predicates, analysis checks all 8 combinations:

| name filter | email filter | active flag | SQL WHERE clause |
|:-----------:|:------------:|:-----------:|:-----------------|
| absent | absent | absent | `WHERE 1=1 ORDER BY name` |
| present | absent | absent | `WHERE 1=1 AND name ILIKE ? ORDER BY name` |
| absent | present | absent | `WHERE 1=1 AND email ILIKE ? ORDER BY name` |
| present | present | absent | `WHERE 1=1 AND name ILIKE ? AND email ILIKE ? ORDER BY name` |
| absent | absent | present | `WHERE 1=1 AND active = TRUE ORDER BY name` |
| present | absent | present | `WHERE 1=1 AND name ILIKE ? AND active = TRUE ORDER BY name` |
| absent | present | present | `WHERE 1=1 AND email ILIKE ? AND active = TRUE ORDER BY name` |
| present | present | present | `WHERE 1=1 AND name ILIKE ? AND email ILIKE ? AND active = TRUE ORDER BY name` |

If any variant has a type error, the analysis report tells you exactly which combination failed and why.

## Multi-Parameter Optionally

When an optional clause needs multiple parameters (e.g., a `BETWEEN` range), pass a multi-parameter builder. The grouped parameters are provided or omitted together as a single unit:

<Snippet file="core/OptionalQueryRange" />

## Packaging Filters in a Record

As the number of optional predicates grows, the raw template signature becomes unwieldy. Use `.from()` with getter references to map a record (or data class / case class) directly to template parameters — callers just pass the record:

<Snippet file="core/OptionalQueryFacade" />
