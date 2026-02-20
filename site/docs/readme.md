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

## Your First Query

Here's a complete end-to-end example: define a named row parser, create a table, insert some data, query it back — and verify that all your types match the database schema:

<Snippet file="core/FirstQuery" />

The last block uses [Query Analysis](./query-analysis) to verify that the column types in your row parser match the actual database columns. This catches type mismatches, nullability errors, and column count problems at test time instead of production. It's one of the most powerful features in the library — see [Query Analysis](./query-analysis) for the full story.

## What's Next

**Continue reading:** [Fragments](./fragments) &rarr; [Row Parsers](./row-parsers) &rarr; [SQL Templates](./sql-templates) &rarr; [Operations](./operations) &rarr; [Query Analysis](./query-analysis)

**Jump to a topic:** [Transactors](./transactors) (connection management) &middot; [Database Types](./database-types) (type catalog)

## Full Example

The [`example-kotlin`](https://github.com/typr-dev/foundations-jdbc/tree/main/example-kotlin) project is a complete working application using DuckDB with domain types, repositories, services, and query analysis. It demonstrates how all the pieces fit together in practice.
