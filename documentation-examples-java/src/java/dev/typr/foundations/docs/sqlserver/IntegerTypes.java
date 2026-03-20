package dev.typr.foundations.docs.sqlserver;

import dev.typr.foundations.SqlServerType;
import dev.typr.foundations.SqlServerTypes;
import dev.typr.foundations.data.Uint1;

@SuppressWarnings("unused")
public class IntegerTypes {
  // start
  SqlServerType<Uint1> tinyType = SqlServerTypes.tinyint; // Note: unsigned!
  SqlServerType<Integer> intType = SqlServerTypes.int_;
  SqlServerType<Long> bigType = SqlServerTypes.bigint;
  // stop
}
