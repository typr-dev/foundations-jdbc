package dev.typr.foundations.docs.duckdb;

import dev.typr.foundations.DuckDbType;
import dev.typr.foundations.DuckDbTypes;
import dev.typr.foundations.data.Uint1;
import dev.typr.foundations.data.Uint4;

import java.math.BigInteger;

@SuppressWarnings("unused")
public class IntegerTypesUnsigned {
    //start
    DuckDbType<Uint1> utinyType = DuckDbTypes.utinyint;
    DuckDbType<Uint4> uintType = DuckDbTypes.uinteger;
    DuckDbType<BigInteger> uhugeType = DuckDbTypes.uhugeint;
    //stop
}
