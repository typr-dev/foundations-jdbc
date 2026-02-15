package dev.typr.foundations.docs.core

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*

@Suppress("unused")
class ExecuteTransact {
    data class City(val name: String, val population: Int)

    val cityParser: RowParser<City> = RowParser.builder<City>()
        .field(PgTypes.text, City::name)
        .field(PgTypes.int4, City::population)
        .build(::City)

    lateinit var tx: Transactor

    val findCities: Operation<List<City>> =
        Fragment.of("SELECT name, population FROM city ORDER BY population DESC")
            .query(cityParser.all())

    //start
    fun cities(): List<City> = findCities.transact(tx)
    //stop
}
