package dev.typr.foundations.docs.db2;

import dev.typr.foundations.Db2Type;
import dev.typr.foundations.Db2Types;
import java.math.BigDecimal;

@SuppressWarnings("unused")
public class FixedPointTypes {
  // start
  Db2Type<BigDecimal> decType = Db2Types.decimal;
  Db2Type<BigDecimal> preciseType = Db2Types.decimalOf(10, 2);
  Db2Type<BigDecimal> decfloatType = Db2Types.decfloat;
  Db2Type<BigDecimal> decfloat16 = Db2Types.decfloatOf(16);
  // stop
}
