package dev.typr.foundations.docs.duckdb;

import dev.typr.foundations.DuckDbType;
import dev.typr.foundations.DuckDbTypes;
import java.util.List;

@SuppressWarnings("unused")
public class ArrayTypes {
  // start
  // Fixed-size ARRAY — every row must have exactly `size` elements.
  DuckDbType<List<Float>> embedding = DuckDbTypes.float_.array(1536);
  DuckDbType<List<Integer>> rgb = DuckDbTypes.integer.array(3);
  // stop
}
