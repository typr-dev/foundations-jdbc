---
title: Getting Started
slug: /
---

import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';
import Snippet from '@site/src/components/Snippet';
import DependencyTabs from '@site/src/components/DependencyTabs';
import ThemedImg from '@site/src/components/ThemedImg';

# Getting Started

A step-by-step introduction to Foundations JDBC. The library is [MIT-licensed](https://github.com/typr-dev/foundations-jdbc/blob/main/LICENSE) and open source.

## Dependencies

Pick the one dependency for your language:

<DependencyTabs java="foundations-jdbc" kotlin="foundations-jdbc-kotlin" scala="foundations-jdbc-scala_3" />

The Kotlin and Scala modules depend on the core transitively, so one dependency is enough.

## Imports

<Tabs groupId="lang">
<TabItem value="java" label="Java">

```java
import dev.typr.foundations.*;           // Core: Fragment, RowCodec, OperationRead, Transactor, *Types
import dev.typr.foundations.connect.*;    // Connection: ConnectionSource, *Config
import dev.typr.foundations.data.*;       // Data types: Json, Range, Uint4, etc.
```

</TabItem>
<TabItem value="kotlin" label="Kotlin">

```kotlin
import dev.typr.foundationskt.*         // Core: Fragment, RowCodec, OperationRead, Transactor, *Types
import dev.typr.foundationskt.connect.* // Connection: ConnectionSource, *Config
import dev.typr.foundationskt.data.*    // Data types: Json, Range, Uint4, etc.
```

</TabItem>
<TabItem value="scala" label="Scala">

```scala
import dev.typr.foundationssc.*         // Core: Fragment, RowCodec, OperationRead, Transactor, *Types
import dev.typr.foundationssc.connect.* // Connection: ConnectionSource, *Config
import dev.typr.foundationssc.data.*    // Data types: Json, Range, Uint4, etc.
```

</TabItem>
</Tabs>

## Setting up a connection

The quickest way to get started is with DuckDB in-memory. DuckDB is an embedded database that requires no Docker, no server, and no setup. Add the dependency and go:

<Snippet file="core/GettingStarted" />

For production connection setup with PostgreSQL, MariaDB, and other databases, see [Transactors](./transactors).

## Your first query

Define a record, a row codec that maps columns to fields, and a query that returns typed results:

<Snippet file="core/FirstQuery" />

The `RowCodecNamed` maps database columns to record fields by name. Use `.all()` to collect all rows, `.exactlyOne()` for a single result, or `.maxOne()` for an optional result. See [Row Codecs](./row-codecs) for more.

## Parameterized queries

Use your types and codecs in a plain function to build parameterized queries:

<Snippet file="core/ParameterizedQuery" />

Values are always bound as JDBC parameters, never concatenated into SQL strings.

## Query analysis

[Query Analysis](./query-analysis) catches type mismatches, nullability errors, and missing columns at test time:

<ThemedImg light="/img/qa-type-mismatch-light.png" dark="/img/qa-type-mismatch-dark.png" alt="Query analysis catches type mismatches at test time" style={{maxWidth: '600px'}} />

## What's next

![Concepts Flow](/img/concepts-flow.svg)

**Continue reading:** [Fragments](./fragments) &rarr; [Row Codecs](./row-codecs) &rarr; [Operations](./operations) &rarr; [Transactors](./transactors) &rarr; [Query Analysis](./query-analysis)

**Jump to a topic:** [Database Types](./database-types) (type catalog) &middot; [Dynamic Queries](./dynamic-queries) (optional filters &amp; conditional clauses)

## Full examples

- [`example-kotlin`](https://github.com/typr-dev/foundations-jdbc/tree/main/example-kotlin) — DuckDB with domain types, repositories, services, and query analysis
- [`example-spring-boot`](https://github.com/typr-dev/foundations-jdbc/tree/main/example-spring-boot) — Java Spring Boot with HikariCP connection pooling
