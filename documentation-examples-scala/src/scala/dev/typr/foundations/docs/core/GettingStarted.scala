package dev.typr.foundations.docs.core
import dev.typr.foundationssc.*
import dev.typr.foundationssc.Fragment.sql
import dev.typr.foundationssc.data.*

@SuppressWarnings(Array("unused"))
object GettingStarted:
  case class City(name: String, population: Int)

  val cityParser: RowParser[City] = RowParser.builder[City]()
    .field(DuckDbTypes.varchar)(_.name)
    .field(DuckDbTypes.integer)(_.population)
    .build(City.apply)

  //start
  def example(): Unit =
    // Connect to an in-memory DuckDB database
    val tx =
      SimpleDataSource.create(
        DuckDbConfig.inMemory().build()
      ).transactor()

    // Create the table and insert data
    tx.transact { conn =>
      sql"CREATE TABLE city (name VARCHAR, population INTEGER)"
        .update().run(conn)
      sql"INSERT INTO city VALUES ('Oslo', 709037), ('Bergen', 291189)"
        .update().run(conn)
    }

    // Query with type-safe parameters
    val cities: List[City] =
      sql"""SELECT name, population
            FROM city
            ORDER BY population DESC"""
        .query(cityParser.all())
        .transact(tx)

    cities.foreach(c => println(s"${c.name}: ${c.population}"))
  //stop
