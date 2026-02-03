package dev.typr.foundations.docs.oracle;

import dev.typr.foundations.OracleType;
import dev.typr.foundations.OracleTypes;

import java.util.Optional;

@SuppressWarnings("unused")
public class NullableType {
    //start
    OracleType<Integer> notNull = OracleTypes.numberInt;
    OracleType<Optional<Integer>> nullable = OracleTypes.numberInt.opt();
    //stop
}
