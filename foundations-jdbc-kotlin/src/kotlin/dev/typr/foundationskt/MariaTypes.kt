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

    /** Alias for [int_] — aesthetic, avoids the Java-keyword `_` suffix. */
    open val integer: MariaType<Int> = int_

    open val bigint: MariaType<Long> = MariaType(JavaMariaTypes.bigint.transform(SqlFunction { it }, { it }))
    open val float_: MariaType<Float> = MariaType(JavaMariaTypes.float_.transform(SqlFunction { it }, { it }))

    /** Alias for [float_] — aesthetic cross-palette naming. 4B IEEE 754. */
    open val float4: MariaType<Float> = float_

    open val double_: MariaType<Double> = MariaType(JavaMariaTypes.double_.transform(SqlFunction { it }, { it }))

    /** Alias for [double_] — aesthetic, avoids the Java-keyword `_` suffix. 8B IEEE 754. */
    open val float8: MariaType<Double> = double_
    open val bool: MariaType<Boolean> = MariaType(JavaMariaTypes.bool.transform(SqlFunction { it }, { it }))
    open val bit1: MariaType<Boolean> = MariaType(JavaMariaTypes.bit1.transform(SqlFunction { it }, { it }))

    // Forward all other types directly from Java (including BigDecimal which stays as java.math.BigDecimal in Kotlin)
    open val decimal: MariaType<java.math.BigDecimal> = MariaType(JavaMariaTypes.decimal)
    open val numeric: MariaType<java.math.BigDecimal> = MariaType(JavaMariaTypes.numeric)
    open val tinyintUnsigned: MariaType<dev.typr.foundations.data.Uint1> = MariaType(JavaMariaTypes.tinyintUnsigned)
    open val smallintUnsigned: MariaType<dev.typr.foundations.data.Uint2> = MariaType(JavaMariaTypes.smallintUnsigned)
    open val mediumintUnsigned: MariaType<dev.typr.foundations.data.Uint4> = MariaType(JavaMariaTypes.mediumintUnsigned)
    open val intUnsigned: MariaType<dev.typr.foundations.data.Uint4> = MariaType(JavaMariaTypes.intUnsigned)
    open val bigintUnsigned: MariaType<dev.typr.foundations.data.Uint8> = MariaType(JavaMariaTypes.bigintUnsigned)
    open val bit: MariaType<ByteArray> = MariaType(JavaMariaTypes.bit)
    open val char_: MariaType<String> = MariaType(JavaMariaTypes.char_)

    /** Alias for [char_] — aesthetic, avoids the Java-keyword `_` suffix. */
    open val character: MariaType<String> = char_
    open val varchar: MariaType<String> = MariaType(JavaMariaTypes.varchar)
    open val tinytext: MariaType<String> = MariaType(JavaMariaTypes.tinytext)
    open val text: MariaType<String> = MariaType(JavaMariaTypes.text)
    open val mediumtext: MariaType<String> = MariaType(JavaMariaTypes.mediumtext)
    open val longtext: MariaType<String> = MariaType(JavaMariaTypes.longtext)
    open val binary: MariaType<ByteArray> = MariaType(JavaMariaTypes.binary)
    open val varbinary: MariaType<ByteArray> = MariaType(JavaMariaTypes.varbinary)
    open val tinyblob: MariaType<ByteArray> = MariaType(JavaMariaTypes.tinyblob)
    open val blob: MariaType<ByteArray> = MariaType(JavaMariaTypes.blob)
    open val mediumblob: MariaType<ByteArray> = MariaType(JavaMariaTypes.mediumblob)
    open val longblob: MariaType<ByteArray> = MariaType(JavaMariaTypes.longblob)
    open val date: MariaType<java.time.LocalDate> = MariaType(JavaMariaTypes.date)
    open val time: MariaType<java.time.LocalTime> = MariaType(JavaMariaTypes.time)
    open val datetime: MariaType<java.time.LocalDateTime> = MariaType(JavaMariaTypes.datetime)
    open val timestamp: MariaType<java.time.LocalDateTime> = MariaType(JavaMariaTypes.timestamp)
    open val year: MariaType<java.time.Year> = MariaType(JavaMariaTypes.year)
    open val set: MariaType<dev.typr.foundations.data.maria.MariaSet> = MariaType(JavaMariaTypes.set)
    open val json: MariaType<dev.typr.foundations.data.Json> = MariaType(JavaMariaTypes.json)
    open val inet4: MariaType<dev.typr.foundations.data.maria.Inet4> = MariaType(JavaMariaTypes.inet4)
    open val inet6: MariaType<dev.typr.foundations.data.maria.Inet6> = MariaType(JavaMariaTypes.inet6)
    open val uuid: MariaType<java.util.UUID> = MariaType(JavaMariaTypes.uuid)
    open val geometry: MariaType<org.mariadb.jdbc.type.Geometry> = MariaType(JavaMariaTypes.geometry)
    open val point: MariaType<org.mariadb.jdbc.type.Point> = MariaType(JavaMariaTypes.point)
    open val linestring: MariaType<org.mariadb.jdbc.type.LineString> = MariaType(JavaMariaTypes.linestring)
    open val polygon: MariaType<org.mariadb.jdbc.type.Polygon> = MariaType(JavaMariaTypes.polygon)
    open val multipoint: MariaType<org.mariadb.jdbc.type.MultiPoint> = MariaType(JavaMariaTypes.multipoint)
    open val multilinestring: MariaType<org.mariadb.jdbc.type.MultiLineString> = MariaType(JavaMariaTypes.multilinestring)
    open val multipolygon: MariaType<org.mariadb.jdbc.type.MultiPolygon> = MariaType(JavaMariaTypes.multipolygon)
    open val geometrycollection: MariaType<org.mariadb.jdbc.type.GeometryCollection> = MariaType(JavaMariaTypes.geometrycollection)
    open val unknown: MariaType<dev.typr.foundations.data.Unknown> = MariaType(JavaMariaTypes.unknown)

    // Parameterized methods
    open fun decimalOf(precision: Int, scale: Int): MariaType<java.math.BigDecimal> = MariaType(JavaMariaTypes.decimalOf(precision, scale))

    open fun char_Of(length: Int): MariaType<String> = MariaType(JavaMariaTypes.char_Of(length))

    open fun varcharOf(length: Int): MariaType<String> = MariaType(JavaMariaTypes.varcharOf(length))

    open fun binaryOf(length: Int): MariaType<ByteArray> = MariaType(JavaMariaTypes.binaryOf(length))

    open fun varbinaryOf(length: Int): MariaType<ByteArray> = MariaType(JavaMariaTypes.varbinaryOf(length))

    open fun timeOf(fsp: Int): MariaType<java.time.LocalTime> = MariaType(JavaMariaTypes.timeOf(fsp))

    open fun datetimeOf(fsp: Int): MariaType<java.time.LocalDateTime> = MariaType(JavaMariaTypes.datetimeOf(fsp))

    open fun timestampOf(fsp: Int): MariaType<java.time.LocalDateTime> = MariaType(JavaMariaTypes.timestampOf(fsp))

    open fun vector(dimension: Int): MariaType<dev.typr.foundations.data.Vector> = MariaType(JavaMariaTypes.vector(dimension))

    /**
     * Create a MariaType for an ENUM column, deriving the SQL literal from the enum class.
     *
     * Call-site: `MariaTypes.ofEnum<OrderState>()` — no string argument, no function reference.
     * The column DDL must match the derived literal (`ENUM('PENDING','SHIPPED',…)` using each
     * enum constant's `name`). Use [ofEnum(sqlType, fromString)] when the database labels
     * differ from the Java enum's `name()` values.
     */
    open fun <E : Enum<E>> ofEnum(values: Array<E>): MariaType<E> =
        MariaType(JavaMariaTypes.ofEnum(values))

    open fun <E> ofEnum(values: Array<E>, name: java.util.function.Function<E, String>): MariaType<E> =
        MariaType(JavaMariaTypes.ofEnum(values, name))

    open fun <E : Enum<E>> ofEnum(sqlType: String, fromString: java.util.function.Function<String, E>): MariaType<E> =
        MariaType(JavaMariaTypes.ofEnum(sqlType, fromString))

    // JSON-encoded row types

    /** A JSON column type that stores a single row as a positional JSON array: [val1, val2, val3]. */
    open fun <Row : Any> jsonArrayEncoded(parser: RowCodec<Row>): MariaType<Row> =
        MariaType(JavaMariaTypes.jsonArrayEncoded(parser.underlying))

    /** A JSON column type that stores a list of rows, each as a positional JSON array. */
    open fun <Row : Any> jsonArrayEncodedList(parser: RowCodec<Row>): MariaType<List<Row>> =
        MariaType(JavaMariaTypes.jsonArrayEncodedList(parser.underlying))

    /** A JSON column type that stores a single row as a keyed JSON object: {"col": val, ...}. */
    open fun <Row : Any> jsonObjectEncoded(parser: RowCodecNamed<Row>): MariaType<Row> =
        MariaType(JavaMariaTypes.jsonObjectEncoded(parser.underlying))

    /** A JSON column type that stores a list of rows, each as a keyed JSON object. */
    open fun <Row : Any> jsonObjectEncodedList(parser: RowCodecNamed<Row>): MariaType<List<Row>> =
        MariaType(JavaMariaTypes.jsonObjectEncodedList(parser.underlying))

    companion object : MariaTypes()
}

/**
 * Reified inline variant of [MariaTypes.ofEnum]. Lets you write
 * `MariaTypes.ofEnum<OrderState>()` with no class-literal argument. Lives at the top-level as
 * an extension on the companion because [MariaTypes] is an `open class` and open methods
 * can't be `inline reified`.
 */
inline fun <reified E : Enum<E>> MariaTypes.Companion.ofEnum(): MariaType<E> =
    ofEnum(enumValues<E>())
