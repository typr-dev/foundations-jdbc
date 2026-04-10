package dev.typr.foundations.docs.duckdb;

import dev.typr.foundations.DuckDbType;
import dev.typr.foundations.DuckDbTypes;
import java.util.UUID;

@SuppressWarnings("unused")
public class ArrayTypes {
  // start
  // Any type can be made into an array with .array()
  DuckDbType<Integer[]> intArray = DuckDbTypes.integer.array();
  DuckDbType<String[]> strArray = DuckDbTypes.varchar.array();
  DuckDbType<UUID[]> uuidArray = DuckDbTypes.uuid.array();
  // stop
}
