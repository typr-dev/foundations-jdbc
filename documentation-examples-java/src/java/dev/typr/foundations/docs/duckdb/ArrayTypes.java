package dev.typr.foundations.docs.duckdb;

import dev.typr.foundations.DuckDbType;
import dev.typr.foundations.DuckDbTypes;
import java.util.UUID;

@SuppressWarnings("unused")
public class ArrayTypes {
  // start
  DuckDbType<Integer[]> intArray = DuckDbTypes.integerArray;
  DuckDbType<String[]> strArray = DuckDbTypes.varcharArray;
  DuckDbType<UUID[]> uuidArray = DuckDbTypes.uuidArray;

  // Create array for any type
  // DuckDbType<MyType[]> customArray = myType.array();
  // stop
}
