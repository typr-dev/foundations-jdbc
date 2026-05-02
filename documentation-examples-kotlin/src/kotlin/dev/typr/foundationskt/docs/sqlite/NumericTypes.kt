package dev.typr.foundationskt.docs.sqlite

import dev.typr.foundationskt.*

@Suppress("unused")
class NumericTypes {
    //start
    val numericType: SqliteType<java.math.BigDecimal> = SqliteTypes.numeric
    val decimalType: SqliteType<java.math.BigDecimal> = SqliteTypes.decimal
    val precise: SqliteType<java.math.BigDecimal> = SqliteTypes.decimalOf(18, 6)
    //stop
}
