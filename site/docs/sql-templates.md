---
title: SQL Templates
---

import Snippet from '@site/src/components/Snippet';

# SQL Templates

In Kotlin and Scala, the recommended way to define templates is the **hybrid approach**: use `Sql { }` / `sql""` for the SQL text and chain `.param(type)` for the typed parameter holes. See [Fragments](./fragments) for details on the two fragment-building styles.

SQL Templates let you define the SQL structure once and supply values later. Use `.param(type)` (without a value) to create a typed parameter hole. This produces a `SqlTemplate` — a reusable operation factory that can be analyzed by [Query Analysis](./query-analysis) without providing concrete values:

<Snippet file="core/SqlTemplateBasic" />

Fill the template with `.on(value)` to get a concrete operation.

## Mixing Bound and Unbound Parameters

You can mix `.value(type, value)` (bound immediately) with `.param(type)` (filled later) in the same fragment. Only the unbound parameters become template parameters:

<Snippet file="core/SqlTemplateMixed" />

## Data Flow Between Operations

Use `.then()` to feed one operation's result into the next operation's template. The first operation runs, and its result becomes the input to the template:

<Snippet file="core/SqlTemplateThen" />

See [Composing Operations](./composing-operations) for the full set of combinators including `.with()`, `Operation.ifEmpty()`, and more.
