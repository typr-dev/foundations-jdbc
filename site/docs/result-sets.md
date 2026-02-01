---
title: Result Sets
---

import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

# Result Sets

Result set parsers handle the full lifecycle of reading from a ResultSet. They build on top of [row parsers](./row-types) to provide convenient ways to consume query results.

## ResultSetParser

A `ResultSetParser<T>` reads a complete ResultSet and produces a value of type `T`. You typically create one from a `RowParser`:

<Tabs groupId="language">
<TabItem value="java" label="Java">

```java
// Parse a single optional result
ResultSetParser<Optional<Person>> singleParser = personParser.singleOpt();

// Parse all results as a list
ResultSetParser<List<Person>> listParser = personParser.list();

// Execute with automatic resource management
Optional<Person> person = singleParser.parse(resultSet);
```

</TabItem>
<TabItem value="kotlin" label="Kotlin">

```kotlin
// Parse a single optional result
val singleParser: ResultSetParser<Person?> = personParser.singleOpt()

// Parse all results as a list
val listParser: ResultSetParser<List<Person>> = personParser.list()

// Execute with automatic resource management
val person: Person? = singleParser.parse(resultSet)
```

</TabItem>
<TabItem value="scala" label="Scala">

```scala
// Parse a single optional result
val singleParser: ResultSetParser[Option[Person]] = personParser.singleOpt()

// Parse all results as a list
val listParser: ResultSetParser[List[Person]] = personParser.list()

// Execute with automatic resource management
val person: Option[Person] = singleParser.parse(resultSet)
```

</TabItem>
</Tabs>

## Available Parsers

From any `RowParser<T>` you can create:

| Method | Returns | Description |
|--------|---------|-------------|
| `.list()` | `List<T>` | All rows as a list |
| `.singleOpt()` | `Optional<T>` / `T?` / `Option[T]` | Zero or one row (throws if more than one) |
| `.single()` | `T` | Exactly one row (throws otherwise) |
