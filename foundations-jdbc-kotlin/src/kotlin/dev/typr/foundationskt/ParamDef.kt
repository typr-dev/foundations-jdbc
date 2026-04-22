@file:Suppress("unused")
package dev.typr.foundationskt

import dev.typr.foundations.ParamDef as JavaParamDef

object ParamDef {
    @JvmStatic
    fun input(type: DbType<*>): JavaParamDef =
        JavaParamDef.input(type.underlying)

    @JvmStatic
    fun of(type: DbType<*>, mode: ParamDefMode): JavaParamDef =
        JavaParamDef.of(type.underlying, mode)
}
