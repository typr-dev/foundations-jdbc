package dev.typr.foundationskt.docs.postgresql

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*
import java.math.BigDecimal

@Suppress("unused")
class NumericTypes {
    //start
    val intType: PgType<Int> = PgTypes.int4
    val decimalType: PgType<BigDecimal> = PgTypes.numeric
    val moneyType: PgType<Money> = PgTypes.money
    //stop
}
