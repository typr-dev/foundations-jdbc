package dev.typr.foundationssc

object Procedure:
  def buildVoid(name: String, params: java.util.List[ParamDef]): Procedure[Void] =
    dev.typr.foundations.Procedure.buildVoid(name, params)

  def buildFunction[R](name: String, params: java.util.List[ParamDef], returnType: DbType[R]): Procedure[R] =
    dev.typr.foundations.Procedure.buildFunction(name, params, returnType.underlying)
