package dev.typr.foundations.docs.core

import dev.typr.kotlinfoundations.*
import dev.typr.kotlinfoundations.connect.*
import dev.typr.kotlinfoundations.data.*

@Suppress("unused")
class GettingStarted {
    data class City(val name: String, val population: Int)

    val cityParser: RowParser<City> = RowParser.builder<City>()
        .field(DuckDbTypes.varchar, City::name)
        .field(DuckDbTypes.integer, City::population)
        .build(::City)

    //start
    fun example() {
        // Connect to an in-memory DuckDB database
        val tx = SimpleDataSource.create(DuckDbConfig.inMemory().build()).transactor()

        // Create the table and insert data
        tx.transact { conn ->
            Fragment.of("CREATE TABLE city (name VARCHAR, population INTEGER)").update().runChecked(conn)
            Fragment.of("INSERT INTO city VALUES ('Oslo', 709037), ('Bergen', 291189)").update().runChecked(conn)
        }

        // Query with type-safe parameters
        val cities: List<City> = Fragment.of("SELECT name, population FROM city ORDER BY population DESC")
            .query(cityParser.all())
            .transact(tx)

        cities.forEach { println("${it.name}: ${it.population}") }
    }
    //stop
}
