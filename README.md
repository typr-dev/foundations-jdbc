# Foundations JDBC

A JDBC wrapper library that makes JDBC actually usable. We've modeled JDBC to perfection so you can finally use all column types correctly across all supported databases.

## Supported Databases

- **PostgreSQL** - arrays, ranges, JSON, geometric types, network types, and more
- **MariaDB/MySQL** - unsigned types, sets, spatial types, and JSON
- **DuckDB** - lists, maps, structs, unions, and nested types
- **Oracle** - OBJECT types, VARRAYs, nested tables, and intervals
- **SQL Server** - geography, geometry, hierarchyid, and all standard types

## Key Features

- **Type-safe database types** - each database has its own type hierarchy
- **Row parsers** - composable and type-safe result set parsing
- **SQL fragment API** - build SQL safely with type-checked parameters
- **JSON codecs** - dependency-free JSON serialization for all types
- **Streaming inserts** - efficient batch inserts without memory issues
- **No reflection** - GraalVM native-image compatible
- **Multi-language** - Java, Kotlin, and Scala support

## Quick Example

```java
// Define types
PgType<int[]> intArray = PgTypes.int4.array();
PgType<Range<LocalDate>> dateRange = PgTypes.daterange;

// Define a row parser
RowParser<Person> personParser = RowParsers.of(
    PgTypes.int4,           // id
    PgTypes.text,           // name
    PgTypes.timestamptz,    // createdAt
    Person::new,
    person -> new Object[]{person.id(), person.name(), person.createdAt()}
);

// Build and execute a query
Fragment query = Fragment.Builder()
    .sql("SELECT * FROM users WHERE id = ")
    .param(PgTypes.int4, userId)
    .done();

List<Person> people = query.query(personParser).runUnchecked(connection);
```

## Modules

| Module | Description |
|--------|-------------|
| `foundations-jdbc` | Core JDBC wrapper with type modeling for all databases |
| `foundations-jdbc-hikari` | HikariCP connection pool integration |
| `foundations-jdbc-scala_3` | Scala 3 string interpolator for SQL fragments |

## Documentation

Full documentation is available at [typr-dev.github.io/foundations-jdbc](https://typr-dev.github.io/foundations-jdbc/).

## Building

```bash
./gradlew compileJava compileScala compileTestJava
```

## License

[MIT](LICENSE)
