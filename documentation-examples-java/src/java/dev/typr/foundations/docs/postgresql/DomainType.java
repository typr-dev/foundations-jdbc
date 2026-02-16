package dev.typr.foundations.docs.postgresql;

import dev.typr.foundations.PgType;
import dev.typr.foundations.PgTypes;

@SuppressWarnings("unused")
public class DomainType {
    //start
    // Wrapper type
    public record Email(String value) {}

    // Create PgType from text
    PgType<Email> emailType = PgTypes.text.transform(Email::new, Email::value);
    //stop
}
