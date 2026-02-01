---
title: Row Types & Parsers
---

import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

# Row Types & Parsers

Row parsers define how to read a complete row from a ResultSet. They're composable and type-safe.

## Defining a Row Parser

A `RowParser<T>` knows how to read all columns of a row and construct an instance of `T`. It also knows how to decompose `T` back into column values for writing.

<Tabs groupId="language">
<TabItem value="java" label="Java">

```java
RowParser<Person> personParser = RowParsers.of(
    PgTypes.int4,           // id
    PgTypes.text,           // name
    PgTypes.timestamptz,    // createdAt
    Person::new,
    person -> new Object[]{person.id(), person.name(), person.createdAt()}
);

List<Person> people = personParser.parseList(resultSet);
```

</TabItem>
<TabItem value="kotlin" label="Kotlin">

```kotlin
val personParser: RowParser<Person> = RowParsers.of(
    PgTypes.int4,           // id
    PgTypes.text,           // name
    PgTypes.timestamptz,    // createdAt
    ::Person,
    { person -> arrayOf(person.id, person.name, person.createdAt) }
)

val people: List<Person> = personParser.parseList(resultSet)
```

</TabItem>
<TabItem value="scala" label="Scala">

```scala
val personParser: RowParser[Person] = RowParsers.of(
  PgTypes.int4,           // id
  PgTypes.text,           // name
  PgTypes.timestamptz,    // createdAt
  Person.apply,
  person => Array(person.id, person.name, person.createdAt)
)

val people: List[Person] = personParser.parseList(resultSet)
```

</TabItem>
</Tabs>

## How It Works

The `RowParsers.of(...)` factory takes:

1. **Database types** — one `DbType<T>` per column, in order. These know how to read and write values correctly for the target database.
2. **Constructor** — a function that takes the column values and returns your row type.
3. **Destructor** — a function that takes your row type and returns the column values as an array.

The parser uses column-index-based reading (not column names), which is both faster and catches schema mismatches at parse time rather than silently returning wrong data.

## Nullable Columns

Use `.opt()` to wrap a type for nullable columns:

<Tabs groupId="language">
<TabItem value="java" label="Java">

```java
RowParser<Person> personParser = RowParsers.of(
    PgTypes.int4,
    PgTypes.text,
    PgTypes.timestamptz.opt(),  // Optional<OffsetDateTime>
    Person::new,
    person -> new Object[]{person.id(), person.name(), person.createdAt()}
);
```

</TabItem>
<TabItem value="kotlin" label="Kotlin">

```kotlin
val personParser: RowParser<Person> = RowParsers.of(
    PgTypes.int4,
    PgTypes.text,
    PgTypes.timestamptz.opt(),  // OffsetDateTime? in Kotlin
    ::Person,
    { person -> arrayOf(person.id, person.name, person.createdAt) }
)
```

</TabItem>
<TabItem value="scala" label="Scala">

```scala
val personParser: RowParser[Person] = RowParsers.of(
  PgTypes.int4,
  PgTypes.text,
  PgTypes.timestamptz.opt(),  // Option[OffsetDateTime] in Scala
  Person.apply,
  person => Array(person.id, person.name, person.createdAt)
)
```

</TabItem>
</Tabs>

## Composing Parsers

Row parsers compose for joins. Left join gives you `Optional` on the right side:

<Tabs groupId="language">
<TabItem value="java" label="Java">

```java
RowParser<And<ProductRow, Optional<CategoryRow>>> joined =
    ProductRow.rowParser.leftJoined(CategoryRow.rowParser);
```

</TabItem>
<TabItem value="kotlin" label="Kotlin">

```kotlin
val joined: RowParser<And<ProductRow, ProductRow?>> =
    ProductRow.rowParser.leftJoined(CategoryRow.rowParser)
```

</TabItem>
<TabItem value="scala" label="Scala">

```scala
val joined: RowParser[And[ProductRow, Option[CategoryRow]]] =
    ProductRow.rowParser.leftJoined(CategoryRow.rowParser)
```

</TabItem>
</Tabs>
