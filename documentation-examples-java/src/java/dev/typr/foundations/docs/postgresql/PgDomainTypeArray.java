package dev.typr.foundations.docs.postgresql;

import dev.typr.foundations.PgType;
import dev.typr.foundations.PgTypes;
import java.util.List;

@SuppressWarnings("unused")
public class PgDomainTypeArray {
  // start
  // Wrap once at the scalar level — the array codec carries the wrapper through
  // .array(), so no list-level bijection is needed.
  public record Name(String value) {
    public static final PgType<Name> pgType =
        PgTypes.text.asDomain("person_name", Name::new, Name::value);

    public static final PgType<List<Name>> pgArrayType = pgType.array();
  }
  // stop
}
