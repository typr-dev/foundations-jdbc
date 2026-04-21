---
title: Templates
---

import Snippet from '@site/src/components/Snippet';

# Templates

Most parameterized queries work fine as plain functions that build a Fragment with bound values. Upgrade to Templates when you need: batch execution with `.onMany()`, dynamic query variants with `.optionally()`, or reusable query values for [Query Analysis](./query-analysis).

Templates let you define the SQL structure once and supply values later. Use `.param(type)` (without a value) to create a typed parameter hole. This produces a `Template` — a reusable operation factory that can be analyzed by [Query Analysis](./query-analysis) without providing concrete values:

<Snippet file="core/TemplateBasic" />

Fill the template with `.on(value)` to get a concrete operation.

:::tip When to use Templates vs bound values
Use **bound values** (`.value(type, value)`, `sql { }`, `sql""`) when all values are known at definition time — they produce a `Fragment` directly.

Use **Templates** (`.param(type)`) when values come later — the SQL structure is fixed but values are supplied per-call. Templates also enable [batch operations](#batch-operations) and [dynamic queries](#dynamic-templates) with compile-time analysis of all variants.
:::

## Multiple Parameters

You can mix `.value(type, value)` (bound immediately) with `.param(type)` (filled later) in the same fragment. Only the unbound parameters become template parameters:

<Snippet file="core/TemplateMixed" />

## Packaging Parameters in a Record

When a template has multiple parameters, use `.from()` to map a record's fields to the template parameters. This gives each parameter a name and lets callers pass a single object:

<Snippet file="core/TemplateFrom" />

`Template.From` implements `Template`, so it works with all combinators including [`.then()`](./composing-operations#data-flow-between-operations) for chaining operations and [`.optionally()`](#dynamic-templates) for dynamic queries.

## Batch Operations

Use a template with `.onMany()` to batch-insert or batch-update rows. The template defines the SQL once, and `.onMany()` executes it for each row using JDBC batch mode (`addBatch()` / `executeBatch()`):

<Snippet file="core/BatchOperations" />

`.onMany()` returns `Optional<int[]>` (Java), `IntArray?` (Kotlin), or `Option[Array[Int]]` (Scala). The result is present when the driver reports per-row affected counts, and empty when it reports `SUCCESS_NO_INFO` — which happens when the driver rewrites the batch into a multi-row statement (e.g. PostgreSQL with `reWriteBatchedInserts=true`). An empty result means all rows succeeded but individual counts are unavailable. PgPipe always returns per-row counts.

Driver-level optimizations like `.reWriteBatchedInserts()` (PostgreSQL), `.useBulkStmts()` (MariaDB), and `.useBulkCopyForBatchInsert()` (SQL Server) must be enabled on the [connection config](./transactors#setting-up) for best performance.

For PostgreSQL high-throughput inserts, use [streaming inserts](./streaming-inserts) with the COPY protocol instead.

## Dynamic Templates

`.optionally()` wraps a fragment so it is included in the SQL when a value is present and omitted entirely when absent. Each combination of present/absent filters produces a structurally different query, and [Query Analysis](./query-analysis#dynamic-sql-analysis) verifies all of them automatically.

### Optional Filters

Use `.optionally()` with a parameterized fragment to create a filter that is included when a value is provided and skipped when absent:

<Snippet file="core/OptionalQueryBasic" />

The template parameter type reflects the optionality — `Optional<String>` in Java, `String?` in Kotlin, `Option[String]` in Scala.

:::note About `WHERE 1=1`
The `WHERE 1=1` pattern lets you unconditionally prefix every filter with `AND` — since `1=1` is always true, the query works correctly whether zero, one, or all filters are present. This avoids conditional logic to decide whether to emit `WHERE` or `AND`.

For non-template scenarios, `Fragment.whereAnd(filters)` handles this automatically.
:::

### Boolean Flags

For SQL chunks without parameters (e.g., `AND active = TRUE`), pass a plain `Fragment` to `.optionally()`. The template parameter becomes a `Boolean` — `true` includes the chunk, `false` skips it:

<Snippet file="core/OptionalQueryBooleanFlags" />

### Multiple Optional Filters

Chain multiple `.optionally()` calls to build queries with many independent filters. Each adds a template parameter:

<Snippet file="core/OptionalQueryMulti" />

With 3 optional filters, there are 2³ = 8 possible query structures. For example, calling the template above with two different inputs:

```
search.on("alice", null, true)
→ SELECT id, name, email FROM users WHERE 1=1 AND name ILIKE ? AND active = TRUE ORDER BY name

search.on(null, null, false)
→ SELECT id, name, email FROM users WHERE 1=1 ORDER BY name
```

`checker.check(search)` expands and verifies all 8 combinations against the database — you don't test them individually.

### Grouped Parameters

When an optional clause needs multiple parameters (e.g., a `BETWEEN` range), pass a multi-parameter builder. The grouped parameters are provided or omitted together as a single unit:

<Snippet file="core/OptionalQueryRange" />

### Packaging Filters in a Record

As the number of optional predicates grows, the raw template signature becomes unwieldy. Use `.from()` with getter references to map a record (or data class / case class) directly to template parameters — callers just pass the record:

<Snippet file="core/OptionalQueryFacade" />
