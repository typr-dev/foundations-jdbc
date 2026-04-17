package dev.typr.foundations.docs.duckdb;

import dev.typr.foundations.DuckDbType;
import dev.typr.foundations.DuckDbTypes;

@SuppressWarnings("unused")
public class EnumType {
  // start
  // Define your Java enum
  public enum Status {
    PENDING,
    ACTIVE,
    COMPLETED
  }

  // Create DuckDbType — pass values(), no reflection
  DuckDbType<Status> statusType = DuckDbTypes.ofEnum("status", Status.values());
  // stop
}
