---
title: Kotlin String Interpolation
---

# Kotlin String Interpolation

`sql { }` provides type-safe string interpolation for building SQL fragments in Kotlin. Write SQL with Kotlin's native `${}` syntax; every value becomes a prepared statement parameter, never concatenated into the SQL string.

```kotlin
val frag = sql { "SELECT * FROM users WHERE id = ${PgTypes.int4(userId)}" }
// Produces: SELECT * FROM users WHERE id = ?
// With userId bound as a typed parameter
```

## Basic usage

Bind values by calling a `DbType` as a function inside `sql { }`:

```kotlin
val userId = 42
val frag = sql { "SELECT * FROM users WHERE id = ${PgTypes.int4(userId)}" }
```

Multiple parameters work naturally:

```kotlin
val frag = sql {
    "SELECT * FROM t WHERE a = ${PgTypes.int4(1)} AND b = ${PgTypes.text("hello")}"
}
```

Nullable values use the nullable type variant:

```kotlin
val frag = sql {
    "SELECT * FROM t WHERE name = ${PgTypes.text.opt()(optionalName)}"
}
```

Queries without parameters pass through unchanged:

```kotlin
val frag = sql { "SELECT 1" }
// Produces: SELECT 1 (no parameters)
```

## Fragment embedding

Any `Fragment` can be embedded inside `sql { }`. Its SQL is spliced directly into the result. This works because `Fragment.toString()` detects the active `SqlContext` and registers itself for splicing instead of returning rendered SQL.

Embed a `columnList` from a named row codec:

```kotlin
val codec: RowCodecNamed<User> = RowCodec.namedBuilder<User>()
    .field("id", PgTypes.int4, User::id)
    .field("name", PgTypes.text, User::name)
    .build(::User)

val frag = sql { "SELECT ${codec.columnList} FROM users WHERE id = ${PgTypes.int4(userId)}" }
// Produces: SELECT id, name FROM users WHERE id = ?
```

Embed a `whereAnd` clause:

```kotlin
val filters = listOf(
    sql { "age > ${PgTypes.int4(18)}" },
    sql { "active = ${PgTypes.bool(true)}" }
)
val frag = sql { "SELECT * FROM users ${Fragment.whereAnd(filters)}" }
// Produces: SELECT * FROM users WHERE (age > ?) AND (active = ?)
```

Embed a literal fragment:

```kotlin
val table = Fragment.of("users")
val frag = sql { "SELECT * FROM $table WHERE id = ${PgTypes.int4(1)}" }
// Produces: SELECT * FROM users WHERE id = ?
```

## Composing dynamic queries

Build filter lists conditionally and combine them:

```kotlin
val filters = mutableListOf<Fragment>()

if (name != null) {
    filters += sql { "name = ${PgTypes.text(name)}" }
}
if (minAge != null) {
    filters += sql { "age >= ${PgTypes.int4(minAge)}" }
}
if (active) {
    filters += Fragment.of("active = true")
}

val query = sql { "SELECT * FROM users ${Fragment.whereAnd(filters)}" }
    .query(userCodec.list())
```

Append fragments incrementally:

```kotlin
var frag = sql { "SELECT * FROM users" }

if (filters.isNotEmpty()) {
    frag = sql { "$frag ${Fragment.whereAnd(filters)}" }
}

frag = sql { "$frag ORDER BY created_at DESC" }
```

## What not to do

**Do not capture fragment references across threads within a single `sql { }` block.** The ThreadLocal context belongs to the thread running the block. Since the block is not a suspend function, this is not something you can accidentally do; Kotlin prevents it structurally.

**Do not use `sql { }` inside a suspend function where you expect suspension between `${}` expressions.** This is also impossible by design: the block parameter is `() -> String`, not `suspend () -> String`, so the compiler rejects any attempt to call suspending functions inside it.

**Do not rely on `toString()` for SQL output when a `SqlContext` is active.** Inside `sql { }`, calling `toString()` on a fragment registers it for splicing rather than returning its SQL. This is the intended behavior for embedding, but if you need the rendered SQL for logging inside a `sql { }` block, use `render()` instead.

## Under the hood

### How it works

The `sql` function uses a `ThreadLocal<SqlContext>`. When you call `sql { block }`:

1. A fresh `SqlContext` is created and stored in the ThreadLocal.
2. The `block` lambda executes. Each `${DbType.invoke(value)}` call creates a `Fragment` holding the bound parameter. That fragment's `toString()` detects the active context and registers itself, returning a null-character placeholder (`\u0000index\u0000`).
3. After the block returns the interpolated string, `buildFragment()` splits it on the null-character delimiters, replacing each placeholder with its registered fragment.
4. The ThreadLocal is restored to the previous context (or cleared if there was none) in a `finally` block.

The result is a `Fragment` with proper SQL and correctly bound parameters. No string concatenation of user values ever occurs.

### Why it is safe

**Inline function.** `sql` is declared `inline`, so the block runs on the same thread with no suspension points. The ThreadLocal is set, used, and cleared within a single uninterruptible sequence.

**Non-suspending block.** The block parameter is `() -> String`, not a suspend function. Kotlin's compiler prevents you from calling suspending functions inside it, which means the thread cannot change between setting and clearing the context.

**ThreadLocal isolation.** Each thread gets its own `SqlContext`. Concurrent calls to `sql { }` on different threads never interfere with each other. This has been validated with 1,000 concurrent virtual threads and 1,000 concurrent coroutines, both with and without actual DuckDB query execution.

### Edge cases

**`Fragment.toString()` outside `sql { }`.**  When there is no active `SqlContext`, `toString()` returns the rendered SQL string with no side effects.

**Nested `sql { }` calls.** `sql { }` blocks can be nested inline. An inner `sql { }` saves the outer context, creates its own, and restores the outer context when it completes. The inner call produces a `Fragment` whose `toString()` then registers it in the restored outer context:

```kotlin
val frag = sql { "SELECT * FROM t WHERE ${sql { "id = ${PgTypes.int4(1)}" }} AND name = ${PgTypes.text("test")}" }
// Produces: SELECT * FROM t WHERE id = ? AND name = ?
```

Sequential composition (building a fragment first, then embedding it) also works:

```kotlin
val inner = sql { "id = ${PgTypes.int4(1)}" }
val outer = sql { "SELECT * FROM t WHERE $inner AND name = ${PgTypes.text("test")}" }
// Produces: SELECT * FROM t WHERE id = ? AND name = ?
```

**Parameters at boundaries.** Parameters at the start or end of the SQL string, or consecutive parameters without intervening text, all work correctly:

```kotlin
val start = sql { "${PgTypes.int4(1)} + 2" }          // ?::INTEGER + 2
val end   = sql { "SELECT ${PgTypes.int4(1)}" }        // SELECT ?::INTEGER
val adj   = sql { "${PgTypes.int4(1)}${PgTypes.int4(2)}" } // ?::INTEGER?::INTEGER
```
