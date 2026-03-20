package dev.typr.foundations.docs.oracle;

import dev.typr.foundations.OracleType;
import dev.typr.foundations.OracleTypes;

@SuppressWarnings("unused")
public class RowIdTypes {
  // start
  OracleType<String> rowidType = OracleTypes.rowId;
  OracleType<String> urowidType = OracleTypes.uRowId;
  OracleType<String> urowid1000 = OracleTypes.uRowId(1000);
  // stop
}
