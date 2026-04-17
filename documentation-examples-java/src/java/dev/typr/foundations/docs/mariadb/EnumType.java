package dev.typr.foundations.docs.mariadb;

import dev.typr.foundations.MariaType;
import dev.typr.foundations.MariaTypes;

@SuppressWarnings("unused")
public class EnumType {
  // start
  // Define your Java enum
  public enum Status {
    PENDING,
    ACTIVE,
    COMPLETED
  }

  // Create MariaType — derives ENUM('PENDING','ACTIVE','COMPLETED') from values()
  MariaType<Status> statusType = MariaTypes.ofEnum(Status.values());
  // stop
}
