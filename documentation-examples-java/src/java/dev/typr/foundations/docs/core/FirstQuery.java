package dev.typr.foundations.docs.core;

import dev.typr.foundations.*;

import java.util.List;

@SuppressWarnings("unused")
public class FirstQuery {
    //start
    record City(String name, int population) {}

    static final RowCodecNamed<City> cityCodec =
        RowCodec.<City>namedBuilder()
            .field("name", DuckDbTypes.varchar, City::name)
            .field("population", DuckDbTypes.integer, City::population)
            .build(City::new);

    static final Operation<List<City>> findCities =
        Fragment.of("SELECT ")
            .append(cityCodec.columnList())
            .append(" FROM city ORDER BY population DESC")
            .query(cityCodec.all());
    //stop
}
