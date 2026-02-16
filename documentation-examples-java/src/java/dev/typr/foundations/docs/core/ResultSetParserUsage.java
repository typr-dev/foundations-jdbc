package dev.typr.foundations.docs.core;

import dev.typr.foundations.PgTypes;
import dev.typr.foundations.ResultSetParser;
import dev.typr.foundations.RowParser;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
public class ResultSetParserUsage {
    record Person(Integer id, String name, Instant createdAt) {}

    RowParser<Person> personParser =
        RowParser.<Person>builder()
            .field(PgTypes.int4, Person::id)
            .field(PgTypes.text, Person::name)
            .field(PgTypes.timestamptz, Person::createdAt)
            .build(Person::new);

    ResultSet resultSet = null; // placeholder

    //start
    // Parse a single optional result
    ResultSetParser<Optional<Person>> singleParser =
        personParser.maxOne();

    // Parse all results as a list
    ResultSetParser<List<Person>> listParser =
        personParser.all();

    // Execute with ResultSet
    Optional<Person> parse() throws SQLException {
        return singleParser.apply(resultSet);
    }
    //stop
}
