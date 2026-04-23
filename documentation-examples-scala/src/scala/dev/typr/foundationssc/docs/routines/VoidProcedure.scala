package dev.typr.foundationssc.docs.routines
import dev.typr.foundationssc.*

@SuppressWarnings(Array("unused"))
object VoidProcedure:
  private val tx: Transactor = null // placeholder

  // start
  // A void procedure — no OUT parameters, just side effects
  val auditLog: DbProcedure.Def2_0[String, String] =
    DbProcedure
      .define("audit_log")
      .input(PgTypes.text) // action
      .input(PgTypes.text) // details
      .build()

  def logAction(action: String, details: String): Unit =
    auditLog.call(action, details).transact(tx)
  // stop
