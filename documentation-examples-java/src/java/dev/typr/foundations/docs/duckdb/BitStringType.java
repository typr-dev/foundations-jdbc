package dev.typr.foundations.docs.duckdb;

import dev.typr.foundations.DuckDbType;
import dev.typr.foundations.DuckDbTypes;

@SuppressWarnings("unused")
public class BitStringType {
  // start
  DuckDbType<String> bitType = DuckDbTypes.bit;
  DuckDbType<String> bit8 = DuckDbTypes.bit(8); // BIT(8)
  // stop
}
