package dev.typr.foundations.docs.core
import dev.typr.foundationssc.*
import dev.typr.foundationssc.connect.*
import dev.typr.foundationssc.Fragment.sql

@SuppressWarnings(Array("unused"))
object FirstQuery:
  //start
  case class City(name: String, country: String, population: Int)

  val cityCodec: RowCodecNamed[City] =
    RowCodec.namedBuilder[City]()
      .field("name", DuckDbTypes.varchar)(_.name)
      .field("country", DuckDbTypes.varchar)(_.country)
      .field("population", DuckDbTypes.integer)(_.population)
      .build(City.apply)

  val findCities: Operation[List[City]] =
    sql"SELECT ${cityCodec.columnList} FROM city ORDER BY population DESC"
      .query(cityCodec.all())

  def example(): Unit =
    val tx = SimpleDataSource.create(
      DuckDbConfig.inMemory().build()).transactor()

    val cities: List[City] = tx.transact { conn =>
      sql"""CREATE TABLE city (
                name VARCHAR, country VARCHAR, population INTEGER)"""
        .update().run(conn)

      sql"""INSERT INTO city VALUES
                ('Oslo', 'Norway', 709037),
                ('Bergen', 'Norway', 291189),
                ('Stockholm', 'Sweden', 984748)"""
        .update().run(conn)

      findCities.run(conn)
    }

    // Verify that query types match the database schema
    tx.transact { conn =>
      val analysis: QueryAnalysis =
        QueryAnalyzer.analyze(findCities, conn).head
      assert(analysis.succeeded, analysis.report())
    }
  //stop
