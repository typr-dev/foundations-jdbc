package dev.typr.foundations.docs.routines
import dev.typr.foundationssc.*

import java.sql.SQLException

@SuppressWarnings(Array("unused"))
object InoutProcedure:
  private val tx: Transactor = null // placeholder

  //start
  // INOUT — the value goes in and comes back modified
  val applyDiscount: DbProcedure.Def2_1[String, BigDecimal, BigDecimal] =
    DbProcedure.define("apply_discount")
      .in(PgTypes.text)           // discount_code IN
      .inout(PgTypes.numeric)     // price INOUT — goes in, comes back modified
      .build()

  @throws[SQLException]
  def applyDiscount(code: String, price: BigDecimal): BigDecimal =
    applyDiscount.call(code, price).transact(tx)
  //stop
