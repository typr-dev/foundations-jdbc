package dev.typr.foundationskt.docs.routines

import dev.typr.foundationskt.*
import java.math.BigDecimal

@Suppress("unused")
class InoutProcedure {
    private lateinit var tx: Transactor

    //start
    // INOUT — the value goes in and comes back modified
    val applyDiscount: DbProcedure.Def2_1<String, BigDecimal, BigDecimal> =
        DbProcedure.define("apply_discount")
            .input(PgTypes.text)           // discount_code IN
            .inout(PgTypes.numeric)       // price INOUT — goes in, comes back modified
            .build()

    fun applyDiscount(code: String, price: BigDecimal): BigDecimal =
        applyDiscount
            .call(code, price)
            .transact(tx)
    //stop
}
