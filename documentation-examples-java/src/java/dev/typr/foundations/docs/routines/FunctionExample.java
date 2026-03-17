package dev.typr.foundations.docs.routines;

import dev.typr.foundations.*;
import java.math.BigDecimal;

@SuppressWarnings("unused")
public class FunctionExample {
  Transactor tx = null; // placeholder

  // start
  // Functions use SELECT instead of CALL — every DbType reads correctly
  static final DbFunction.Def2<BigDecimal, String, BigDecimal> calcTax =
      DbFunction.define("calculate_tax", PgTypes.numeric)
          .input(PgTypes.numeric)
          .input(PgTypes.text)
          .build();

  // Zero-argument function
  static final DbFunction.Def0<Integer> nextId = DbFunction.define("next_id", PgTypes.int4).build();

  BigDecimal calculateTax(BigDecimal amount, String region) {
    return calcTax.call(amount, region).transact(tx);
  }
  // stop
}
