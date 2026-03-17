package dev.typr.foundations.docs.duckdb;

import dev.typr.foundations.DuckDbType;
import dev.typr.foundations.DuckDbTypes;
import java.math.BigInteger;

@SuppressWarnings("unused")
public class IntegerTypesSigned {
  // start
  DuckDbType<Byte> tinyType = DuckDbTypes.tinyint;
  DuckDbType<Integer> intType = DuckDbTypes.integer;
  DuckDbType<BigInteger> hugeType = DuckDbTypes.hugeint;
  // stop
}
