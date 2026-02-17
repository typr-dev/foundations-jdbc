---
title: Fragments
---

import Snippet from '@site/src/components/Snippet';

# Fragments

A Fragment is a composable SQL building block — it holds a SQL string together with its bound parameters. There are two ways to build fragments: **string interpolation** (Kotlin and Scala) and the **builder pattern** (all languages).

:::tip Which style should I use?
- **Kotlin** — Use `Sql { }` for queries where all values are known. Use the builder pattern when you need parameter holes for [SQL Templates](./sql-templates).
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

The builder pattern works in all languages and is useful for constructing fragments programmatically:

<Snippet file="core/FragmentBuilderBasic" />

For parameterized templates with unfilled parameter holes, see [SQL Templates](./sql-templates).

## Chaining Reference

Fragments are self-composing — every combinator returns a new `Fragment`:

| Method | Description |
|--------|-------------|
| `.append(string)` | Append a literal SQL string |
| `.value(type, value)` | Append a bound parameter |
| `.append(fragment)` | Append another fragment (e.g. `columnList`, `whereAnd()`) |
| `.appendAll(fragments, separator)` | Append multiple fragments joined by a separator |
| `.row(parser, value)` | Append all columns of a named row parser as bound parameters |
| `.paramRow(parser)` | Append all columns of a named row parser as parameter holes — see [SQL Templates](./sql-templates) |
| `.param(type)` | Create a single parameter hole — see [SQL Templates](./sql-templates) |

## Static Factories

| Method | Description |
|--------|-------------|
| `Fragment.of(sql)` | Create a fragment from a literal SQL string |
| `Fragment.empty()` | An empty fragment (no SQL, no parameters) |
| `Fragment.value(type, value)` | Create a single-parameter fragment |
| `Fragment.encode(type, value)` | Alias for `value` — Kotlin: `type(value)`, Scala: `type(value)` |
| `Fragment.concat(fragments...)` | Concatenate multiple fragments with no separator |
| `Fragment.join(fragments, separator)` | Join fragments with a separator |
| `Fragment.comma(fragments...)` | Join fragments with `, ` |
| `Fragment.and(fragments...)` | Join fragments with ` AND ` |
| `Fragment.or(fragments...)` | Join fragments with ` OR ` |
| `Fragment.set(fragments...)` | `SET ` prefix with `, `-joined fragments |
| `Fragment.orderBy(fragments...)` | `ORDER BY ` prefix with `, `-joined fragments |
| `Fragment.whereAnd(fragments...)` | `WHERE ` prefix with ` AND `-joined fragments |
| `Fragment.whereOr(fragments...)` | `WHERE ` prefix with ` OR `-joined fragments |
| `Fragment.parentheses(fragment)` | Wrap a fragment in `(` `)` |
| `Fragment.quotedDouble(name)` | Double-quote an identifier: `"name"` |
| `Fragment.quotedSingle(value)` | Single-quote a literal: `'value'` |

## Terminals

| Method | Returns |
|--------|---------|
| `.query(parser)` | `Operation<T>` — a SELECT that reads rows using the given result set parser |
| `.queryOne(type)` | `Operation<T>` — convenience for `.query(RowParser.of(type).exactlyOne())` |
| `.queryList(type)` | `Operation<List<T>>` — convenience for `.query(RowParser.of(type).all())` |
| `.queryMaybe(type)` | `Operation<Optional<T>>` / `Operation<T?>` / `Operation<Option[T]]>` — convenience for `.query(RowParser.of(type).maxOne())` |
| `.update()` | `Operation<Int>` — an INSERT/UPDATE/DELETE returning the affected row count |
| `.execute()` | `Operation<Void>` — like `.update()` but discards the row count |
