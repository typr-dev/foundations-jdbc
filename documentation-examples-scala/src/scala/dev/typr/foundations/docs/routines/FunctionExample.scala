package dev.typr.foundations.docs.routines
import dev.typr.foundations.PgTypes
import dev.typr.scalafoundations.*

import java.math.BigDecimal
import java.sql.SQLException

@SuppressWarnings(Array("unused"))
object FunctionExample:
  private val tx: dev.typr.foundations.Transactor = null // placeholder

  //start
  // Functions use SELECT instead of CALL — every DbType reads correctly
  val calcTax: DbFunction.Def2[BigDecimal, String, BigDecimal] =
    DbFunction.define("calculate_tax", PgTypes.numeric)
      .in(PgTypes.numeric)    // amount
      .in(PgTypes.text)       // region
      .build()

  // Zero-argument function
  val nextId: DbFunction.Def0[Integer] =
    DbFunction.define("next_id", PgTypes.int4)
      .build()

  @throws[SQLException]
  def calculateTax(amount: BigDecimal, region: String): BigDecimal =
    calcTax.call(amount, region).transact(tx)
  //stop
