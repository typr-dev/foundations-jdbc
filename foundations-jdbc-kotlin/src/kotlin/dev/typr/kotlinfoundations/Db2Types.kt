package dev.typr.kotlinfoundations

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
    open val smallint: Db2Type<Short> = Db2Type(JavaDb2Types.smallint.bimap(SqlFunction { it }, { it }))
    open val integer: Db2Type<Int> = Db2Type(JavaDb2Types.integer.bimap(SqlFunction { it }, { it }))
    open val int_: Db2Type<Int> = Db2Type(JavaDb2Types.int_.bimap(SqlFunction { it }, { it }))
    open val bigint: Db2Type<Long> = Db2Type(JavaDb2Types.bigint.bimap(SqlFunction { it }, { it }))
    open val real: Db2Type<Float> = Db2Type(JavaDb2Types.real.bimap(SqlFunction { it }, { it }))
    open val double_: Db2Type<Double> = Db2Type(JavaDb2Types.double_.bimap(SqlFunction { it }, { it }))
    open val float_: Db2Type<Double> = Db2Type(JavaDb2Types.float_.bimap(SqlFunction { it }, { it }))
    open val boolean_: Db2Type<Boolean> = Db2Type(JavaDb2Types.boolean_.bimap(SqlFunction { it }, { it }))

    // Forward all other types directly from Java
    open val decimal = Db2Type(JavaDb2Types.decimal)
    open val numeric = Db2Type(JavaDb2Types.numeric)
    open val dec = Db2Type(JavaDb2Types.dec)
    open val decfloat = Db2Type(JavaDb2Types.decfloat)
    open val char_ = Db2Type(JavaDb2Types.char_)
    open val character = Db2Type(JavaDb2Types.character)
    open val varchar = Db2Type(JavaDb2Types.varchar)
    open val clob = Db2Type(JavaDb2Types.clob)
    open val graphic = Db2Type(JavaDb2Types.graphic)
    open val vargraphic = Db2Type(JavaDb2Types.vargraphic)
    open val dbclob = Db2Type(JavaDb2Types.dbclob)
    open val binary = Db2Type(JavaDb2Types.binary)
    open val varbinary = Db2Type(JavaDb2Types.varbinary)
    open val blob = Db2Type(JavaDb2Types.blob)
    open val date = Db2Type(JavaDb2Types.date)
    open val time = Db2Type(JavaDb2Types.time)
    open val timestamp = Db2Type(JavaDb2Types.timestamp)
    open val xml = Db2Type(JavaDb2Types.xml)
    open val rowid = Db2Type(JavaDb2Types.rowid)
    open val `object` = Db2Type(JavaDb2Types.`object`)
    open val unknown = Db2Type(JavaDb2Types.unknown)

    // Parameterized methods
    open fun decimal(precision: Int, scale: Int) = Db2Type(JavaDb2Types.decimal(precision, scale))

    open fun numeric(precision: Int, scale: Int) = Db2Type(JavaDb2Types.numeric(precision, scale))

    open fun decfloat(precision: Int) = Db2Type(JavaDb2Types.decfloat(precision))

    open fun char_(length: Int) = Db2Type(JavaDb2Types.char_(length))

    open fun varchar(length: Int) = Db2Type(JavaDb2Types.varchar(length))

    open fun clob(length: Int) = Db2Type(JavaDb2Types.clob(length))

    open fun graphic(length: Int) = Db2Type(JavaDb2Types.graphic(length))

    open fun vargraphic(length: Int) = Db2Type(JavaDb2Types.vargraphic(length))

    open fun dbclob(length: Int) = Db2Type(JavaDb2Types.dbclob(length))

    open fun binary(length: Int) = Db2Type(JavaDb2Types.binary(length))

    open fun varbinary(length: Int) = Db2Type(JavaDb2Types.varbinary(length))

    open fun blob(length: Int) = Db2Type(JavaDb2Types.blob(length))

    open fun timestamp(scale: Int) = Db2Type(JavaDb2Types.timestamp(scale))

    companion object : Db2Types()
}
