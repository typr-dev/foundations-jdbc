package dev.typr.foundationskt.docs.core

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

    val countCities: Operation<Long> =
        sql { "SELECT count(*) FROM city" }.queryExactlyOne(PgTypes.int8)

    //start
    // Single-operation form: .transact(tx) handles commit/rollback/close.
    fun cities(): List<City> = findCities.transact(tx)

    // Multiple operations in one transaction: pass a block that takes a Connection.
    // Each .run(conn) inside shares the same Connection and therefore the same transaction.
    fun citiesWithCount(): List<City> = tx.transact { conn ->
        val list = findCities.run(conn)
        val count = countCities.run(conn)
        println("rows: $count")
        list
    }
    //stop
}
