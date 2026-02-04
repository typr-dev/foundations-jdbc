package dev.typr.foundations.docs.oracle
import dev.typr.scalafoundations.*
import dev.typr.scalafoundations.data.*



@SuppressWarnings(Array("unused"))
object IntervalTypes:
  //start
  val ymType: OracleType[OracleIntervalYM] = OracleTypes.intervalYearToMonth
  val ym4: OracleType[OracleIntervalYM] = OracleTypes.intervalYearToMonth(4)

  val dsType: OracleType[OracleIntervalDS] = OracleTypes.intervalDayToSecond
  val ds96: OracleType[OracleIntervalDS] = OracleTypes.intervalDayToSecond(9, 6)

  // Create and use intervals
  val interval: OracleIntervalYM = OracleIntervalYM.parse("+02-05")  // 2 years, 5 months
  val oracle: String = interval.toOracleFormat()  // "+02-05"
  val iso: String = interval.toIso8601()          // "P2Y5M"
  //stop
