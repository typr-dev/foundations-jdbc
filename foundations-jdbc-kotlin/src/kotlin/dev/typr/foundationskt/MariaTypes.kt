package dev.typr.foundationskt

import dev.typr.foundations.SqlFunction
import dev.typr.foundations.MariaTypes as JavaMariaTypes

/**
 * Kotlin-friendly MariaType instances that use Kotlin types instead of Java boxed types.
 * All types from dev.typr.foundations.MariaTypes are available here, with primitives converted to Kotlin types.
 *
 * Extend this class to add your own custom types to a shared set of type definitions.
 */
open class MariaTypes {
    // Primitives - convert Java boxed types to Kotlin native types
    open val tinyint: MariaType<Byte> = MariaType(JavaMariaTypes.tinyint.transform(SqlFunction { it }, { it }))
    open val smallint: MariaType<Short> = MariaType(JavaMariaTypes.smallint.transform(SqlFunction { it }, { it }))
    open val mediumint: MariaType<Int> = MariaType(JavaMariaTypes.mediumint.transform(SqlFunction { it }, { it }))
    open val int_: MariaType<Int> = MariaType(JavaMariaTypes.int_.transform(SqlFunction { it }, { it }))
    open val bigint: MariaType<Long> = MariaType(JavaMariaTypes.bigint.transform(SqlFunction { it }, { it }))
    open val float_: MariaType<Float> = MariaType(JavaMariaTypes.float_.transform(SqlFunction { it }, { it }))
    open val double_: MariaType<Double> = MariaType(JavaMariaTypes.double_.transform(SqlFunction { it }, { it }))
    open val bool: MariaType<Boolean> = MariaType(JavaMariaTypes.bool.transform(SqlFunction { it }, { it }))
    open val bit1: MariaType<Boolean> = MariaType(JavaMariaTypes.bit1.transform(SqlFunction { it }, { it }))

    // Forward all other types directly from Java (including BigDecimal which stays as java.math.BigDecimal in Kotlin)
    open val decimal = MariaType(JavaMariaTypes.decimal)
    open val numeric = MariaType(JavaMariaTypes.numeric)
    open val tinyintUnsigned = MariaType(JavaMariaTypes.tinyintUnsigned)
    open val smallintUnsigned = MariaType(JavaMariaTypes.smallintUnsigned)
    open val mediumintUnsigned = MariaType(JavaMariaTypes.mediumintUnsigned)
    open val intUnsigned = MariaType(JavaMariaTypes.intUnsigned)
    open val bigintUnsigned = MariaType(JavaMariaTypes.bigintUnsigned)
    open val bit = MariaType(JavaMariaTypes.bit)
    open val char_ = MariaType(JavaMariaTypes.char_)
    open val varchar = MariaType(JavaMariaTypes.varchar)
    open val tinytext = MariaType(JavaMariaTypes.tinytext)
    open val text = MariaType(JavaMariaTypes.text)
    open val mediumtext = MariaType(JavaMariaTypes.mediumtext)
    open val longtext = MariaType(JavaMariaTypes.longtext)
    open val binary = MariaType(JavaMariaTypes.binary)
    open val varbinary = MariaType(JavaMariaTypes.varbinary)
    open val tinyblob = MariaType(JavaMariaTypes.tinyblob)
    open val blob = MariaType(JavaMariaTypes.blob)
    open val mediumblob = MariaType(JavaMariaTypes.mediumblob)
    open val longblob = MariaType(JavaMariaTypes.longblob)
    open val date = MariaType(JavaMariaTypes.date)
    open val time = MariaType(JavaMariaTypes.time)
    open val datetime = MariaType(JavaMariaTypes.datetime)
    open val timestamp = MariaType(JavaMariaTypes.timestamp)
    open val year = MariaType(JavaMariaTypes.year)
    open val set = MariaType(JavaMariaTypes.set)
    open val json = MariaType(JavaMariaTypes.json)
    open val inet4 = MariaType(JavaMariaTypes.inet4)
    open val inet6 = MariaType(JavaMariaTypes.inet6)
    open val geometry = MariaType(JavaMariaTypes.geometry)
    open val point = MariaType(JavaMariaTypes.point)
    open val linestring = MariaType(JavaMariaTypes.linestring)
    open val polygon = MariaType(JavaMariaTypes.polygon)
    open val multipoint = MariaType(JavaMariaTypes.multipoint)
    open val multilinestring = MariaType(JavaMariaTypes.multilinestring)
    open val multipolygon = MariaType(JavaMariaTypes.multipolygon)
    open val geometrycollection = MariaType(JavaMariaTypes.geometrycollection)
    open val unknown = MariaType(JavaMariaTypes.unknown)

    // Parameterized methods
    open fun decimal(precision: Int, scale: Int) = MariaType(JavaMariaTypes.decimal(precision, scale))

    open fun char_(length: Int) = MariaType(JavaMariaTypes.char_(length))

    open fun varchar(length: Int) = MariaType(JavaMariaTypes.varchar(length))

    open fun binary(length: Int) = MariaType(JavaMariaTypes.binary(length))

    open fun varbinary(length: Int) = MariaType(JavaMariaTypes.varbinary(length))

    open fun time(fsp: Int) = MariaType(JavaMariaTypes.time(fsp))

    open fun datetime(fsp: Int) = MariaType(JavaMariaTypes.datetime(fsp))

    open fun timestamp(fsp: Int) = MariaType(JavaMariaTypes.timestamp(fsp))

    open fun <E : Enum<E>> ofEnum(sqlType: String, fromString: java.util.function.Function<String, E>) =
        MariaType(JavaMariaTypes.ofEnum(sqlType, fromString))

    // JSON-encoded row types

    /** A JSON column type that stores a single row as a positional JSON array: [val1, val2, val3]. */
    open fun <Row : Any> jsonArrayEncoded(parser: RowCodec<Row>) =
        MariaType<Row>(JavaMariaTypes.jsonArrayEncoded(parser.underlying))

    /** A JSON column type that stores a list of rows, each as a positional JSON array. */
    open fun <Row : Any> jsonArrayEncodedList(parser: RowCodec<Row>) =
        MariaType<List<Row>>(JavaMariaTypes.jsonArrayEncodedList(parser.underlying))

    /** A JSON column type that stores a single row as a keyed JSON object: {"col": val, ...}. */
    open fun <Row : Any> jsonObjectEncoded(parser: RowCodecNamed<Row>) =
        MariaType<Row>(JavaMariaTypes.jsonObjectEncoded(parser.underlying))

    /** A JSON column type that stores a list of rows, each as a keyed JSON object. */
    open fun <Row : Any> jsonObjectEncodedList(parser: RowCodecNamed<Row>) =
        MariaType<List<Row>>(JavaMariaTypes.jsonObjectEncodedList(parser.underlying))

    companion object : MariaTypes()
}
