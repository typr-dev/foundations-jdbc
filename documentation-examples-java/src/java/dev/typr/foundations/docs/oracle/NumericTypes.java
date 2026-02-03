package dev.typr.foundations.docs.oracle;

import dev.typr.foundations.OracleType;
import dev.typr.foundations.OracleTypes;

import java.math.BigDecimal;

@SuppressWarnings("unused")
public class NumericTypes {
    //start
    OracleType<BigDecimal> numberType = OracleTypes.number;
    OracleType<BigDecimal> decimal = OracleTypes.number(10, 2);  // NUMBER(10,2)
    OracleType<Integer> intType = OracleTypes.numberAsInt(9);    // NUMBER(9)
    OracleType<Long> longType = OracleTypes.numberAsLong(18);    // NUMBER(18)
    //stop
}
