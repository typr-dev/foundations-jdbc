package dev.typr.foundations.docs.sqlserver

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*
import java.math.BigDecimal

@Suppress("unused")
class FixedPointTypes {
    //start
    val decimalType: SqlServerType<BigDecimal> = SqlServerTypes.decimal
    val precise: SqlServerType<BigDecimal> = SqlServerTypes.decimal(18, 4)
    val moneyType: SqlServerType<BigDecimal> = SqlServerTypes.money
    //stop
}
