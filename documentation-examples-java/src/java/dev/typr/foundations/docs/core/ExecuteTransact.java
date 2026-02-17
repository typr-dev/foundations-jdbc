package dev.typr.foundations.docs.core;

import dev.typr.foundations.Fragment;
import dev.typr.foundations.Operation;
import dev.typr.foundations.PgTypes;
import dev.typr.foundations.RowParser;
import dev.typr.foundations.Transactor;

import java.sql.SQLException;
import java.util.List;

@SuppressWarnings("unused")
public class ExecuteTransact {
    record City(String name, int population) {}

    static RowParser<City> cityParser =
        RowParser.<City>builder()
            .field(PgTypes.text, City::name)
            .field(PgTypes.int4, City::population)
            .build(City::new);

    Transactor tx = null; // placeholder

    Operation<List<City>> findCities =
        Fragment.of("""
                SELECT name, population FROM city
                ORDER BY population DESC""")
            .query(cityParser.all());

    //start
    List<City> cities() throws SQLException {
        return tx.execute(conn -> findCities.run(conn));
    }
    //stop
}
