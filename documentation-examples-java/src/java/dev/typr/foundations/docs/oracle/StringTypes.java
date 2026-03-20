package dev.typr.foundations.docs.oracle;

import dev.typr.foundations.OracleType;
import dev.typr.foundations.OracleTypes;

@SuppressWarnings("unused")
public class StringTypes {
  // start
  OracleType<String> varcharType = OracleTypes.varchar2;
  OracleType<String> varchar100 = OracleTypes.varchar2(100);
  OracleType<String> charType = OracleTypes.char_(10);
  OracleType<String> nvarcharType = OracleTypes.nvarchar2(100);
  // stop
}
