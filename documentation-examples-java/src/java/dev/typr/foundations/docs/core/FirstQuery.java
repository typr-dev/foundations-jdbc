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

    static RowCodecNamed<City> cityParser =
        RowCodec.<City>namedBuilder()
            .field("name", DuckDbTypes.varchar, City::name)
            .field("country", DuckDbTypes.varchar, City::country)
            .field("population", DuckDbTypes.integer, City::population)
            .build(City::new);

    void example() throws SQLException {
        var ds = SingleConnectionDataSource.create(
            DuckDbConfig.inMemory().build());
        var tx = ds.transactor();

        tx.execute(conn -> {
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
            return null;
        });

        List<City> cities = tx.execute(conn ->
            Fragment.of("SELECT ")
                .append(cityParser.columnList())
                .append(" FROM city ORDER BY population DESC")
                .query(cityParser.all())
                .run(conn));

        // [City[name=Stockholm, ...], City[name=Oslo, ...], City[name=Bergen, ...]]

        // Verify that query types match the database schema
        tx.execute(conn -> {
            var query = Fragment.of("SELECT ")
                .append(cityParser.columnList())
                .append(" FROM city")
                .query(cityParser.all());
            QueryAnalysis analysis =
                QueryAnalyzer.analyze(query, conn).getFirst();
            assert analysis.succeeded() : analysis.report();
            return null;
        });
    }
    //stop
}
