package dev.typr.foundations.docs.oracle;

import dev.typr.foundations.OracleType;
import dev.typr.foundations.OracleTypes;
import dev.typr.foundations.data.NonEmptyBlob;

@SuppressWarnings("unused")
public class BinaryTypes {
  // start
  OracleType<byte[]> rawType = OracleTypes.raw;
  OracleType<byte[]> raw100 = OracleTypes.raw(100); // RAW(100)

  // Non-empty variant
  OracleType<NonEmptyBlob> rawNonEmpty = OracleTypes.rawNonEmpty(100);
  // stop
}
