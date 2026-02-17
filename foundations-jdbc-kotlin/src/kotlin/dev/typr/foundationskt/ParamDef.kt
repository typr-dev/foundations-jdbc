@file:Suppress("unused")
package dev.typr.foundationskt

object ParamDef {
    @JvmStatic
    fun input(type: DbType<*>): dev.typr.foundations.ParamDef =
        dev.typr.foundations.ParamDef.input(type.underlying)

    @JvmStatic
    fun of(type: DbType<*>, mode: dev.typr.foundations.ParamDef.Mode): dev.typr.foundations.ParamDef =
        dev.typr.foundations.ParamDef.of(type.underlying, mode)
}
