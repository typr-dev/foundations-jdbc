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
    val allCities: OperationRead<List<City>> =
        Fragment.of("SELECT id, name FROM cities")
            .query(cityCodec.all())

    // No-arg methods — discovered automatically
    fun activeCities(): OperationRead<List<City>> =
        Fragment.of("SELECT id, name FROM cities WHERE active")
            .query(cityCodec.all())

    // Methods with parameters — dummy arguments constructed automatically
    fun findByName(name: String): OperationRead<City?> =
        Fragment.of("SELECT id, name FROM cities WHERE name = ")
            .value(PgTypes.text, name)
            .query(cityCodec.maxOne())

    // Methods with primitive arguments are also handled
    fun findById(id: Int): OperationRead<City?> =
        Fragment.of("SELECT id, name FROM cities WHERE id = ")
            .value(PgTypes.int4, id)
            .query(cityCodec.maxOne())
    //stop
}
