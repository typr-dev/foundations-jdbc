package dev.typr.foundations.docs.oracle;

import dev.typr.foundations.OracleType;
import dev.typr.foundations.OracleTypes;
import dev.typr.foundations.data.PaddedString;

@SuppressWarnings("unused")
public class PaddedStringTypes {
  // start
  OracleType<PaddedString> padded = OracleTypes.charPadded(10); // CHAR(10)
  OracleType<PaddedString> npadded = OracleTypes.ncharPadded(10); // NCHAR(10)
  // stop
}
