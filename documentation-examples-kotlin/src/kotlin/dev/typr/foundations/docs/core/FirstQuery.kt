package dev.typr.foundations.docs.core

import dev.typr.foundationskt.*
import dev.typr.foundationskt.connect.*

@Suppress("unused")
class FirstQuery {
    //start
    data class City(val name: String, val country: String, val population: Int)

    val cityCodec: RowCodecNamed<City> =
        RowCodec.namedBuilder<City>()
            .field("name", DuckDbTypes.varchar, City::name)
            .field("country", DuckDbTypes.varchar, City::country)
            .field("population", DuckDbTypes.integer, City::population)
            .build(::City)

    val findCities: Operation<List<City>> =
        sql { "SELECT ${cityCodec.columnList} FROM city ORDER BY population DESC" }
            .query(cityCodec.all())

    fun example() {
        val tx = SingleConnectionDataSource.create(
            DuckDbConfig.inMemory().build()).transactor()

        val cities: List<City> = tx.transact { conn ->
            sql { """
                CREATE TABLE city (
                    name VARCHAR, country VARCHAR, population INTEGER)
            """ }.update().run(conn)

            sql { """
                INSERT INTO city VALUES
                    ('Oslo', 'Norway', 709037),
                    ('Bergen', 'Norway', 291189),
                    ('Stockholm', 'Sweden', 984748)
            """ }.update().run(conn)

            findCities.run(conn)
        }

        // Verify that query types match the database schema
        tx.transact { conn ->
            val analysis: QueryAnalysis =
                QueryAnalyzer.analyze(findCities, conn).first()
            check(analysis.succeeded()) { analysis.report() }
        }
    }
    //stop
}
