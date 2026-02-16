@file:Suppress("unused")
package dev.typr.foundationskt

object ParamDef {
    @JvmStatic
    fun `in`(type: DbType<*>): dev.typr.foundations.ParamDef =
        dev.typr.foundations.ParamDef.`in`(type.underlying)

    @JvmStatic
    fun of(type: DbType<*>, mode: dev.typr.foundations.ParamDef.Mode): dev.typr.foundations.ParamDef =
        dev.typr.foundations.ParamDef.of(type.underlying, mode)
}
