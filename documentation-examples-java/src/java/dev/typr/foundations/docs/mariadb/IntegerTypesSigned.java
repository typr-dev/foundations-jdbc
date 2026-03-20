package dev.typr.foundations.docs.mariadb;

import dev.typr.foundations.MariaType;
import dev.typr.foundations.MariaTypes;

@SuppressWarnings("unused")
public class IntegerTypesSigned {
  // start
  MariaType<Byte> tinyType = MariaTypes.tinyint;
  MariaType<Integer> intType = MariaTypes.int_;
  MariaType<Long> bigType = MariaTypes.bigint;
  // stop
}
