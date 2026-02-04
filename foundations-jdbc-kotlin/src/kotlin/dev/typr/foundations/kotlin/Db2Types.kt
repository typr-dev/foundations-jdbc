package dev.typr.foundations.kotlin

import dev.typr.foundations.Db2Type
import dev.typr.foundations.SqlFunction
import dev.typr.foundations.Db2Types as JavaDb2Types

/**
 * Kotlin-friendly Db2Type instances that use Kotlin types instead of Java boxed types.
 * All types from dev.typr.foundations.Db2Types are available here, with primitives converted to Kotlin types.
 *
 * Extend this class to add your own custom types to a shared set of type definitions.
 */
open class Db2Types {
    // Primitives - convert Java boxed types to Kotlin native types
    open val smallint: Db2Type<Short> = JavaDb2Types.smallint.bimap(SqlFunction { it }, { it })
    open val integer: Db2Type<Int> = JavaDb2Types.integer.bimap(SqlFunction { it }, { it })
    open val int_: Db2Type<Int> = JavaDb2Types.int_.bimap(SqlFunction { it }, { it })
    open val bigint: Db2Type<Long> = JavaDb2Types.bigint.bimap(SqlFunction { it }, { it })
    open val real: Db2Type<Float> = JavaDb2Types.real.bimap(SqlFunction { it }, { it })
    open val double_: Db2Type<Double> = JavaDb2Types.double_.bimap(SqlFunction { it }, { it })
    open val float_: Db2Type<Double> = JavaDb2Types.float_.bimap(SqlFunction { it }, { it })
    open val boolean_: Db2Type<Boolean> = JavaDb2Types.boolean_.bimap(SqlFunction { it }, { it })

    // Forward all other types directly from Java
    open val decimal = JavaDb2Types.decimal
    open val numeric = JavaDb2Types.numeric
    open val dec = JavaDb2Types.dec
    open val decfloat = JavaDb2Types.decfloat
    open val char_ = JavaDb2Types.char_
    open val character = JavaDb2Types.character
    open val varchar = JavaDb2Types.varchar
    open val clob = JavaDb2Types.clob
    open val graphic = JavaDb2Types.graphic
    open val vargraphic = JavaDb2Types.vargraphic
    open val dbclob = JavaDb2Types.dbclob
    open val binary = JavaDb2Types.binary
    open val varbinary = JavaDb2Types.varbinary
    open val blob = JavaDb2Types.blob
    open val date = JavaDb2Types.date
    open val time = JavaDb2Types.time
    open val timestamp = JavaDb2Types.timestamp
    open val xml = JavaDb2Types.xml
    open val rowid = JavaDb2Types.rowid
    open val `object` = JavaDb2Types.`object`
    open val unknown = JavaDb2Types.unknown

    companion object : Db2Types()
}
