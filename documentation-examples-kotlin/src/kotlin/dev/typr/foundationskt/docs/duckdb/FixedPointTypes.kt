package dev.typr.foundationskt.docs.duckdb

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*
import java.math.BigDecimal

@Suppress("unused")
class FixedPointTypes {
    //start
    val decimalType: DuckDbType<BigDecimal> = DuckDbTypes.decimal
    val precise: DuckDbType<BigDecimal> = DuckDbTypes.decimalOf(18, 6)  // DECIMAL(18,6)
    //stop
}
