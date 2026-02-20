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

The builder pattern works in all languages and is useful for constructing fragments programmatically:

<Snippet file="core/FragmentBuilderBasic" />

For parameterized templates with unfilled parameter holes, see [Templates](./templates).

:::tip Which style should I use?
- **Kotlin** — Use `sql { }` for queries where all values are known. Use the builder pattern when you need parameter holes for [Templates](./templates).
- **Scala** — Same guidance, using `sql""` instead of `sql { }`.
- **Java** — Use the builder pattern for everything (no string interpolation available).
:::

## Composing Fragments

Build small reusable fragments, then combine them into full queries. Static factories like `Fragment.whereAnd()`, `Fragment.set()`, and `Fragment.comma()` handle SQL syntax — commas, AND/OR separators, SET clauses — so you don't have to:

<Snippet file="core/FragmentComposing" />

The same approach works for UPDATE statements — build a list of assignments and let `Fragment.set()` join them:

<Snippet file="core/FragmentCombinators" />

Other useful combinators: `Fragment.and()`, `Fragment.or()`, `Fragment.whereOr()`, `Fragment.orderBy()`, `Fragment.comma()`, `Fragment.parentheses()`.
