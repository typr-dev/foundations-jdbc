---
title: Getting Started
slug: /
---

import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';
import Snippet from '@site/src/components/Snippet';

# Getting Started

A step-by-step introduction to Foundations JDBC — from setup to executing queries. Foundations JDBC is [MIT-licensed](https://github.com/typr-dev/foundations-jdbc/blob/main/LICENSE) and open source.

## Dependencies

Pick the one dependency for your language:

<Tabs groupId="lang">
<TabItem value="java" label="Java">

```kotlin
// build.gradle.kts
implementation("dev.typr:foundations-jdbc:1.0.0-M1")
```

```xml
<!-- pom.xml -->
<dependency>
    <groupId>dev.typr</groupId>
    <artifactId>foundations-jdbc</artifactId>
    <version>1.0.0-M1</version>
</dependency>
```

</TabItem>
<TabItem value="kotlin" label="Kotlin">

```kotlin
// build.gradle.kts
implementation("dev.typr:foundations-jdbc-kotlin:1.0.0-M1")
```

```xml
<!-- pom.xml -->
<dependency>
    <groupId>dev.typr</groupId>
    <artifactId>foundations-jdbc-kotlin</artifactId>
    <version>1.0.0-M1</version>
</dependency>
```

</TabItem>
<TabItem value="scala" label="Scala">

```kotlin
// build.gradle.kts
implementation("dev.typr:foundations-jdbc-scala_3:1.0.0-M1")
```

```xml
<!-- pom.xml -->
<dependency>
    <groupId>dev.typr</groupId>
    <artifactId>foundations-jdbc-scala_3</artifactId>
    <version>1.0.0-M1</version>
</dependency>
```

</TabItem>
</Tabs>

Each module includes everything you need — the Kotlin and Scala modules depend on the core transitively.

## Imports

<Tabs groupId="lang">
<TabItem value="java" label="Java">

```java
import dev.typr.foundations.*;           // Core: Fragment, RowParser, Operation, Transactor, *Types
import dev.typr.foundations.connect.*;    // Connection: SimpleDataSource, *Config
import dev.typr.foundations.data.*;       // Data types: Json, Range, Uint4, etc.
```

</TabItem>
<TabItem value="kotlin" label="Kotlin">

```kotlin
import dev.typr.foundationskt.*         // Core: Fragment, RowParser, Operation, Transactor, *Types
import dev.typr.foundationskt.connect.* // Connection: SimpleDataSource, *Config
import dev.typr.foundationskt.data.*    // Data types: Json, Range, Uint4, etc.
```

</TabItem>
<TabItem value="scala" label="Scala">

```scala
import dev.typr.foundationssc.*         // Core: Fragment, RowParser, Operation, Transactor, *Types
import dev.typr.foundationssc.connect.* // Connection: SimpleDataSource, *Config
import dev.typr.foundationssc.data.*    // Data types: Json, Range, Uint4, etc.
```

</TabItem>
</Tabs>

## Setting Up a Connection

The quickest way to get started is with DuckDB in-memory using `SingleConnectionDataSource`. DuckDB is an embedded database that requires no Docker, no server, and no setup — just add the dependency and go:

<Snippet file="core/GettingStarted" />

For production connection setup with PostgreSQL, MariaDB, and other databases, see [Transactors](./transactors).

## Types

A `DbType<A>` models a single database column type — it knows how to write a Java/Kotlin/Scala value into a PreparedStatement and read it back from a ResultSet. Each supported database has its own set of types (`PgTypes`, `DuckDbTypes`, `MariaDbTypes`, etc.) that map database-specific SQL types to JVM types with full precision.

See the [Database Types](./database-types) pages for the complete type catalog for each database.

## Fragments

Fragments let you build SQL queries safely with type-checked parameters. Parameters are always bound via prepared statements — never interpolated into the SQL string:

<Snippet file="core/FragmentBuilding" />

Fragments compose naturally for dynamic queries:

<Snippet file="core/FragmentComposing" />

See [Fragments](./fragments) for the full chaining API, string interpolation, and builder pattern details.

## Row Parsers

A `RowParser<T>` knows how to read a complete row from a ResultSet and construct an instance of `T`. It also knows how to decompose `T` back into column values for writing.

<Snippet file="core/RowParserBasic" />

### How It Works

The `RowParser.builder()` pattern takes:

1. **Fields** — each `.field(dbType, getter)` defines a column with its database type and how to extract that value from the row type.
2. **Constructor** — `.build(constructor)` takes a function that receives the typed column values and returns your row type. For records/case classes, just use `::new` or `apply`.

The builder is fully type-safe: the constructor function receives exactly the types you declared, with no casts needed. The parser uses column-index-based reading (not column names), which is both faster and catches schema mismatches at parse time.

### Single-Column Parser

For single-column queries, use the simpler `of()` factory:

<Snippet file="core/SingleColumnParser" />

### Nullable Columns

Use `.opt()` to wrap a type for nullable columns:

<Snippet file="core/NullableColumns" />

For named row parsers with column metadata, data-driven inserts, and composing parsers for joins, see [Named Row Parsers](./named-row-parsers).

## Executing Queries

A fragment becomes an operation once you specify how to read the results:

| Method | Returns |
|--------|---------|
| `.query(parser)` | `Operation<T>` — a SELECT that reads rows using the given result set parser |
| `.update()` | `Operation<Int>` — an INSERT/UPDATE/DELETE returning the affected row count |

### Result Set Parsers

A `ResultSetParser<T>` reads a complete ResultSet and produces a value of type `T`. You typically create one from a `RowParser`:

<Snippet file="core/ResultSetParserUsage" />

From any `RowParser<T>` you can create:

| Method | Returns | Description |
|--------|---------|-------------|
| `.all()` | `List<T>` | All rows as a list |
| `.maxOne()` | `Optional<T>` / `T?` / `Option[T]` | Zero or one row (throws if more than one) |
| `.exactlyOne()` | `T` | Exactly one row (throws otherwise) |

### Running Operations

The transactor manages connections and transactions. Call `.transact` to obtain a connection, run your code, and commit:

<Snippet file="core/ExecuteTransact" />

For multiple operations in a single transaction, call `.run(conn)` on each one inside the same block:

<Snippet file="core/ManualTransaction" />

Or compose operations as values with `.with()` and run the combined result:

<Snippet file="core/ExecuteComposed" />

See [Composing Operations](./composing-operations) for the full set of combinators.

## Full Example

The [`example-kotlin`](https://github.com/typr-dev/foundations-jdbc/tree/main/example-kotlin) project is a complete working application using DuckDB with domain types, repositories, services, and query analysis. It demonstrates how all the pieces fit together in practice.
