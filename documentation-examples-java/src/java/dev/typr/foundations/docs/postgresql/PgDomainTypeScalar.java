package dev.typr.foundations.docs.postgresql;

import dev.typr.foundations.PgType;
import dev.typr.foundations.PgTypes;

@SuppressWarnings("unused")
public class PgDomainTypeScalar {
  // start
  // PG schema:  CREATE DOMAIN person_name AS varchar(100);
  public record Name(String value) {
    public static final PgType<Name> pgType =
        PgTypes.text.asDomain("person_name", Name::new, Name::value);
  }
  // stop
}
