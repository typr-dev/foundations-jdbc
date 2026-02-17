package dev.typr.foundations.docs.core;

import dev.typr.foundations.PgTypes;
import dev.typr.foundations.RowParser;

import java.time.Instant;
import java.util.Optional;

@SuppressWarnings("unused")
public class NullableColumns {
    //start
    record Person(Integer id, String name, Optional<Instant> createdAt) {}

    RowParser<Person> personParser =
        RowParser.<Person>builder()
            .field(PgTypes.int4, Person::id)
            .field(PgTypes.text, Person::name)
            .field(PgTypes.timestamptz.opt(), Person::createdAt)
            .build(Person::new);
    //stop
}
