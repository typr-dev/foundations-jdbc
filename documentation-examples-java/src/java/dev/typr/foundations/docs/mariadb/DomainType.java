package dev.typr.foundations.docs.mariadb;

import dev.typr.foundations.MariaType;
import dev.typr.foundations.MariaTypes;

@SuppressWarnings("unused")
public class DomainType {
  // start
  // Wrapper type
  public record UserId(Long value) {}

  // Create MariaType from bigint
  MariaType<UserId> userIdType = MariaTypes.bigint.transform(UserId::new, UserId::value);
  // stop
}
