package dev.typr.foundations.docs.duckdb

import dev.typr.foundations.DuckDbType
import dev.typr.foundations.DuckDbTypes
import dev.typr.foundations.data.Uint1
import dev.typr.foundations.data.Uint4
import java.math.BigInteger

@Suppress("unused")
class IntegerTypesUnsigned {
    //start
    val utinyType: DuckDbType<Uint1> = DuckDbTypes.utinyint
    val uintType: DuckDbType<Uint4> = DuckDbTypes.uinteger
    val uhugeType: DuckDbType<BigInteger> = DuckDbTypes.uhugeint
    //stop
}
