---
title: Templates
---

import Snippet from '@site/src/components/Snippet';

# Templates

Templates let you define the SQL structure once and supply values later. Use `.param(type)` (without a value) to create a typed parameter hole. This produces a `Template` — a reusable operation factory that can be analyzed by [Query Analysis](./query-analysis) without providing concrete values:

<Snippet file="core/TemplateBasic" />

Fill the template with `.on(value)` to get a concrete operation.

## Mixing Bound and Unbound Parameters

You can mix `.value(type, value)` (bound immediately) with `.param(type)` (filled later) in the same fragment. Only the unbound parameters become template parameters:

<Snippet file="core/TemplateMixed" />

## Packaging Parameters in a Record

When a template has multiple parameters, use `.from()` to map a record's fields to the template parameters. This gives each parameter a name and lets callers pass a single object:

<Snippet file="core/TemplateFrom" />

`Template.From` implements `Template`, so it works with all combinators including [`.then()`](./composing-operations#data-flow-between-operations) for chaining operations and [`.optionally()`](#dynamic-templates) for dynamic queries.

## Batch Operations

Use a template with `.onMany()` to batch-insert or batch-update rows. The template defines the SQL once, and `.onMany()` executes it for each row using JDBC batch mode (`addBatch()` / `executeBatch()`):

<Snippet file="core/BatchOperations" />

Driver-level optimizations like `.reWriteBatchedInserts()` (PostgreSQL), `.useBulkStmts()` (MariaDB), and `.useBulkCopyForBatchInsert()` (SQL Server) must be enabled on the [connection config](./transactors#setting-up) for best performance.

For PostgreSQL high-throughput inserts, use [streaming inserts](./streaming-inserts) with the COPY protocol instead.

## Dynamic Templates

Build templates with optional predicates — each combination of present/absent filters produces a different SQL structure, and [Query Analysis](./query-analysis#dynamic-sql-analysis) verifies all of them automatically.

### The Problem

Search forms and filters often produce dynamic SQL. Each combination of present/absent predicates is a structurally different query. Without tooling, you either:

- **Build SQL strings manually** and lose type safety
- **Test only the combinations you think of**, missing edge cases
- **Analyze each permutation individually**, which doesn't scale

With N optional predicates, there are 2^N possible query structures. `.optionally()` lets you declare them inline, and [Query Analysis](./query-analysis) verifies all of them with a single `checker.check()` call.

### Single Optional Parameter

Use `.optionally()` with a parameterized fragment to create a filter that is included when a value is provided and skipped when absent:

<Snippet file="core/OptionalQueryBasic" />

The template parameter type reflects the optionality — `Optional<String>` in Java, `String?` in Kotlin, `Option[String]` in Scala.

### Boolean Flags

For SQL chunks without parameters (e.g., `AND active = TRUE`), pass a plain `Fragment` to `.optionally()`. The template parameter becomes a `Boolean` — `true` includes the chunk, `false` skips it:

```java
Template<Boolean, List<User>> activeUsers =
    Fragment.of("SELECT * FROM users WHERE 1=1")
        .optionally(Fragment.of(" AND active = TRUE"))
        .query(userCodec.all());
```

### Multiple Optional Parameters

Chain multiple `.optionally()` calls to build queries with many independent filters. Each adds a template parameter:

<Snippet file="core/OptionalQueryMulti" />

### Multi-Parameter Optionally

When an optional clause needs multiple parameters (e.g., a `BETWEEN` range), pass a multi-parameter builder. The grouped parameters are provided or omitted together as a single unit:

<Snippet file="core/OptionalQueryRange" />

### Packaging Filters in a Record

As the number of optional predicates grows, the raw template signature becomes unwieldy. Use `.from()` with getter references to map a record (or data class / case class) directly to template parameters — callers just pass the record:

<Snippet file="core/OptionalQueryFacade" />
