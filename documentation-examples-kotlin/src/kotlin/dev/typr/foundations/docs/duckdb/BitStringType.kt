package dev.typr.foundations.docs.duckdb

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*

@Suppress("unused")
class BitStringType {
    //start
    val bitType: DuckDbType<String> = DuckDbTypes.bit
    val bit8: DuckDbType<String> = DuckDbTypes.bit(8)  // BIT(8)
    //stop
}
