package dev.typr.foundations.docs.oracle;

import dev.typr.foundations.OracleType;
import dev.typr.foundations.OracleTypes;
import dev.typr.foundations.data.OracleIntervalDS;
import dev.typr.foundations.data.OracleIntervalYM;

@SuppressWarnings("unused")
public class IntervalTypes {
    //start
    OracleType<OracleIntervalYM> ymType = OracleTypes.intervalYearToMonth;
    OracleType<OracleIntervalYM> ym4 = OracleTypes.intervalYearToMonth(4);

    OracleType<OracleIntervalDS> dsType = OracleTypes.intervalDayToSecond;
    OracleType<OracleIntervalDS> ds96 = OracleTypes.intervalDayToSecond(9, 6);

    // Create and use intervals
    OracleIntervalYM interval = OracleIntervalYM.parse("+02-05");  // 2 years, 5 months
    String oracle = interval.toOracleFormat();  // "+02-05"
    String iso = interval.toIso8601();          // "P2Y5M"
    //stop
}
