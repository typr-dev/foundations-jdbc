package dev.typr.foundations.docs.core

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*

@Suppress("unused")
class ExecuteTransact {
    data class City(val name: String, val population: Int)

    val cityCodec: RowCodec<City> =
        RowCodec.builder<City>()
            .field(PgTypes.text, City::name)
            .field(PgTypes.int4, City::population)
            .build(::City)

    lateinit var tx: Transactor

    val findCities: Operation<List<City>> =
        sql { "SELECT name, population FROM city ORDER BY population DESC" }
            .query(cityCodec.all())

    //start
    fun cities(): List<City> = tx.transact { conn ->
        findCities.run(conn)
    }
    //stop
}
