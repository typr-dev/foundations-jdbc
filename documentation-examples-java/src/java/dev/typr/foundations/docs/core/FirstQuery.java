package dev.typr.foundations.docs.core;

import dev.typr.foundations.*;
import dev.typr.foundations.connect.SingleConnectionDataSource;
import dev.typr.foundations.connect.DuckDbConfig;

import java.sql.SQLException;
import java.util.List;

@SuppressWarnings("unused")
public class FirstQuery {
    //start
    record City(String name, String country, int population) {}

    static RowCodecNamed<City> cityCodec =
        RowCodec.<City>namedBuilder()
            .field("name", DuckDbTypes.varchar, City::name)
            .field("country", DuckDbTypes.varchar, City::country)
            .field("population", DuckDbTypes.integer, City::population)
            .build(City::new);

    static Operation<List<City>> findCities =
        Fragment.of("SELECT ")
            .append(cityCodec.columnList())
            .append(" FROM city ORDER BY population DESC")
            .query(cityCodec.all());

    void example() throws SQLException {
        var tx = SingleConnectionDataSource.create(
            DuckDbConfig.inMemory().build()).transactor();

        List<City> cities = tx.execute(conn -> {
            Fragment.of("""
                    CREATE TABLE city (
                        name VARCHAR, country VARCHAR, population INTEGER)""")
                .update().run(conn);

            Fragment.of("""
                    INSERT INTO city VALUES
                        ('Oslo', 'Norway', 709037),
                        ('Bergen', 'Norway', 291189),
                        ('Stockholm', 'Sweden', 984748)""")
                .update().run(conn);

            return findCities.run(conn);
        });

        // Verify that query types match the database schema
        tx.execute(conn -> {
            QueryAnalysis analysis =
                QueryAnalyzer.analyze(findCities, conn).getFirst();
            assert analysis.succeeded() : analysis.report();
            return null;
        });
    }
    //stop
}
