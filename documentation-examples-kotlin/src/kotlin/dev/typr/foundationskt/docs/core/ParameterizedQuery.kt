package dev.typr.foundationskt.docs.core

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*

@Suppress("unused")
object ParameterizedQuery {
    data class City(val name: String, val population: Int)

    val cityCodec: RowCodecNamed<City> =
        RowCodec.namedBuilder<City>()
            .field("name", DuckDbTypes.varchar, City::name)
            .field("population", DuckDbTypes.integer, City::population)
            .build(::City)

    //start
    fun findCityByName(name: String): Operation<City?> =
        sql { "SELECT ${cityCodec.columnList} FROM city WHERE name = ${DuckDbTypes.varchar(name)}" }
            .query(cityCodec.maxOne())
    //stop
}
