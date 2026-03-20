package dev.typr.foundations.docs.mariadb;

import dev.typr.foundations.MariaType;
import dev.typr.foundations.MariaTypes;
import java.util.Optional;

@SuppressWarnings("unused")
public class NullableType {
  // start
  MariaType<Integer> notNull = MariaTypes.int_;
  MariaType<Optional<Integer>> nullable = MariaTypes.int_.opt();
  // stop
}
