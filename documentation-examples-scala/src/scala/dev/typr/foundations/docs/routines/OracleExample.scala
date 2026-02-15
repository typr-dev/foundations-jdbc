package dev.typr.foundations.docs.routines
import dev.typr.foundationssc.*

@SuppressWarnings(Array("unused"))
object OracleExample:
  //start
  // Works with any database — just use the right types
  val applyDiscount =
    DbProcedure.define("apply_discount")
      .in(OracleTypes.number)         // amount IN
      .inout(OracleTypes.varchar2)    // status INOUT
      .build()

  val getBalance =
    DbFunction.define("get_balance", OracleTypes.number)
      .in(OracleTypes.varchar2)       // account_id
      .build()
  //stop
