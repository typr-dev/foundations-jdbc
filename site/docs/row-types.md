---
title: Row Types & Parsers
---

import Snippet from '@site/src/components/Snippet';

# Row Types & Parsers

Row parsers define how to read a complete row from a ResultSet. They're composable and type-safe.

## Defining a Row Parser

A `RowParser<T>` knows how to read all columns of a row and construct an instance of `T`. It also knows how to decompose `T` back into column values for writing.

<Snippet file="core/RowParserBasic" />

## How It Works

The `RowParser.builder()` pattern takes:

1. **Fields** — each `.field(dbType, getter)` defines a column with its database type and how to extract that value from the row type.
2. **Constructor** — `.build(constructor)` takes a function that receives the typed column values and returns your row type. For records/case classes, just use `::new` or `apply`.

The builder is fully type-safe: the constructor function receives exactly the types you declared, with no casts needed. The parser uses column-index-based reading (not column names), which is both faster and catches schema mismatches at parse time.

## Single-Column Parser

For single-column queries, use the simpler `of()` factory:

<Snippet file="core/SingleColumnParser" />

## Nullable Columns

Use `.opt()` to wrap a type for nullable columns:

<Snippet file="core/NullableColumns" />

## Composing Parsers

Row parsers compose for joins. Left join gives you `Optional` on the right side:

<Snippet file="core/ComposingParsers" />
