package dev.typr.foundations.docs.core;

import dev.typr.foundations.PgTypes;
import dev.typr.foundations.RowParser;

import java.time.Instant;

@SuppressWarnings("unused")
public class RowParserBasic {
    //start
    record Person(Integer id, String name, Instant createdAt) {}

    RowParser<Person> personParser =
        RowParser.<Person>builder()
            .field(PgTypes.int4, Person::id)
            .field(PgTypes.text, Person::name)
            .field(PgTypes.timestamptz, Person::createdAt)
            .build(Person::new);
    //stop
}
