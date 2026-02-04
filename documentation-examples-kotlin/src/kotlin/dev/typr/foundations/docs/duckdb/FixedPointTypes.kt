package dev.typr.foundations.docs.duckdb

import dev.typr.kotlinfoundations.*
import dev.typr.kotlinfoundations.data.*
import java.math.BigDecimal

@Suppress("unused")
class FixedPointTypes {
    //start
    val decimalType: DuckDbType<BigDecimal> = DuckDbTypes.decimal
    val precise: DuckDbType<BigDecimal> = DuckDbTypes.decimal(18, 6)  // DECIMAL(18,6)
    //stop
}
