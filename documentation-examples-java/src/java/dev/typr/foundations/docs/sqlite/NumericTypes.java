package dev.typr.foundations.docs.sqlite;

import dev.typr.foundations.SqliteType;
import dev.typr.foundations.SqliteTypes;
import java.math.BigDecimal;

@SuppressWarnings("unused")
public class NumericTypes {
  // start
  // SQLite stores BigDecimal as TEXT to preserve precision — declared (p,s) is a label only.
  SqliteType<BigDecimal> numericType = SqliteTypes.numeric;
  SqliteType<BigDecimal> decimalType = SqliteTypes.decimal;
  SqliteType<BigDecimal> precise = SqliteTypes.decimalOf(18, 6);
  // stop
}
