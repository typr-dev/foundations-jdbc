package dev.typr.foundations.docs.routines

import dev.typr.foundationskt.*
import java.math.BigDecimal

@Suppress("unused")
class FunctionExample {
    private val tx: Transactor? = null // placeholder

    //start
    companion object {
        // Functions use SELECT instead of CALL — every DbType reads correctly
        val calcTax =
            DbFunction.define("calculate_tax", PgTypes.numeric)
                .input(PgTypes.numeric)    // amount
                .input(PgTypes.text)       // region
                .build()

        // Zero-argument function
        val nextId =
            DbFunction.define("next_id", PgTypes.int4)
                .build()
    }

    fun calculateTax(amount: BigDecimal, region: String): BigDecimal =
        calcTax.call(amount, region)
            .transact(tx!!)
    //stop
}
