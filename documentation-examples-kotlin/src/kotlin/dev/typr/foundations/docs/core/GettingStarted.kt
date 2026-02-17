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

    fun setup(tx: Transactor) {
        tx.transact { conn ->
            Sql { "CREATE TABLE city (name VARCHAR, population INTEGER)" }
                .update().run(conn)
            Sql { "INSERT INTO city VALUES ('Oslo', 709037), ('Bergen', 291189)" }
                .update().run(conn)
        }
    }

    //start
    fun example() {
        val tx =
            SingleConnectionDataSource.create(DuckDbConfig.inMemory().build())
                .transactor()

        val cities: List<City> = tx.transact { conn ->
            Sql { """
                SELECT name, population
                FROM city
                ORDER BY population DESC
            """ }
                .query(cityParser.all())
                .run(conn)
        }
    }
    //stop
}
