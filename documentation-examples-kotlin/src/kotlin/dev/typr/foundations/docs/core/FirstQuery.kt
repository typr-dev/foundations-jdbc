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

    fun example() {
        val ds = SingleConnectionDataSource.create(
            DuckDbConfig.inMemory().build())
        val tx = ds.transactor()

        tx.transact { conn ->
            Sql { """
                CREATE TABLE city (
                    name VARCHAR, country VARCHAR, population INTEGER)
            """ }.update().run(conn)
            Sql { """
                INSERT INTO city VALUES
                    ('Oslo', 'Norway', 709037),
                    ('Bergen', 'Norway', 291189),
                    ('Stockholm', 'Sweden', 984748)
            """ }.update().run(conn)
        }

        val cities: List<City> = tx.transact { conn ->
            Sql { "SELECT ${cityCodec.columnList} FROM city ORDER BY population DESC" }
                .query(cityCodec.all())
                .run(conn)
        }

        // [City(name=Stockholm, ...), City(name=Oslo, ...), City(name=Bergen, ...)]

        // Verify that query types match the database schema
        tx.transact { conn ->
            val query = Sql { "SELECT ${cityCodec.columnList} FROM city" }
                .query(cityCodec.all())
            val analysis: QueryAnalysis =
                QueryAnalyzer.analyze(query, conn).first()
            check(analysis.succeeded()) { analysis.report() }
        }
    }
    //stop
}
