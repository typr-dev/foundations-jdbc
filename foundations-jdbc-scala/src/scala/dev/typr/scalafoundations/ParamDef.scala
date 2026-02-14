package dev.typr.scalafoundations

type ParamDef = dev.typr.foundations.ParamDef

object ParamDef:
  def in(dbType: DbType[?]): dev.typr.foundations.ParamDef =
    dev.typr.foundations.ParamDef.in(dbType.underlying)
  def of(dbType: DbType[?], mode: dev.typr.foundations.ParamDef.Mode): dev.typr.foundations.ParamDef =
    dev.typr.foundations.ParamDef.of(dbType.underlying, mode)
  type Mode = dev.typr.foundations.ParamDef.Mode
