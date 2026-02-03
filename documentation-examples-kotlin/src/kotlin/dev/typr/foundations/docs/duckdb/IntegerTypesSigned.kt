package dev.typr.foundations.docs.duckdb

import dev.typr.foundations.DuckDbType
import dev.typr.foundations.DuckDbTypes
import java.math.BigInteger

@Suppress("unused")
class IntegerTypesSigned {
    //start
    val tinyType: DuckDbType<Byte> = DuckDbTypes.tinyint
    val intType: DuckDbType<Int> = DuckDbTypes.integer
    val hugeType: DuckDbType<BigInteger> = DuckDbTypes.hugeint
    //stop
}
