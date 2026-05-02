package dev.typr.foundations.docs.sqlite;

import dev.typr.foundations.SqliteType;
import dev.typr.foundations.SqliteTypes;
import java.util.Optional;

@SuppressWarnings("unused")
public class NullableTypes {
  // start
  SqliteType<Long> notNull = SqliteTypes.integer;
  SqliteType<Optional<Long>> nullable = SqliteTypes.integer.opt();
  // stop
}
