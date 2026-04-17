package dev.typr.foundationskt

import dev.typr.foundations.SqlFunction
import dev.typr.foundations.SqlServerTypes as JavaSqlServerTypes

/**
 * Kotlin-friendly SqlServerType instances that use Kotlin types instead of Java boxed types.
 * All types from dev.typr.foundations.SqlServerTypes are available here, with primitives converted to Kotlin types.
 *
 * Extend this class to add your own custom types to a shared set of type definitions.
 */
open class SqlServerTypes {
    // Primitives - convert Java boxed types to Kotlin native types
    open val smallint: SqlServerType<Short> = SqlServerType(JavaSqlServerTypes.smallint.transform(SqlFunction { it }, { it }))
    open val int_: SqlServerType<Int> = SqlServerType(JavaSqlServerTypes.int_.transform(SqlFunction { it }, { it }))

    /** Alias for [int_] — aesthetic, avoids the Java-keyword `_` suffix. */
    open val integer: SqlServerType<Int> = int_

    open val bigint: SqlServerType<Long> = SqlServerType(JavaSqlServerTypes.bigint.transform(SqlFunction { it }, { it }))
    open val real: SqlServerType<Float> = SqlServerType(JavaSqlServerTypes.real.transform(SqlFunction { it }, { it }))

    /** Alias for [real] — aesthetic cross-palette naming. 4B IEEE 754. */
    open val float4: SqlServerType<Float> = real

    open val float_: SqlServerType<Double> = SqlServerType(JavaSqlServerTypes.float_.transform(SqlFunction { it }, { it }))

    /** Alias for [float_] — aesthetic, avoids the Java-keyword `_` suffix. 8B IEEE 754 (FLOAT ≡ FLOAT(53)). */
    open val float8: SqlServerType<Double> = float_
    open val bit: SqlServerType<Boolean> = SqlServerType(JavaSqlServerTypes.bit.transform(SqlFunction { it }, { it }))

    // Forward all other types directly from Java
    open val tinyint = SqlServerType(JavaSqlServerTypes.tinyint)
    open val decimal = SqlServerType(JavaSqlServerTypes.decimal)
    open val numeric = SqlServerType(JavaSqlServerTypes.numeric)
    open val money = SqlServerType(JavaSqlServerTypes.money)
    open val smallmoney = SqlServerType(JavaSqlServerTypes.smallmoney)
    open val char_ = SqlServerType(JavaSqlServerTypes.char_)

    /** Alias for [char_] — aesthetic, avoids the Java-keyword `_` suffix. */
    open val character = char_
    open val varchar = SqlServerType(JavaSqlServerTypes.varchar)
    open val varcharMax = SqlServerType(JavaSqlServerTypes.varcharMax)
    open val text = SqlServerType(JavaSqlServerTypes.text)
    open val nchar = SqlServerType(JavaSqlServerTypes.nchar)
    open val nvarchar = SqlServerType(JavaSqlServerTypes.nvarchar)
    open val nvarcharMax = SqlServerType(JavaSqlServerTypes.nvarcharMax)
    open val ntext = SqlServerType(JavaSqlServerTypes.ntext)
    open val binary = SqlServerType(JavaSqlServerTypes.binary)
    open val varbinary = SqlServerType(JavaSqlServerTypes.varbinary)
    open val varbinaryMax = SqlServerType(JavaSqlServerTypes.varbinaryMax)
    open val image = SqlServerType(JavaSqlServerTypes.image)
    open val date = SqlServerType(JavaSqlServerTypes.date)
    open val time = SqlServerType(JavaSqlServerTypes.time)
    open val datetime = SqlServerType(JavaSqlServerTypes.datetime)
    open val smalldatetime = SqlServerType(JavaSqlServerTypes.smalldatetime)
    open val datetime2 = SqlServerType(JavaSqlServerTypes.datetime2)
    open val datetimeoffset = SqlServerType(JavaSqlServerTypes.datetimeoffset)
    open val uniqueidentifier = SqlServerType(JavaSqlServerTypes.uniqueidentifier)
    open val xml = SqlServerType(JavaSqlServerTypes.xml)
    open val json = SqlServerType(JavaSqlServerTypes.json)
    open val vector = SqlServerType(JavaSqlServerTypes.vector)
    open val rowversion = SqlServerType(JavaSqlServerTypes.rowversion)
    open val timestamp = SqlServerType(JavaSqlServerTypes.timestamp)
    open val hierarchyid = SqlServerType(JavaSqlServerTypes.hierarchyid)
    open val sqlVariant = SqlServerType(JavaSqlServerTypes.sqlVariant)
    open val geography = SqlServerType(JavaSqlServerTypes.geography)
    open val geometry = SqlServerType(JavaSqlServerTypes.geometry)
    open val unknown = SqlServerType(JavaSqlServerTypes.unknown)

    // Parameterized methods
    open fun decimalOf(precision: Int, scale: Int) = SqlServerType(JavaSqlServerTypes.decimalOf(precision, scale))

    open fun numericOf(precision: Int, scale: Int) = SqlServerType(JavaSqlServerTypes.numericOf(precision, scale))

    open fun char_Of(length: Int) = SqlServerType(JavaSqlServerTypes.char_Of(length))

    open fun varcharOf(length: Int) = SqlServerType(JavaSqlServerTypes.varcharOf(length))

    open fun ncharOf(length: Int) = SqlServerType(JavaSqlServerTypes.ncharOf(length))

    open fun nvarcharOf(length: Int) = SqlServerType(JavaSqlServerTypes.nvarcharOf(length))

    open fun binaryOf(length: Int) = SqlServerType(JavaSqlServerTypes.binaryOf(length))

    open fun varbinaryOf(length: Int) = SqlServerType(JavaSqlServerTypes.varbinaryOf(length))

    open fun timeOf(scale: Int) = SqlServerType(JavaSqlServerTypes.timeOf(scale))

    open fun datetime2Of(scale: Int) = SqlServerType(JavaSqlServerTypes.datetime2Of(scale))

    open fun datetimeoffsetOf(scale: Int) = SqlServerType(JavaSqlServerTypes.datetimeoffsetOf(scale))

    // JSON-encoded row types

    /** A JSON column type that stores a single row as a positional JSON array: [val1, val2, val3]. */
    open fun <Row : Any> jsonArrayEncoded(parser: RowCodec<Row>) =
        SqlServerType<Row>(JavaSqlServerTypes.jsonArrayEncoded(parser.underlying))

    /** A JSON column type that stores a list of rows, each as a positional JSON array. */
    open fun <Row : Any> jsonArrayEncodedList(parser: RowCodec<Row>) =
        SqlServerType<List<Row>>(JavaSqlServerTypes.jsonArrayEncodedList(parser.underlying))

    /** A JSON column type that stores a single row as a keyed JSON object: {"col": val, ...}. */
    open fun <Row : Any> jsonObjectEncoded(parser: RowCodecNamed<Row>) =
        SqlServerType<Row>(JavaSqlServerTypes.jsonObjectEncoded(parser.underlying))

    /** A JSON column type that stores a list of rows, each as a keyed JSON object. */
    open fun <Row : Any> jsonObjectEncodedList(parser: RowCodecNamed<Row>) =
        SqlServerType<List<Row>>(JavaSqlServerTypes.jsonObjectEncodedList(parser.underlying))

    companion object : SqlServerTypes()
}
