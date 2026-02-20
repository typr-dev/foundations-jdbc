---
title: Kotlin String Interpolation
---

# Kotlin String Interpolation

`Sql { }` provides type-safe string interpolation for building SQL fragments in Kotlin. Write SQL with Kotlin's native `${}` syntax — every value becomes a prepared statement parameter, never concatenated into the SQL string. No other Kotlin database library offers this.

```kotlin
val frag = Sql { "SELECT * FROM users WHERE id = ${PgTypes.int4(userId)}" }
// Produces: SELECT * FROM users WHERE id = ?
// With userId bound as a typed parameter
```

## Basic Usage

Bind values by calling a `DbType` as a function inside `Sql { }`:

```kotlin
val userId = 42
val frag = Sql { "SELECT * FROM users WHERE id = ${PgTypes.int4(userId)}" }
```

Multiple parameters work naturally:

```kotlin
val frag = Sql {
    "SELECT * FROM t WHERE a = ${PgTypes.int4(1)} AND b = ${PgTypes.text("hello")}"
}
```

Nullable values use the nullable type variant:

```kotlin
val frag = Sql {
    "SELECT * FROM t WHERE name = ${PgTypes.text.opt()(optionalName)}"
}
```

Queries without parameters pass through unchanged:

```kotlin
val frag = Sql { "SELECT 1" }
// Produces: SELECT 1 (no parameters)
```

## Fragment Embedding

Any `Fragment` can be embedded inside `Sql { }` — its SQL is spliced directly into the result. This works because `Fragment.toString()` detects the active `SqlContext` and registers itself for splicing instead of returning rendered SQL.

Embed a `columnList` from a named row codec:

```kotlin
val codec: RowCodecNamed<User> = RowCodec.namedBuilder<User>()
    .field("id", PgTypes.int4, User::id)
    .field("name", PgTypes.text, User::name)
    .build(::User)

val frag = Sql { "SELECT ${codec.columnList} FROM users WHERE id = ${PgTypes.int4(userId)}" }
// Produces: SELECT id, name FROM users WHERE id = ?
```

Embed a `whereAnd` clause:

```kotlin
val filters = listOf(
    Sql { "age > ${PgTypes.int4(18)}" },
    Sql { "active = ${PgTypes.bool(true)}" }
)
val frag = Sql { "SELECT * FROM users ${Fragment.whereAnd(filters)}" }
// Produces: SELECT * FROM users WHERE (age > ?) AND (active = ?)
```

Embed a literal fragment:

```kotlin
val table = Fragment.of("users")
val frag = Sql { "SELECT * FROM $table WHERE id = ${PgTypes.int4(1)}" }
// Produces: SELECT * FROM users WHERE id = ?
```

## Composing Dynamic Queries

Build filter lists conditionally and combine them:

```kotlin
val filters = mutableListOf<Fragment>()

if (name != null) {
    filters += Sql { "name = ${PgTypes.text(name)}" }
}
if (minAge != null) {
    filters += Sql { "age >= ${PgTypes.int4(minAge)}" }
}
if (active) {
    filters += Fragment.of("active = true")
}

val query = Sql { "SELECT * FROM users ${Fragment.whereAnd(filters)}" }
    .query(userCodec.list())
```

Append fragments incrementally:

```kotlin
var frag = Sql { "SELECT * FROM users" }

if (filters.isNotEmpty()) {
    frag = Sql { "$frag ${Fragment.whereAnd(filters)}" }
}

frag = Sql { "$frag ORDER BY created_at DESC" }
```

## What Not to Do

**Do not capture fragment references across threads within a single `Sql { }` block.** The ThreadLocal context belongs to the thread executing the block. Since the block is not a suspend function, this is not something you can accidentally do — Kotlin prevents it structurally.

**Do not use `Sql { }` inside a suspend function where you expect suspension between `${}` expressions.** This is also impossible by design — the block parameter is `() -> String`, not `suspend () -> String`, so the compiler rejects any attempt to call suspending functions inside it.

**Do not rely on `toString()` for SQL output when a `SqlContext` is active.** Inside `Sql { }`, calling `toString()` on a fragment registers it for splicing rather than returning its SQL. This is the intended behavior for embedding, but if you need the rendered SQL for logging inside a `Sql { }` block, use `render()` instead.

## Under the Hood

### How It Works

The `Sql` object holds a `ThreadLocal<SqlContext>`. When you call `Sql { block }`:

1. A fresh `SqlContext` is created and stored in the ThreadLocal.
2. The `block` lambda executes. Each `${DbType.invoke(value)}` call creates a `Fragment` holding the bound parameter. That fragment's `toString()` detects the active context and registers itself, returning a null-character placeholder (`\u0000index\u0000`).
3. After the block returns the interpolated string, `buildFragment()` splits it on the null-character delimiters, replacing each placeholder with its registered fragment.
4. The ThreadLocal is cleared in a `finally` block.

The result is a `Fragment` with proper SQL and correctly bound parameters — no string concatenation of user values ever occurs.

### Why It Is Safe

**Inline function** — `Sql.invoke` is declared `inline`, so the block runs on the same thread with no suspension points. The ThreadLocal is set, used, and cleared within a single uninterruptible sequence.

**Non-suspending block** — The block parameter is `() -> String`, not a suspend function. Kotlin's compiler prevents you from calling suspending functions inside it, which means the thread cannot change between setting and clearing the context.

**ThreadLocal isolation** — Each thread gets its own `SqlContext`. Concurrent calls to `Sql { }` on different threads never interfere with each other. This has been validated with 1,000 concurrent virtual threads and 1,000 concurrent coroutines, both with and without actual DuckDB query execution.

### Edge Cases

**`Fragment.toString()` outside `Sql { }`** — When there is no active `SqlContext`, `toString()` simply returns the rendered SQL string. There is no side effect and no registration occurs. This means you can safely log or inspect fragments without triggering any context-related behavior.

**Nested `Sql { }` calls** — An inner `Sql { }` creates its own `SqlContext`, replacing the outer one on the ThreadLocal for the duration of the inner block. When the inner block completes, the ThreadLocal is cleared. The outer block's context is no longer on the ThreadLocal at that point, but this is safe because the inner call produces a `Fragment`, and that fragment's `toString()` is evaluated by Kotlin's string interpolation, which happens after the inner `Sql { }` returns. In practice, nesting works correctly:

```kotlin
val inner = Sql { "id = ${PgTypes.int4(1)}" }
val outer = Sql { "SELECT * FROM t WHERE $inner AND name = ${PgTypes.text("test")}" }
// Produces: SELECT * FROM t WHERE id = ? AND name = ?
```

Note that in this example, `inner` is fully constructed before `outer` starts — there is no true nesting of active contexts. The inner fragment is embedded into the outer one via `toString()`.

**Parameters at boundaries** — Parameters at the very start or end of the SQL string, or consecutive parameters without intervening text, all work correctly:

```kotlin
val start = Sql { "${PgTypes.int4(1)} + 2" }          // ?::INTEGER + 2
val end   = Sql { "SELECT ${PgTypes.int4(1)}" }        // SELECT ?::INTEGER
val adj   = Sql { "${PgTypes.int4(1)}${PgTypes.int4(2)}" } // ?::INTEGER?::INTEGER
```
