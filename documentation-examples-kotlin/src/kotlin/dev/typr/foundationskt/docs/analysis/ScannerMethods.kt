package dev.typr.foundationskt.docs.analysis

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*

@Suppress("unused")
class ScannerMethods {
    data class City(val id: Int, val name: String)

    val cityCodec: RowCodec<City> = RowCodec.builder<City>()
        .field(PgTypes.int4, City::id)
        .field(PgTypes.text, City::name)
        .build(::City)

    //start
    // Properties — discovered automatically
    val allCities: Operation<List<City>> =
        sql { "SELECT id, name FROM cities" }
            .query(cityCodec.all())

    // No-arg methods — discovered automatically
    fun activeCities(): Operation<List<City>> =
        sql { "SELECT id, name FROM cities WHERE active" }
            .query(cityCodec.all())

    // Methods with parameters — dummy arguments constructed automatically
    fun findByName(name: String): Operation<City?> =
        sql { "SELECT id, name FROM cities WHERE name = ${PgTypes.text(name)}" }
            .query(cityCodec.maxOne())

    // Templates — also discovered
    val findById: Template<Int, City?> =
        Fragment.of("SELECT id, name FROM cities WHERE id = ")
            .param(PgTypes.int4)
            .query(cityCodec.maxOne())
    //stop
}
