package dev.typr.foundations.docs.duckdb;

import dev.typr.foundations.DuckDbType;
import dev.typr.foundations.DuckDbTypes;
import java.util.Optional;

@SuppressWarnings("unused")
public class NullableTypes {
  // start
  DuckDbType<Integer> notNull = DuckDbTypes.integer;
  DuckDbType<Optional<Integer>> nullable = DuckDbTypes.integer.opt();
  // stop
}
