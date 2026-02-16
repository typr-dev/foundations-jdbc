package dev.typr.foundations.docs.routines

import dev.typr.foundationskt.*
import java.math.BigDecimal

@Suppress("unused")
class InoutProcedure {
    private val tx: Transactor? = null // placeholder

    //start
    companion object {
        // INOUT — the value goes in and comes back modified
        val applyDiscount =
            DbProcedure.define("apply_discount")
                .input(PgTypes.text)           // discount_code IN
                .inout(PgTypes.numeric)       // price INOUT — goes in, comes back modified
                .build()
    }

    fun applyDiscount(code: String, price: BigDecimal): BigDecimal =
        InoutProcedure.applyDiscount
            .call(code, price)
            .transact(tx!!)
    //stop
}
