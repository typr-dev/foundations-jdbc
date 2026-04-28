---
title: Dynamic Queries
---

import Snippet from '@site/src/components/Snippet';
import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

# Dynamic Queries

SQL that varies at runtime (search forms, optional filters, sort direction toggles) has two patterns in foundations-jdbc. They look similar but interact with [Query Analysis](./query-analysis) very differently.

## TL;DR

| | `optionally` DSL | List + `whereAnd` |
|---|---|---|
| Branches are | static (chained on the fragment) | dynamic (built at runtime) |
| Query Analysis verifies | every possible variant (2<sup>N</sup> for N branches) | only the SQL shape your test happened to construct |
| Best for | optional filters known up front | filters from genuinely list-shaped sources |

For most filter-style queries, prefer `optionally`. It is the only style where Query Analysis can prove every code path is well-typed against the schema.

## The `optionally` DSL

Chain `.optionally(value).append(sql, type)` (or the no-bind / boolean variants) onto a base fragment. Each `optionally` is a branch point: present/absent for `Optional`, true/false for a `Boolean` flag.

<Snippet file="dynamic/WhenDsl" />

The SQL the runtime issues depends on the arguments. The SQL Query Analysis checks doesn't:

- 3 `optionally` calls → 2 × 2 × 2 = **8 SQL shapes** verified against the schema, parameter types, and column nullability
- Forget `.opt()` on a column that only appears in one branch? QA flags it
- Add a fourth filter that breaks the schema in some combination? QA fails the test that built the operation, before that branch ever gets exercised at runtime

In other words: **every code path is checked**, not just the one your test happened to take.

### Variants of optionally

```
.optionally(Optional<T>).append(sql, type)              // include with bound value, or skip
.optionally(Optional<T>).append(sql, type, fallbackSql) // include with bound value, or fallback SQL
.optionally(boolean)    .append(sql)                    // include, or skip
.optionally(boolean)    .append(sqlTrue, sqlFalse)      // choose between two SQL shapes
.optionally(boolean)    .append(fragmentTrue, fragmentFalse)
```

The `Optional<T>` variants always require a `DbType<T>` so the value is bound, never concatenated. The `boolean` variants are for switching SQL with no parameter to bind (sort direction, feature flags, conditional joins).

### Idiomatic Kotlin / Scala: extension methods

Wrapping each filter in an extension method on `Fragment` keeps the call site reading as domain verbs rather than `optionally`/`append` plumbing:

<Tabs groupId="lang">
<TabItem value="kotlin" label="Kotlin">

```kotlin
fun Fragment.matchingName(name: String?): Fragment =
    optionally(name).append(" AND name LIKE ", PgTypes.text)

fun Fragment.cheaperThan(max: BigDecimal?): Fragment =
    optionally(max).append(" AND price < ", PgTypes.numeric)

// Call site reads like domain verbs:
sql { "SELECT id, name, price FROM product WHERE 1 = 1" }
    .matchingName(name)
    .cheaperThan(maxPrice)
    .query(codec.all())
```

</TabItem>
<TabItem value="scala" label="Scala">

```scala
extension (f: Fragment)
  def matchingName(n: Option[String]): Fragment =
    f.optionally(n).append(" AND name LIKE ", PgTypes.text)
  def cheaperThan(max: Option[BigDecimal]): Fragment =
    f.optionally(max).append(" AND price < ", PgTypes.numeric)

// Call site reads like domain verbs:
sql"SELECT id, name, price FROM product WHERE 1 = 1"
  .matchingName(name)
  .cheaperThan(maxPrice)
  .query(codec.all())
```

</TabItem>
</Tabs>

The branch structure is preserved (Query Analysis still expands all variants), but the call site reads as filter verbs rather than `optionally`/`append` plumbing.

## The list pattern

When filters come from a genuinely list-shaped source (a search form posting an arbitrary set, an admin tool letting users compose conditions, code that loops over column metadata), assemble a `List<Fragment>` and join with `Fragment.whereAnd`:

<Snippet file="dynamic/ListBased" />

`whereAnd` emits ` WHERE x AND y AND z` for non-empty lists and `""` for empty ones, so the surrounding SQL doesn't need to branch.

This style is more flexible (you can build the list however you like), but the structure is opaque to Query Analysis.

## How Query Analysis sees each style

Query Analysis works by walking the fragment tree at scan/test time and verifying each variant against the database. The two styles produce different trees:

- **`optionally` DSL** produces a fragment tree with explicit `Branch` nodes. The analyzer expands every branch: 8 variants for 3 `optionally` calls, all checked.
- **`whereAnd(list)`** produces a flat concatenation. The analyzer sees one variant: whatever SQL was constructed when the test created the operation.

That means with `whereAnd`, **only the filter combination your test exercises is verified**. If production sometimes hits a runtime list shape your test never built, type errors in that combination won't be caught.

:::tip
You can still use `whereAnd` and get full QA coverage, but you have to enumerate the variants yourself, e.g., by parameterizing your test class so the scanner constructs each combination explicitly. The `optionally` DSL does this for you.
:::

## When to use which

**Use `optionally`** when:

- You have a small, statically known set of optional filters (most search/list endpoints).
- You want Query Analysis to verify every possible runtime SQL shape.
- You want each filter to read as a clear, named operation at the call site.

**Use `Fragment.whereAnd` (list)** when:

- The number or shape of filters is dynamic (driven by configuration, user-defined columns, recursive structure).
- You're stitching together fragments from multiple unrelated sources.
- You don't need exhaustive QA coverage of every runtime shape, OR you've structured your tests to enumerate variants explicitly.

In a typical CRUD/search application, `optionally` covers most cases; `whereAnd` handles the rest.
