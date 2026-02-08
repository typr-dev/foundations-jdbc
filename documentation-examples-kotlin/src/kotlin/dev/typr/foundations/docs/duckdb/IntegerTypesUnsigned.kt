package dev.typr.foundations.docs.duckdb

import dev.typr.kotlinfoundations.*
import dev.typr.kotlinfoundations.data.*
import java.math.BigInteger

@Suppress("unused")
class IntegerTypesUnsigned {
    //start
    val utinyType: DuckDbType<Uint1> = DuckDbTypes.utinyint
    val uintType: DuckDbType<Uint4> = DuckDbTypes.uinteger
    val uhugeType: DuckDbType<BigInteger> = DuckDbTypes.uhugeint
    //stop
}
