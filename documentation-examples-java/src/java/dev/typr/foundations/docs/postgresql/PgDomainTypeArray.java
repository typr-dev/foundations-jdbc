package dev.typr.foundations.docs.postgresql;

import dev.typr.foundations.Bijection;
import dev.typr.foundations.PgType;
import dev.typr.foundations.PgTypes;
import java.util.List;

@SuppressWarnings("unused")
public class PgDomainTypeArray {
  // start
  // Arrays of domains "just work" — `.array()` composes after `.asDomain(...)`.
  // Use `.to(Bijection)` if you want the outer container as a different wrapper type.
  public record Name(String value) {
    public static final PgType<Name> pgType =
        PgTypes.text.transform(Name::new, Name::value).asDomain("person_name");
  }

  public record MiddleName(Name value) {}

  public static final PgType<List<MiddleName>> middleNames =
      Name.pgType
          .array()
          .to(
              Bijection.of(
                  ns -> ns.stream().map(MiddleName::new).toList(),
                  ms -> ms.stream().map(MiddleName::value).toList()));
  // stop
}
