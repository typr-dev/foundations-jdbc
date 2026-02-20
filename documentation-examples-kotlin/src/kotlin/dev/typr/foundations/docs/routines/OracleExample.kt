package dev.typr.foundations.docs.routines

import dev.typr.foundationskt.*
import java.math.BigDecimal

@Suppress("unused")
class OracleExample {
    //start
    // Works with any database — just use the right types
    val applyDiscount =
        DbProcedure.define("apply_discount")
            .input(OracleTypes.number)         // amount IN
            .inout(OracleTypes.varchar2)      // status INOUT
            .build()

    val getBalance =
        DbFunction.define("get_balance", OracleTypes.number)
            .input(OracleTypes.varchar2)       // account_id
            .build()
    //stop
}
