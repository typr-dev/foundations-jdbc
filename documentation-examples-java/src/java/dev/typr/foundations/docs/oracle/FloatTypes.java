package dev.typr.foundations.docs.oracle;

import dev.typr.foundations.OracleType;
import dev.typr.foundations.OracleTypes;

@SuppressWarnings("unused")
public class FloatTypes {
  // start
  OracleType<Float> binaryFloat = OracleTypes.binaryFloat;
  OracleType<Double> binaryDouble = OracleTypes.binaryDouble;
  OracleType<Double> floatType = OracleTypes.float_Of(126); // FLOAT(126)
  // stop
}
