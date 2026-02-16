package dev.typr.foundations.docs.routines
import dev.typr.foundationssc.*

import java.sql.SQLException

@SuppressWarnings(Array("unused"))
object FunctionExample:
  private val tx: Transactor = null // placeholder

  //start
  // Functions use SELECT instead of CALL — every DbType reads correctly
  val calcTax =
    DbFunction.define("calculate_tax", PgTypes.numeric)
      .in(PgTypes.numeric)    // amount
      .in(PgTypes.text)       // region
      .build()

  // Zero-argument function
  val nextId =
    DbFunction.define("next_id", PgTypes.int4)
      .build()

  @throws[SQLException]
  def calculateTax(amount: BigDecimal, region: String): BigDecimal =
    calcTax.call(amount, region).transact(tx)
  //stop
