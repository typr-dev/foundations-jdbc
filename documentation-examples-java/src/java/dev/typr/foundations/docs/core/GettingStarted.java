package dev.typr.foundations.docs.core;

import dev.typr.foundations.DuckDbTypes;
import dev.typr.foundations.Fragment;
import dev.typr.foundations.RowParser;
import dev.typr.foundations.Transactor;
import dev.typr.foundations.connect.SimpleDataSource;
import dev.typr.foundations.connect.DuckDbConfig;

import java.sql.SQLException;
import java.util.List;

@SuppressWarnings("unused")
public class GettingStarted {
    record City(String name, int population) {}

    //start
    void example() throws SQLException {
        // Connect to an in-memory DuckDB database
        var ds =
            SimpleDataSource.create(
                DuckDbConfig.inMemory().build());
        var tx = ds.transactor();

        // Define a row parser
        RowParser<City> cityParser =
            RowParser.<City>builder()
                .field(DuckDbTypes.varchar, City::name)
                .field(DuckDbTypes.integer, City::population)
                .build(City::new);

        // Create the table and insert data
        tx.execute(conn -> {
            Fragment.of("""
                    CREATE TABLE city
                    (name VARCHAR, population INTEGER)""")
                .update().run(conn);
            Fragment.of("""
                    INSERT INTO city VALUES
                    ('Oslo', 709037), ('Bergen', 291189)""")
                .update().run(conn);
            return null;
        });

        // Query with type-safe parameters
        List<City> cities =
            Fragment.of("""
                    SELECT name, population
                    FROM city
                    ORDER BY population DESC""")
                .query(cityParser.all())
                .transact(tx);

        cities.forEach(c -> System.out.println(c.name() + ": " + c.population()));
    }
    //stop
}
