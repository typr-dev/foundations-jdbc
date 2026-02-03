package dev.typr.foundations.docs.postgresql;

import dev.typr.foundations.PgType;
import dev.typr.foundations.PgTypes;
import dev.typr.foundations.data.Money;

import java.math.BigDecimal;

@SuppressWarnings("unused")
public class NumericTypes {
    //start
    PgType<Integer> intType = PgTypes.int4;
    PgType<BigDecimal> decimalType = PgTypes.numeric;
    PgType<Money> moneyType = PgTypes.money;
    //stop
}
