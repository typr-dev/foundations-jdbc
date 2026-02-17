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
    open val bigint: SqlServerType<Long> = SqlServerType(JavaSqlServerTypes.bigint.transform(SqlFunction { it }, { it }))
    open val real: SqlServerType<Float> = SqlServerType(JavaSqlServerTypes.real.transform(SqlFunction { it }, { it }))
    open val float_: SqlServerType<Double> = SqlServerType(JavaSqlServerTypes.float_.transform(SqlFunction { it }, { it }))
    open val bit: SqlServerType<Boolean> = SqlServerType(JavaSqlServerTypes.bit.transform(SqlFunction { it }, { it }))

    // Forward all other types directly from Java
    open val tinyint = SqlServerType(JavaSqlServerTypes.tinyint)
    open val decimal = SqlServerType(JavaSqlServerTypes.decimal)
    open val numeric = SqlServerType(JavaSqlServerTypes.numeric)
    open val money = SqlServerType(JavaSqlServerTypes.money)
    open val smallmoney = SqlServerType(JavaSqlServerTypes.smallmoney)
    open val char_ = SqlServerType(JavaSqlServerTypes.char_)
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
    open fun decimal(precision: Int, scale: Int) = SqlServerType(JavaSqlServerTypes.decimal(precision, scale))

    open fun numeric(precision: Int, scale: Int) = SqlServerType(JavaSqlServerTypes.numeric(precision, scale))

    open fun char_(length: Int) = SqlServerType(JavaSqlServerTypes.char_(length))

    open fun varchar(length: Int) = SqlServerType(JavaSqlServerTypes.varchar(length))

    open fun nchar(length: Int) = SqlServerType(JavaSqlServerTypes.nchar(length))

    open fun nvarchar(length: Int) = SqlServerType(JavaSqlServerTypes.nvarchar(length))

    open fun binary(length: Int) = SqlServerType(JavaSqlServerTypes.binary(length))

    open fun varbinary(length: Int) = SqlServerType(JavaSqlServerTypes.varbinary(length))

    open fun time(scale: Int) = SqlServerType(JavaSqlServerTypes.time(scale))

    open fun datetime2(scale: Int) = SqlServerType(JavaSqlServerTypes.datetime2(scale))

    open fun datetimeoffset(scale: Int) = SqlServerType(JavaSqlServerTypes.datetimeoffset(scale))

    // JSON-encoded row types

    /** A JSON column type that stores a single row as a positional JSON array: [val1, val2, val3]. */
    open fun <Row : Any> jsonArrayEncoded(parser: RowParser<Row>) =
        SqlServerType<Row>(JavaSqlServerTypes.jsonArrayEncoded(parser.underlying))

    /** A JSON column type that stores a list of rows, each as a positional JSON array. */
    open fun <Row : Any> jsonArrayEncodedList(parser: RowParser<Row>) =
        SqlServerType<List<Row>>(JavaSqlServerTypes.jsonArrayEncodedList(parser.underlying))

    /** A JSON column type that stores a single row as a keyed JSON object: {"col": val, ...}. */
    open fun <Row : Any> jsonObjectEncoded(parser: RowParserNamed<Row>) =
        SqlServerType<Row>(JavaSqlServerTypes.jsonObjectEncoded(parser.underlying))

    /** A JSON column type that stores a list of rows, each as a keyed JSON object. */
    open fun <Row : Any> jsonObjectEncodedList(parser: RowParserNamed<Row>) =
        SqlServerType<List<Row>>(JavaSqlServerTypes.jsonObjectEncodedList(parser.underlying))

    companion object : SqlServerTypes()
}
