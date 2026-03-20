package dev.typr.foundations.docs.routines;

import dev.typr.foundations.*;
import java.math.BigDecimal;

@SuppressWarnings("unused")
public class InoutProcedure {
  Transactor tx = null; // placeholder

  // start
  // INOUT — the value goes in and comes back modified
  static final DbProcedure.Def2_1<String, BigDecimal, BigDecimal> applyDiscount =
      DbProcedure.define("apply_discount").input(PgTypes.text).inout(PgTypes.numeric).build();

  BigDecimal applyDiscount(String code, BigDecimal price) {
    return applyDiscount.call(code, price).transact(tx);
  }
  // stop
}
