package dev.typr.foundationskt.docs.core

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*

@Suppress("unused")
object FirstQuery {
    //start
    data class City(val name: String, val population: Int)

    val cityCodec: RowCodecNamed<City> =
        RowCodec.namedBuilder<City>()
            .field("name", DuckDbTypes.varchar, City::name)
            .field("population", DuckDbTypes.integer, City::population)
            .build(::City)

    val findCities: Operation<List<City>> =
        sql { "SELECT ${cityCodec.columnList} FROM city ORDER BY population DESC" }
            .query(cityCodec.all())
    //stop
}
