package dev.typr.foundations.docs.oracle

import dev.typr.kotlinfoundations.*
import dev.typr.kotlinfoundations.data.*
import dev.typr.foundations.data.OracleIntervalDS
import dev.typr.foundations.data.OracleIntervalYM

@Suppress("unused")
class IntervalTypes {
    //start
    val ymType: OracleType<OracleIntervalYM> = OracleTypes.intervalYearToMonth
    val ym4: OracleType<OracleIntervalYM> = OracleTypes.intervalYearToMonth(4)

    val dsType: OracleType<OracleIntervalDS> = OracleTypes.intervalDayToSecond
    val ds96: OracleType<OracleIntervalDS> = OracleTypes.intervalDayToSecond(9, 6)

    // Create and use intervals
    val interval: OracleIntervalYM = OracleIntervalYM.parse("+02-05")  // 2 years, 5 months
    val oracle: String = interval.toOracleFormat()  // "+02-05"
    val iso: String = interval.toIso8601()          // "P2Y5M"
    //stop
}
