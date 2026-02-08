package dev.typr.foundations.docs.routines
import dev.typr.foundations.OracleTypes
import dev.typr.scalafoundations.*

import java.math.BigDecimal

@SuppressWarnings(Array("unused"))
object OracleExample:
  //start
  // Works with any database — just use the right types
  val applyDiscount: DbProcedure.Def2_1[BigDecimal, String, String] =
    DbProcedure.define("apply_discount")
      .in(OracleTypes.number)         // amount IN
      .inout(OracleTypes.varchar2)    // status INOUT
      .build()

  val getBalance: DbFunction.Def1[String, BigDecimal] =
    DbFunction.define("get_balance", OracleTypes.number)
      .in(OracleTypes.varchar2)       // account_id
      .build()
  //stop
