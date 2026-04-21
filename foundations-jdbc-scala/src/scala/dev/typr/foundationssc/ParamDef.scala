package dev.typr.foundationssc

type ParamDef = dev.typr.foundations.ParamDef

object ParamDef:
  def input(dbType: DbType[?]): ParamDef =
    dev.typr.foundations.ParamDef.input(dbType.underlying)
  def of(dbType: DbType[?], mode: Mode): ParamDef =
    dev.typr.foundations.ParamDef.of(dbType.underlying, mode)
  type Mode = dev.typr.foundations.ParamDef.Mode
