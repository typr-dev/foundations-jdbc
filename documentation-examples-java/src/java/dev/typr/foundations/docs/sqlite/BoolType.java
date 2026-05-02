package dev.typr.foundations.docs.sqlite;

import dev.typr.foundations.SqliteType;
import dev.typr.foundations.SqliteTypes;

@SuppressWarnings("unused")
public class BoolType {
  // start
  // SQLite has no BOOLEAN storage class — values land as INTEGER 0/1.
  SqliteType<Boolean> boolType = SqliteTypes.boolean_;
  // stop
}
