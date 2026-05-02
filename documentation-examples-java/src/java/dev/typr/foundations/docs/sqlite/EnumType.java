package dev.typr.foundations.docs.sqlite;

import dev.typr.foundations.SqliteType;
import dev.typr.foundations.SqliteTypes;

@SuppressWarnings("unused")
public class EnumType {
  // start
  // Define your Java enum
  public enum Status {
    PENDING,
    ACTIVE,
    COMPLETED
  }

  // SQLite has no native enum — pair with `CHECK (col IN ('PENDING','ACTIVE','COMPLETED'))` in DDL.
  SqliteType<Status> statusType = SqliteTypes.ofEnum(Status.values());
  // stop
}
