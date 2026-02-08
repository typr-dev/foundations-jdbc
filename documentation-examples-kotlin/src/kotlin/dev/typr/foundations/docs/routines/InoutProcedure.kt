package dev.typr.foundations.docs.routines

import dev.typr.kotlinfoundations.*
import java.math.BigDecimal
import java.sql.SQLException

@Suppress("unused")
class InoutProcedure {
    private val tx: Transactor? = null // placeholder

    //start
    companion object {
        // INOUT — the value goes in and comes back modified
        val applyDiscount: DbProcedure.Def2_1<String, BigDecimal, BigDecimal> =
            DbProcedure.define("apply_discount")
                .`in`(PgTypes.text)           // discount_code IN
                .inout(PgTypes.numeric)       // price INOUT — goes in, comes back modified
                .build()
    }

    @Throws(SQLException::class)
    fun applyDiscount(code: String, price: BigDecimal): BigDecimal =
        InoutProcedure.applyDiscount.call(code, price).transact(tx!!)
    //stop
}
