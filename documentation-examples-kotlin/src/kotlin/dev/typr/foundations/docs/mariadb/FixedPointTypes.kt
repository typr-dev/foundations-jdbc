package dev.typr.foundations.docs.mariadb

import dev.typr.kotlinfoundations.*
import dev.typr.kotlinfoundations.data.*
import java.math.BigDecimal

@Suppress("unused")
class FixedPointTypes {
    //start
    val decimalType: MariaType<BigDecimal> = MariaTypes.decimal
    val preciseDecimal: MariaType<BigDecimal> = MariaTypes.decimal(10, 2)
    //stop
}
