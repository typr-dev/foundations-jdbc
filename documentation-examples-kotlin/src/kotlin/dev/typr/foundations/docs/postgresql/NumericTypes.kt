package dev.typr.foundations.docs.postgresql

import dev.typr.kotlinfoundations.*
import dev.typr.kotlinfoundations.data.*
import dev.typr.foundations.data.Money
import java.math.BigDecimal

@Suppress("unused")
class NumericTypes {
    //start
    val intType: PgType<Int> = PgTypes.int4
    val decimalType: PgType<BigDecimal> = PgTypes.numeric
    val moneyType: PgType<Money> = PgTypes.money
    //stop
}
