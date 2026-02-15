---
title: Named Row Parsers
---

import Snippet from '@site/src/components/Snippet';

# Named Row Parsers

A `RowParserNamed<T>` extends `RowParser<T>` with column name metadata. This enables data-driven inserts, JSON codecs, and composable parsers for joins.

## Defining a Named Row Parser

Use `namedBuilder()` instead of `builder()` and provide a name for each field:

<Snippet file="core/NamedRowParser" />

Since the parser knows its column names, it can derive things that would otherwise require hand-written strings:

- **`columnList()`** — comma-joined column names as a `Fragment`, for composing into SQL queries
- **`columnNames()`** — column names as a list
- **`fragment.row(parser, value)`** — emit all columns as comma-separated parameters
- **`DbJsonRow.jsonObject(parser)`** — JSON object codec with column names as keys

## Data-Driven Inserts

Named parsers enable a pattern where `fragment.row()` emits an object's fields as parameters, driven by the parser's column and type metadata. Pass column names to `except` to skip columns handled by the database:

<Snippet file="core/FragmentRow" />

## JSON Object Codecs

With an unnamed parser, `jsonArray` produces positional JSON arrays (`[value1, value2, ...]`). A named parser additionally enables `jsonObject`, which produces keyed JSON objects (`{"column": value, ...}`) using the parser's column names — no separate name list to maintain:

<Snippet file="core/NamedJsonObject" />

## Composing Parsers

Row parsers compose for joins. The result type is `And<A, B>` in Java (with `.left()` and `.right()` accessors), `Pair<A, B>` in Kotlin, and a tuple `(A, B)` in Scala. Left join wraps the right side in `Optional` (or nullable in Kotlin, `Option` in Scala).

Composition is safe regardless of column naming — all row parsers use index-based column reading, so column name clashes between joined tables don't cause conflicts:

<Snippet file="core/ComposingParsers" />
