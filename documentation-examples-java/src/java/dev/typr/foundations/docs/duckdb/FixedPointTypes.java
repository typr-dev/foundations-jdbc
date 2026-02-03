package dev.typr.foundations.docs.duckdb;

import dev.typr.foundations.DuckDbType;
import dev.typr.foundations.DuckDbTypes;

import java.math.BigDecimal;

@SuppressWarnings("unused")
public class FixedPointTypes {
    //start
    DuckDbType<BigDecimal> decimalType = DuckDbTypes.decimal;
    DuckDbType<BigDecimal> precise = DuckDbTypes.decimal(18, 6);  // DECIMAL(18,6)
    //stop
}
