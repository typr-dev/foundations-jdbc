package dev.typr.foundations.docs.oracle;

import dev.typr.foundations.OracleType;
import dev.typr.foundations.OracleTypes;

@SuppressWarnings("unused")
public class BoolType {
  // start
  OracleType<Boolean> boolNative = OracleTypes.boolean_; // Oracle 23c+
  OracleType<Boolean> boolNumber = OracleTypes.numberAsBoolean; // NUMBER(1)
  // stop
}
