package dev.typr.foundations.docs.duckdb

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*
import java.math.BigInteger

@Suppress("unused")
class IntegerTypesSigned {
    //start
    val tinyType: DuckDbType<Byte> = DuckDbTypes.tinyint
    val intType: DuckDbType<Int> = DuckDbTypes.integer
    val hugeType: DuckDbType<BigInteger> = DuckDbTypes.hugeint
    //stop
}
