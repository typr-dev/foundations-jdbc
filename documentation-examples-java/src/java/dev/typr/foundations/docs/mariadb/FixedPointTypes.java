package dev.typr.foundations.docs.mariadb;

import dev.typr.foundations.MariaType;
import dev.typr.foundations.MariaTypes;

import java.math.BigDecimal;

@SuppressWarnings("unused")
public class FixedPointTypes {
    //start
    MariaType<BigDecimal> decimalType = MariaTypes.decimal;
    MariaType<BigDecimal> preciseDecimal = MariaTypes.decimal(10, 2);
    //stop
}
