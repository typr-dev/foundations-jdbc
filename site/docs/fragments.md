---
title: Fragments
---

import Snippet from '@site/src/components/Snippet';

# Fragments

A Fragment is a composable SQL building block — it holds a SQL string together with its bound parameters. There are two ways to build fragments: **string interpolation** (Kotlin and Scala) and the **builder pattern** (all languages).

## String Interpolation

Kotlin uses `sql { }` and Scala uses `sql""` to build fragments from string templates. Database values are embedded as typed, bound parameters — never concatenated into the SQL string.

> For a thorough explanation of how `sql { }` works internally and its thread safety guarantees, see [Kotlin String Interpolation](./kotlin-interpolation).

<Snippet file="core/FragmentBuilding" />

Inside the interpolation block, you can embed:

- **Bound values** — `${PgTypes.int4(userId)}` becomes a `?` parameter
- **Other fragments** — `${codec.columnList}`, `${Fragment.whereAnd(filters)}`, or another `sql { }` / `sql""` block are spliced into the SQL

## Builder Pattern

The builder pattern works in all languages and is useful for constructing fragments programmatically. Start from `Fragment.of("SELECT …")` (or `Fragment.builder()` for an empty start) and chain `.value(type, x)` / `.append(fragment)`:

<Snippet file="core/FragmentBuilderBasic" />

:::tip Which style should I use?
- **Kotlin** — Use `sql { }` for queries where all values are known. Use the builder pattern when you need to compose fragments programmatically.
- **Scala** — Same guidance, using `sql""` instead of `sql { }`.
- **Java** — Use the builder pattern for everything (no string interpolation available).
:::

## Composing Fragments

For dynamic queries — searches with optional filters, conditional clauses — chain `.optionally(value).append(sql, type)` onto a base fragment. Each `optionally` is a branch point [Query Analysis](./query-analysis) expands into all 2<sup>N</sup> possible SQL shapes, so every code path is verified against the schema:

<Snippet file="core/FragmentComposing" />

For genuinely list-shaped composition — joining a `List<Fragment>` from arbitrary sources — static factories like `Fragment.whereAnd()`, `Fragment.set()`, and `Fragment.comma()` handle SQL syntax (commas, AND/OR separators, SET clauses) so you don't have to:

<Snippet file="core/FragmentCombinators" />

Other useful combinators: `Fragment.and()`, `Fragment.or()`, `Fragment.whereOr()`, `Fragment.orderBy()`, `Fragment.comma()`, `Fragment.parentheses()`.

> See [Dynamic Queries](./dynamic-queries) for a deeper comparison of the two styles and when to reach for each — including the trade-offs around Query Analysis coverage.

## IN-clause helper

For `IN` clauses against dialects without native array types (MariaDB, SQL Server, Oracle, DB2), `Fragment.valuesList(type, values)` emits `(?, ?, …)` with each value bound as a typed parameter:

```java
Fragment.of("SELECT * FROM emp WHERE id IN ")
    .append(Fragment.valuesList(MariaTypes.int_, List.of(1, 2, 3)))
// SELECT * FROM emp WHERE id IN (?, ?, ?)
```

On PostgreSQL or DuckDB, prefer the native array idiom: `.value(int4.array(), ids)` with `WHERE id = ANY(?)`.

`valuesList` throws `IllegalArgumentException` on an empty list — an empty `IN()` is SQL-invalid, so the caller has to branch (typically: return an empty result without issuing the query).
