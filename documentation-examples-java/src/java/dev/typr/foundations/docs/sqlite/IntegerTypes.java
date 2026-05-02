package dev.typr.foundations.docs.sqlite;

import dev.typr.foundations.SqliteType;
import dev.typr.foundations.SqliteTypes;

@SuppressWarnings("unused")
public class IntegerTypes {
  // start
  SqliteType<Long> integerType = SqliteTypes.integer; // INTEGER (canonical, INTEGER affinity)
  SqliteType<Long> bigintType = SqliteTypes.bigint; // BIGINT alias
  SqliteType<Integer> intType = SqliteTypes.int_; // INT (32-bit Java int)
  SqliteType<Short> smallintType = SqliteTypes.smallint;
  SqliteType<Byte> tinyintType = SqliteTypes.tinyint;
  // stop
}
