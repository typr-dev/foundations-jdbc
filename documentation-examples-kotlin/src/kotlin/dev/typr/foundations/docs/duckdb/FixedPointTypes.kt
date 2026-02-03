package dev.typr.foundations.docs.duckdb

import dev.typr.foundations.DuckDbType
import dev.typr.foundations.DuckDbTypes
import java.math.BigDecimal

@Suppress("unused")
class FixedPointTypes {
    //start
    val decimalType: DuckDbType<BigDecimal> = DuckDbTypes.decimal
    val precise: DuckDbType<BigDecimal> = DuckDbTypes.decimal(18, 6)  // DECIMAL(18,6)
    //stop
}
