package dev.typr.foundations.docs.duckdb;

import dev.typr.foundations.DuckDbType;
import dev.typr.foundations.DuckDbTypes;
import java.util.List;

@SuppressWarnings("unused")
public class NestedCollections {
  // start
  // Nested collections compose — any combination is legal.
  DuckDbType<List<List<Integer>>> grid = DuckDbTypes.integer.list().list();
  DuckDbType<List<List<Float>>> matrix = DuckDbTypes.float_.array(3).array(3);
  DuckDbType<List<List<String>>> variableRows = DuckDbTypes.varchar.list().array(4);
  // stop
}
