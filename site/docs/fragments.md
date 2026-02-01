---
title: Fragments
---

import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

# Fragments

Fragments let you build SQL queries safely with type-checked parameters. Parameters are always bound via prepared statements — never interpolated into the SQL string.

## Building Fragments

<Tabs groupId="language">
<TabItem value="java" label="Java">

```java
Fragment query = Fragment.of("SELECT * FROM users WHERE id = ?")
    .param(PgTypes.int4, userId)
    .append(Fragment.of(" AND status = ?").param(PgTypes.text, "active"))
    .append(Fragment.of(" AND created_at > ?").param(PgTypes.timestamptz, cutoffDate));

// Execute safely — parameters are bound, not interpolated
List<User> users = query.query(userParser.list()).runUnchecked(connection);
```

</TabItem>
<TabItem value="kotlin" label="Kotlin">

```kotlin
val query = Fragment.of("SELECT * FROM users WHERE id = ?")
    .param(PgTypes.int4, userId)
    .append(Fragment.of(" AND status = ?").param(PgTypes.text, "active"))
    .append(Fragment.of(" AND created_at > ?").param(PgTypes.timestamptz, cutoffDate))

// Execute safely — parameters are bound, not interpolated
val users: List<User> = query.query(userParser.list()).runUnchecked(connection)
```

</TabItem>
<TabItem value="scala" label="Scala">

```scala
import dev.typr.foundations.scala.FragmentInterpolator.sql

val query = sql"""SELECT * FROM users
  WHERE id = ${PgTypes.int4.encode(userId)}
  AND status = ${PgTypes.text.encode("active")}
  AND created_at > ${PgTypes.timestamptz.encode(cutoffDate)}"""

// Execute safely
val users: List[User] = query.query(userParser.list()).runUnchecked(connection)
```

</TabItem>
</Tabs>

## Composing Fragments

Fragments can be combined to build dynamic queries:

<Tabs groupId="language">
<TabItem value="java" label="Java">

```java
// Build small reusable filters
Fragment byName(String name) {
    return Fragment.of("name ILIKE ?").param(PgTypes.text, name);
}
Fragment cheaperThan(BigDecimal max) {
    return Fragment.of("price < ?").param(PgTypes.numeric, max);
}

// Compose dynamically — only include the filters that are present
List<Fragment> filters = Stream.of(
        Optional.of(byName("%widget%")),
        maxPrice.map(this::cheaperThan)
    )
    .flatMap(Optional::stream)
    .toList();

List<ProductRow> products = Fragment.of("SELECT * FROM product ")
    .append(Fragment.whereAnd(filters))
    .query(ProductRow.rowParser.list())
    .run(tx);
```

</TabItem>
<TabItem value="kotlin" label="Kotlin">

```kotlin
// Build small reusable filters
fun byName(name: String) =
    Fragment.of("name ILIKE ?").param(PgTypes.text, name)

fun cheaperThan(max: BigDecimal) =
    Fragment.of("price < ?").param(PgTypes.numeric, max)

// Compose dynamically
val filters = listOfNotNull(
    byName("%widget%"),
    maxPrice?.let { cheaperThan(it) }
)

val products: List<ProductRow> = Fragment.of("SELECT * FROM product ")
    .append(Fragment.whereAnd(filters))
    .query(ProductRow.rowParser.list())
    .run(tx)
```

</TabItem>
<TabItem value="scala" label="Scala">

```scala
import dev.typr.foundations.scala.FragmentInterpolator.sql

// Build small reusable filters
def byName(name: String) =
    sql"name ILIKE ${PgTypes.text.encode(name)}"

def cheaperThan(max: BigDecimal) =
    sql"price < ${PgTypes.numeric.encode(max)}"

// Compose dynamically
val filters: List[Fragment] = List(
    Some(byName("%widget%")),
    maxPrice.map(cheaperThan)
).flatten

val products: List[ProductRow] =
    sql"SELECT * FROM product ${Fragment.whereAnd(filters)}"
        .query(ProductRow.rowParser.list())
        .run(tx)
```

</TabItem>
</Tabs>

## Executing Fragments

A fragment can be executed with a row parser to produce results:

| Method | Description |
|--------|-------------|
| `.query(parser).run(tx)` | Execute a SELECT query within a transactor |
| `.query(parser).runUnchecked(conn)` | Execute a SELECT query with a raw connection |
| `.update().run(tx)` | Execute an INSERT/UPDATE/DELETE |
