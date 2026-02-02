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
record Person(Integer id, String name, OffsetDateTime createdAt) {}

RowParser<Person> personParser = RowParser.<Person>builder()
    .field(PgTypes.int4, Person::id)
    .field(PgTypes.text, Person::name)
    .field(PgTypes.timestamptz, Person::createdAt)
    .build(Person::new);

List<Person> people = personParser.parseList(resultSet);
```

</TabItem>
<TabItem value="kotlin" label="Kotlin">

```kotlin
data class Person(val id: Int, val name: String, val createdAt: OffsetDateTime)

val personParser: RowParser<Person> = RowParser.builder<Person>()
    .field(PgTypes.int4, Person::id)
    .field(PgTypes.text, Person::name)
    .field(PgTypes.timestamptz, Person::createdAt)
    .build(::Person)

val people: List<Person> = personParser.parseList(resultSet)
```

</TabItem>
<TabItem value="scala" label="Scala">

```scala
case class Person(id: Int, name: String, createdAt: OffsetDateTime)

val personParser: RowParser[Person] = RowParser.builder[Person]()
  .field(PgTypes.int4, _.id)
  .field(PgTypes.text, _.name)
  .field(PgTypes.timestamptz, _.createdAt)
  .build(Person.apply)

val people: List[Person] = personParser.parseList(resultSet)
```

</TabItem>
</Tabs>

## How It Works

The `RowParser.builder()` pattern takes:

1. **Fields** — each `.field(dbType, getter)` defines a column with its database type and how to extract that value from the row type.
2. **Constructor** — `.build(constructor)` takes a function that receives the typed column values and returns your row type. For records/case classes, just use `::new` or `apply`.

The builder is fully type-safe: the constructor function receives exactly the types you declared, with no casts needed. The parser uses column-index-based reading (not column names), which is both faster and catches schema mismatches at parse time.

## Single-Column Parser

For single-column queries, use the simpler `of()` factory:

```java
RowParser<Integer> idParser = RowParser.of(PgTypes.int4);
```

## Nullable Columns

Use `.opt()` to wrap a type for nullable columns:

<Tabs groupId="language">
<TabItem value="java" label="Java">

```java
record Person(Integer id, String name, Optional<OffsetDateTime> createdAt) {}

RowParser<Person> personParser = RowParser.<Person>builder()
    .field(PgTypes.int4, Person::id)
    .field(PgTypes.text, Person::name)
    .field(PgTypes.timestamptz.opt(), Person::createdAt)
    .build(Person::new);
```

</TabItem>
<TabItem value="kotlin" label="Kotlin">

```kotlin
data class Person(val id: Int, val name: String, val createdAt: OffsetDateTime?)

val personParser: RowParser<Person> = RowParser.builder<Person>()
    .field(PgTypes.int4, Person::id)
    .field(PgTypes.text, Person::name)
    .field(PgTypes.timestamptz.opt(), Person::createdAt)
    .build(::Person)
```

</TabItem>
<TabItem value="scala" label="Scala">

```scala
case class Person(id: Int, name: String, createdAt: Option[OffsetDateTime])

val personParser: RowParser[Person] = RowParser.builder[Person]()
  .field(PgTypes.int4, _.id)
  .field(PgTypes.text, _.name)
  .field(PgTypes.timestamptz.opt(), _.createdAt)
  .build(Person.apply)
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
