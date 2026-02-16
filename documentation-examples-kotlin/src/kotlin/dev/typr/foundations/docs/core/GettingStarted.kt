package dev.typr.foundations.docs.core

import dev.typr.foundationskt.*
import dev.typr.foundationskt.connect.*
import dev.typr.foundationskt.data.*

@Suppress("unused")
class GettingStarted {
    data class City(val name: String, val population: Int)

    val cityParser: RowParser<City> =
        RowParser.builder<City>()
            .field(DuckDbTypes.varchar, City::name)
            .field(DuckDbTypes.integer, City::population)
            .build(::City)

    //start
    fun example() {
        // Connect to an in-memory DuckDB database
        val tx =
            SimpleDataSource.create(DuckDbConfig.inMemory().build())
                .transactor()

        // Create the table and insert data
        tx.transact { conn ->
            Sql { "CREATE TABLE city (name VARCHAR, population INTEGER)" }
                .update().run(conn)
            Sql { "INSERT INTO city VALUES ('Oslo', 709037), ('Bergen', 291189)" }
                .update().run(conn)
        }

        // Query with type-safe parameters
        val cities: List<City> =
            Sql { """
                SELECT name, population
                FROM city
                ORDER BY population DESC
            """.trimIndent() }
                .query(cityParser.all())
                .transact(tx)

        cities.forEach { println("${it.name}: ${it.population}") }
    }
    //stop
}
