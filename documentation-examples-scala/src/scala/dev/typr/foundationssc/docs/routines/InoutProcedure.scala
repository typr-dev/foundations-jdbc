package dev.typr.foundationssc.docs.routines
import dev.typr.foundationssc.*

@SuppressWarnings(Array("unused"))
object InoutProcedure:
  private val tx: Transactor = null // placeholder

  // start
  // INOUT — the value goes in and comes back modified
  val applyDiscount: DbProcedure.Def2_1[String, BigDecimal, BigDecimal] =
    DbProcedure
      .define("apply_discount")
      .input(PgTypes.text) // discount_code IN
      .inout(PgTypes.numeric) // price INOUT
      .build()

  def applyDiscount(code: String, price: BigDecimal): BigDecimal =
    applyDiscount.call(code, price).transact(tx)
  // stop
