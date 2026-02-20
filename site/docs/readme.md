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
import dev.typr.foundations.*;           // Core: Fragment, RowCodec, Operation, Transactor, *Types
import dev.typr.foundations.connect.*;    // Connection: SimpleDataSource, *Config
import dev.typr.foundations.data.*;       // Data types: Json, Range, Uint4, etc.
```

</TabItem>
<TabItem value="kotlin" label="Kotlin">

```kotlin
import dev.typr.foundationskt.*         // Core: Fragment, RowCodec, Operation, Transactor, *Types
import dev.typr.foundationskt.connect.* // Connection: SimpleDataSource, *Config
import dev.typr.foundationskt.data.*    // Data types: Json, Range, Uint4, etc.
```

</TabItem>
<TabItem value="scala" label="Scala">

```scala
import dev.typr.foundationssc.*         // Core: Fragment, RowCodec, Operation, Transactor, *Types
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

Define a record, a row codec that maps columns to fields, and a query that returns typed results:

<Snippet file="core/FirstQuery" />

The `RowCodecNamed` maps database columns to record fields by name. Use `.all()` to collect all rows, `.exactlyOne()` for a single result, or `.maxOne()` for an optional result. See [Row Codecs](./row-codecs) for more.

## What's Next

**Continue reading:** [Fragments](./fragments) &rarr; [Row Codecs](./row-codecs) &rarr; [Templates](./templates) &rarr; [Operations](./operations) &rarr; [Query Analysis](./query-analysis)

**Jump to a topic:** [Transactors](./transactors) (connection management) &middot; [Database Types](./database-types) (type catalog)

## Full Examples

- [`example-kotlin`](https://github.com/typr-dev/foundations-jdbc/tree/main/example-kotlin) — DuckDB with domain types, repositories, services, and query analysis
- [`example-spring-boot`](https://github.com/typr-dev/foundations-jdbc/tree/main/example-spring-boot) — Java Spring Boot with HikariCP connection pooling
