package dev.typr.foundations.docs.duckdb;

import dev.typr.foundations.DuckDbType;
import dev.typr.foundations.DuckDbTypes;

@SuppressWarnings("unused")
public class StringTypes {
  // start
  DuckDbType<String> varcharType = DuckDbTypes.varchar;
  DuckDbType<String> charType = DuckDbTypes.char_(10);
  // stop
}
