package dev.typr.foundations.docs.mariadb;

import dev.typr.foundations.MariaType;
import dev.typr.foundations.MariaTypes;
import dev.typr.foundations.data.maria.MariaSet;

@SuppressWarnings("unused")
public class SetType {
  // start
  MariaType<MariaSet> setType = MariaTypes.set;

  // Create and use sets
  MariaSet values = MariaSet.of("read", "write");
  String csv = values.toCommaSeparated();
  // stop
}
