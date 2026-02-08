package dev.typr.foundations.docs.routines

import dev.typr.kotlinfoundations.*
import java.math.BigDecimal

@Suppress("unused")
class OracleExample {
    //start
    companion object {
        // Works with any database — just use the right types
        val applyDiscount: DbProcedure.Def2_1<BigDecimal, String, String> =
            DbProcedure.define("apply_discount")
                .`in`(OracleTypes.number)         // amount IN
                .inout(OracleTypes.varchar2)      // status INOUT
                .build()

        val getBalance: DbFunction.Def1<String, BigDecimal> =
            DbFunction.define("get_balance", OracleTypes.number)
                .`in`(OracleTypes.varchar2)       // account_id
                .build()
    }
    //stop
}
