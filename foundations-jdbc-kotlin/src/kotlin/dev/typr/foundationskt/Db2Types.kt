package dev.typr.foundationskt

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
    open val smallint: Db2Type<Short> = Db2Type(JavaDb2Types.smallint.transform(SqlFunction { it }, { it }))
    open val integer: Db2Type<Int> = Db2Type(JavaDb2Types.integer.transform(SqlFunction { it }, { it }))
    open val int_: Db2Type<Int> = Db2Type(JavaDb2Types.int_.transform(SqlFunction { it }, { it }))
    open val bigint: Db2Type<Long> = Db2Type(JavaDb2Types.bigint.transform(SqlFunction { it }, { it }))
    open val real: Db2Type<Float> = Db2Type(JavaDb2Types.real.transform(SqlFunction { it }, { it }))
    open val double_: Db2Type<Double> = Db2Type(JavaDb2Types.double_.transform(SqlFunction { it }, { it }))
    open val float_: Db2Type<Double> = Db2Type(JavaDb2Types.float_.transform(SqlFunction { it }, { it }))
    open val boolean_: Db2Type<Boolean> = Db2Type(JavaDb2Types.boolean_.transform(SqlFunction { it }, { it }))

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
    open fun decimalOf(precision: Int, scale: Int) = Db2Type(JavaDb2Types.decimalOf(precision, scale))

    open fun numericOf(precision: Int, scale: Int) = Db2Type(JavaDb2Types.numericOf(precision, scale))

    open fun decfloatOf(precision: Int) = Db2Type(JavaDb2Types.decfloatOf(precision))

    open fun char_Of(length: Int) = Db2Type(JavaDb2Types.char_Of(length))

    open fun varcharOf(length: Int) = Db2Type(JavaDb2Types.varcharOf(length))

    open fun clobOf(length: Int) = Db2Type(JavaDb2Types.clobOf(length))

    open fun graphicOf(length: Int) = Db2Type(JavaDb2Types.graphicOf(length))

    open fun vargraphicOf(length: Int) = Db2Type(JavaDb2Types.vargraphicOf(length))

    open fun dbclobOf(length: Int) = Db2Type(JavaDb2Types.dbclobOf(length))

    open fun binaryOf(length: Int) = Db2Type(JavaDb2Types.binaryOf(length))

    open fun varbinaryOf(length: Int) = Db2Type(JavaDb2Types.varbinaryOf(length))

    open fun blobOf(length: Int) = Db2Type(JavaDb2Types.blobOf(length))

    open fun timestampOf(scale: Int) = Db2Type(JavaDb2Types.timestampOf(scale))

    open val json = Db2Type(JavaDb2Types.json)

    // JSON-encoded row types

    /** A JSON column type that stores a single row as a positional JSON array: [val1, val2, val3]. */
    open fun <Row : Any> jsonArrayEncoded(parser: RowCodec<Row>) =
        Db2Type<Row>(JavaDb2Types.jsonArrayEncoded(parser.underlying))

    /** A JSON column type that stores a list of rows, each as a positional JSON array. */
    open fun <Row : Any> jsonArrayEncodedList(parser: RowCodec<Row>) =
        Db2Type<List<Row>>(JavaDb2Types.jsonArrayEncodedList(parser.underlying))

    /** A JSON column type that stores a single row as a keyed JSON object: {"col": val, ...}. */
    open fun <Row : Any> jsonObjectEncoded(parser: RowCodecNamed<Row>) =
        Db2Type<Row>(JavaDb2Types.jsonObjectEncoded(parser.underlying))

    /** A JSON column type that stores a list of rows, each as a keyed JSON object. */
    open fun <Row : Any> jsonObjectEncodedList(parser: RowCodecNamed<Row>) =
        Db2Type<List<Row>>(JavaDb2Types.jsonObjectEncodedList(parser.underlying))

    companion object : Db2Types()
}
