package dev.typr.foundations.docs.postgresql;

import dev.typr.foundations.PgType;
import dev.typr.foundations.PgTypes;

@SuppressWarnings("unused")
public class EnumType {
  // start
  // Define your Java enum
  public enum Status {
    PENDING,
    ACTIVE,
    COMPLETED
  }

  // Create a PgType — pass values(), no reflection
  PgType<Status> statusType = PgTypes.ofEnum("status", Status.values());
  // stop
}
