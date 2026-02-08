package dev.typr.scalafoundations

import java.sql.{Connection, SQLException}

/** Scala wrapper for dev.typr.foundations.Procedure with Unit instead of Void. */
class Procedure[Out] private[scalafoundations] (
    private[scalafoundations] val javaProcedure: dev.typr.foundations.Procedure[?],
    private[scalafoundations] val mapResult: Any => Out
) {

  def call(inValues: Any*): ProcedureOp[Out] = {
    val javaOp = javaProcedure
      .asInstanceOf[dev.typr.foundations.Procedure[Any]]
      .call(inValues.map(_.asInstanceOf[AnyRef]): _*)
    new ProcedureOp(javaOp, mapResult)
  }
}

object Procedure {

  private[scalafoundations] def fromVoid(jp: dev.typr.foundations.Procedure[Void]): Procedure[Unit] =
    new Procedure(jp, _ => ())

  private[scalafoundations] def fromJava[Out](jp: dev.typr.foundations.Procedure[Out]): Procedure[Out] =
    new Procedure(jp, _.asInstanceOf[Out])
}

/** Operation returned by Procedure.call() — wraps a Java Operation with result conversion. */
class ProcedureOp[Out] private[scalafoundations] (
    private val javaOp: dev.typr.foundations.Operation[Any],
    private val mapResult: Any => Out
) {

  @throws[SQLException]
  def run(conn: Connection): Out = mapResult(javaOp.run(conn))

  def runUnchecked(conn: Connection): Out =
    try run(conn)
    catch { case e: SQLException => throw new RuntimeException(e) }

  @throws[SQLException]
  def transact(transactor: dev.typr.foundations.Transactor): Out =
    mapResult(javaOp.transact(transactor))
}
