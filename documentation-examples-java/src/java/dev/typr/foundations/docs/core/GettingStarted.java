package dev.typr.foundations.docs.core;

import dev.typr.foundations.DuckDbTypes;
import dev.typr.foundations.Fragment;
import dev.typr.foundations.RowCodec;
import dev.typr.foundations.Transactor;
import dev.typr.foundations.connect.SingleConnectionDataSource;
import dev.typr.foundations.connect.DuckDbConfig;

import java.sql.SQLException;
import java.util.List;

@SuppressWarnings("unused")
public class GettingStarted {
    record City(String name, int population) {}

    RowCodec<City> cityParser =
        RowCodec.<City>builder()
            .field(DuckDbTypes.varchar, City::name)
            .field(DuckDbTypes.integer, City::population)
            .build(City::new);

    void setup(Transactor tx) throws SQLException {
        tx.execute(conn -> {
            Fragment.of("CREATE TABLE city (name VARCHAR, population INTEGER)")
                .update().run(conn);
            Fragment.of("INSERT INTO city VALUES ('Oslo', 709037), ('Bergen', 291189)")
                .update().run(conn);
            return null;
        });
    }

    //start
    void example() throws SQLException {
        var tx =
            SingleConnectionDataSource.create(
                    DuckDbConfig.inMemory().build())
                .transactor();

        List<City> cities = tx.execute(conn ->
            Fragment.of("""
                    SELECT name, population
                    FROM city
                    ORDER BY population DESC""")
                .query(cityParser.all())
                .run(conn));
    }
    //stop
}
