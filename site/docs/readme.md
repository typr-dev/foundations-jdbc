---
title: Foundations JDBC
slug: /
---

import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

# Foundations JDBC

A JDBC wrapper library that makes JDBC actually usable. Every column type modeled correctly across all supported databases, with full roundtrip support.

## Getting Started

Add the dependency to your project:

<Tabs groupId="build">
<TabItem value="gradle" label="Gradle">

```kotlin
implementation("dev.typr.foundations:foundations-jdbc:version")
```

</TabItem>
<TabItem value="maven" label="Maven">

```xml
<dependency>
    <groupId>dev.typr.foundations</groupId>
    <artifactId>foundations-jdbc</artifactId>
    <version>version</version>
</dependency>
```

</TabItem>
</Tabs>

Language-specific DSL modules:

<Tabs groupId="build">
<TabItem value="gradle" label="Gradle">

```kotlin
// Kotlin DSL
implementation("dev.typr.foundations:foundations-jdbc-dsl-kotlin:version")

// Scala DSL
implementation("dev.typr.foundations:foundations-jdbc-dsl-scala_3:version")
```

</TabItem>
<TabItem value="maven" label="Maven">

```xml
<!-- Kotlin DSL -->
<dependency>
    <groupId>dev.typr.foundations</groupId>
    <artifactId>foundations-jdbc-dsl-kotlin</artifactId>
    <version>version</version>
</dependency>

<!-- Scala DSL -->
<dependency>
    <groupId>dev.typr.foundations</groupId>
    <artifactId>foundations-jdbc-dsl-scala_3</artifactId>
    <version>version</version>
</dependency>
```

</TabItem>
</Tabs>

## Core Concepts

- **[Row Types & Parsers](./row-types)** — Define how to read rows from a ResultSet with composable, type-safe parsers.
- **[Result Sets](./result-sets)** — Handle the full lifecycle of reading from a ResultSet.
- **[Fragments](./fragments)** — Build SQL safely with type-checked parameters.
- **[Transactors](./transactors)** — Manage connections and transactions.
