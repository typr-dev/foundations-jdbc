package dev.typr.foundations.docs.routines
import dev.typr.foundations.{PgTypes, Tuple}
import dev.typr.scalafoundations.*

import java.sql.SQLException

@SuppressWarnings(Array("unused"))
object VoidProcedure:
  private val tx: dev.typr.foundations.Transactor = null // placeholder

  //start
  // A void procedure — no OUT parameters, just side effects
  val auditLog: DbProcedure.Def2_0[String, String] =
    DbProcedure.define("audit_log")
      .in(PgTypes.text)       // action
      .in(PgTypes.text)       // details
      .build()

  @throws[SQLException]
  def logAction(action: String, details: String): Unit =
    auditLog.call(action, details).transact(tx)
  //stop
