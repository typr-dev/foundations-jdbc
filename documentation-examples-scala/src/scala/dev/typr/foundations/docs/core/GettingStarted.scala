package dev.typr.foundations.docs.core
import dev.typr.foundationssc.*
import dev.typr.foundationssc.connect.*
import dev.typr.foundationssc.Fragment.sql
import dev.typr.foundationssc.data.*

@SuppressWarnings(Array("unused"))
object GettingStarted:
  case class City(name: String, population: Int)

  val cityParser: RowParser[City] = RowParser.builder[City]()
    .field(DuckDbTypes.varchar)(_.name)
    .field(DuckDbTypes.integer)(_.population)
    .build(City.apply)

  def setup(tx: Transactor): Unit =
    tx.transact { conn =>
      sql"CREATE TABLE city (name VARCHAR, population INTEGER)"
        .update().run(conn)
      sql"INSERT INTO city VALUES ('Oslo', 709037), ('Bergen', 291189)"
        .update().run(conn)
    }

  //start
  def example(): Unit =
    val tx =
      SimpleDataSource.create(
        DuckDbConfig.inMemory().build()
      ).transactor()

    val cities: List[City] =
      sql"""SELECT name, population
            FROM city
            ORDER BY population DESC"""
        .query(cityParser.all())
        .transact(tx)
  //stop
