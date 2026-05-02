package dev.typr.foundations.docs.sqlite;

import dev.typr.foundations.SqliteType;
import dev.typr.foundations.SqliteTypes;

@SuppressWarnings("unused")
public class RealTypes {
  // start
  SqliteType<Double> realType = SqliteTypes.real; // REAL (canonical, 8-byte IEEE 754)
  SqliteType<Double> doubleType = SqliteTypes.double_;
  SqliteType<Float> floatType = SqliteTypes.float_;
  // stop
}
