---
title: Fragments
---

import Snippet from '@site/src/components/Snippet';

# Fragments

A Fragment is a composable SQL building block — it holds a SQL string together with its bound parameters. There are two ways to build fragments: **string interpolation** (Kotlin and Scala) and the **builder pattern** (all languages).

:::tip Which style should I use?
- **Kotlin** — Use `Sql { }` for queries where all values are known. Use the builder pattern (or the [hybrid approach](#hybrid-approach)) when you need parameter holes for [SQL Templates](./sql-templates).
- **Scala** — Same guidance, using `sql""` instead of `Sql { }`.
- **Java** — Use the builder pattern for everything (no string interpolation available).
:::

## String Interpolation

Kotlin uses `Sql { }` and Scala uses `sql""` to build fragments from string templates. Database values are embedded as typed, bound parameters — never concatenated into the SQL string.

> For a thorough explanation of how `Sql { }` works internally and its thread safety guarantees, see [Kotlin String Interpolation](./kotlin-interpolation).

<Snippet file="core/FragmentBuilding" />

Inside the interpolation block, you can embed:

- **Bound values** — `${PgTypes.int4(userId)}` becomes a `?` parameter
- **Other fragments** — `${parser.columnList}` or `${Fragment.whereAnd(filters)}` are spliced into the SQL
- **Nested blocks** — fragments built with `Sql { }` / `sql""` compose naturally

This makes dynamic query composition concise:

<Snippet file="core/FragmentComposing" />

## Builder Pattern

The builder pattern works in all languages and is required when creating [SQL Templates](./sql-templates) with parameter holes:

<Snippet file="core/SqlTemplateBasic" />

Use the builder when you need:

- **Parameter holes** via `.param(type)` for SQL Templates
- **Named row parser features** like `.row()` and `.paramRow()`
- **Java code** (no string interpolation available)

## Hybrid Approach

For SQL Templates in Kotlin and Scala, combine both styles — use `Sql { }` / `sql""` for the static SQL and chain `.param()` for the typed parameter holes:

<Snippet file="core/SqlTemplateMixed" />

## Chaining Reference

Fragments are self-composing — every combinator returns a new `Fragment`:

| Method | Description |
|--------|-------------|
| `.append(string)` | Append a literal SQL string |
| `.value(type, value)` | Append a bound parameter |
| `.append(fragment)` | Append another fragment (e.g. `columnList`, `whereAnd()`) |
| `.appendAll(fragments, separator)` | Append multiple fragments joined by a separator |
| `.row(parser, value)` | Append all columns of a named row parser as bound parameters |
| `.paramRow(parser)` | Append all columns of a named row parser as parameter holes |
| `.param(type)` | Create a single parameter hole for [SQL Templates](./sql-templates) |

## Static Factories

| Method | Description |
|--------|-------------|
| `Fragment.of(sql)` | Create a fragment from a literal SQL string |
| `Fragment.concat(fragments...)` | Concatenate multiple fragments |
| `Fragment.whereAnd(filters)` | Build a `WHERE` clause with `AND`-joined filters |
| `Fragment.whereOr(filters)` | Build a `WHERE` clause with `OR`-joined filters |

## Terminals

| Method | Returns |
|--------|---------|
| `.query(parser)` | `Operation<T>` — a SELECT that reads rows using the given result set parser |
| `.update()` | `Operation<Int>` — an INSERT/UPDATE/DELETE returning the affected row count |
| `.execute()` | `Operation<Void>` — like `.update()` but discards the row count |
