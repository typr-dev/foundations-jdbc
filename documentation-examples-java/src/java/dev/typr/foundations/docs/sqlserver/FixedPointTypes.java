package dev.typr.foundations.docs.sqlserver;

import dev.typr.foundations.SqlServerType;
import dev.typr.foundations.SqlServerTypes;
import java.math.BigDecimal;

@SuppressWarnings("unused")
public class FixedPointTypes {
  // start
  SqlServerType<BigDecimal> decimalType = SqlServerTypes.decimal;
  SqlServerType<BigDecimal> precise = SqlServerTypes.decimal(18, 4);
  SqlServerType<BigDecimal> moneyType = SqlServerTypes.money;
  // stop
}
