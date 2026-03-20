package dev.typr.foundationssc

import java.sql.Connection

/** Scala wrapper for dev.typr.foundations.Procedure with Unit instead of Void. */
class Procedure[Out] private[foundationssc] (
    private[foundationssc] val javaProcedure: dev.typr.foundations.Procedure[?],
    private[foundationssc] val mapResult: Any => Out
) {

  def call(inValues: Any*): ProcedureOp[Out] = {
    val javaOp = javaProcedure
      .asInstanceOf[dev.typr.foundations.Procedure[Any]]
      .call(inValues.map(_.asInstanceOf[AnyRef]): _*)
    new ProcedureOp(javaOp, mapResult)
  }
}

object Procedure {

  private[foundationssc] def fromVoid(jp: dev.typr.foundations.Procedure[Void]): Procedure[Unit] =
    new Procedure(jp, _ => ())

  private[foundationssc] def fromJava[Out](jp: dev.typr.foundations.Procedure[Out]): Procedure[Out] =
    new Procedure(jp, _.asInstanceOf[Out])

  def buildVoid(name: String, params: java.util.List[dev.typr.foundations.ParamDef]): dev.typr.foundations.Procedure[Void] =
    dev.typr.foundations.Procedure.buildVoid(name, params)

  def buildSingleOut[O](name: String, params: java.util.List[dev.typr.foundations.ParamDef]): dev.typr.foundations.Procedure[O] =
    dev.typr.foundations.Procedure.buildSingleOut(name, params)

  def buildMultiOut[O](
      name: String,
      params: java.util.List[dev.typr.foundations.ParamDef],
      assembler: java.util.function.Function[Array[AnyRef], O]
  ): dev.typr.foundations.Procedure[O] =
    dev.typr.foundations.Procedure.buildMultiOut(name, params, assembler)

  def buildFunction[R](name: String, inParams: java.util.List[dev.typr.foundations.ParamDef], returnType: DbType[R]): dev.typr.foundations.Procedure[R] =
    dev.typr.foundations.Procedure.buildFunction(name, inParams, returnType.underlying)
}

/** Operation returned by Procedure.call() -- wraps a Java Operation with result conversion. */
class ProcedureOp[Out] private[foundationssc] (
    private val javaOp: dev.typr.foundations.Operation[Any],
    private val mapResult: Any => Out
) {

  def run(conn: Connection): Out = mapResult(javaOp.run(conn))

  def transact(transactor: Transactor): Out =
    mapResult(javaOp.transact(transactor.underlying))
}
