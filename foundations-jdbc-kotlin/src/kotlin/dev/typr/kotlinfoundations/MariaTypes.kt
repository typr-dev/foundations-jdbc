package dev.typr.kotlinfoundations

import dev.typr.foundations.MariaType
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
    open val tinyint: MariaType<Byte> = JavaMariaTypes.tinyint.bimap(SqlFunction { it }, { it })
    open val smallint: MariaType<Short> = JavaMariaTypes.smallint.bimap(SqlFunction { it }, { it })
    open val mediumint: MariaType<Int> = JavaMariaTypes.mediumint.bimap(SqlFunction { it }, { it })
    open val int_: MariaType<Int> = JavaMariaTypes.int_.bimap(SqlFunction { it }, { it })
    open val bigint: MariaType<Long> = JavaMariaTypes.bigint.bimap(SqlFunction { it }, { it })
    open val float_: MariaType<Float> = JavaMariaTypes.float_.bimap(SqlFunction { it }, { it })
    open val double_: MariaType<Double> = JavaMariaTypes.double_.bimap(SqlFunction { it }, { it })
    open val bool: MariaType<Boolean> = JavaMariaTypes.bool.bimap(SqlFunction { it }, { it })
    open val bit1: MariaType<Boolean> = JavaMariaTypes.bit1.bimap(SqlFunction { it }, { it })

    // Forward all other types directly from Java (including BigDecimal which stays as java.math.BigDecimal in Kotlin)
    open val decimal = JavaMariaTypes.decimal
    open val numeric = JavaMariaTypes.numeric
    open val tinyintUnsigned = JavaMariaTypes.tinyintUnsigned
    open val smallintUnsigned = JavaMariaTypes.smallintUnsigned
    open val mediumintUnsigned = JavaMariaTypes.mediumintUnsigned
    open val intUnsigned = JavaMariaTypes.intUnsigned
    open val bigintUnsigned = JavaMariaTypes.bigintUnsigned
    open val bit = JavaMariaTypes.bit
    open val char_ = JavaMariaTypes.char_
    open val varchar = JavaMariaTypes.varchar
    open val tinytext = JavaMariaTypes.tinytext
    open val text = JavaMariaTypes.text
    open val mediumtext = JavaMariaTypes.mediumtext
    open val longtext = JavaMariaTypes.longtext
    open val binary = JavaMariaTypes.binary
    open val varbinary = JavaMariaTypes.varbinary
    open val tinyblob = JavaMariaTypes.tinyblob
    open val blob = JavaMariaTypes.blob
    open val mediumblob = JavaMariaTypes.mediumblob
    open val longblob = JavaMariaTypes.longblob
    open val date = JavaMariaTypes.date
    open val time = JavaMariaTypes.time
    open val datetime = JavaMariaTypes.datetime
    open val timestamp = JavaMariaTypes.timestamp
    open val year = JavaMariaTypes.year
    open val set = JavaMariaTypes.set
    open val json = JavaMariaTypes.json
    open val inet4 = JavaMariaTypes.inet4
    open val inet6 = JavaMariaTypes.inet6
    open val geometry = JavaMariaTypes.geometry
    open val point = JavaMariaTypes.point
    open val linestring = JavaMariaTypes.linestring
    open val polygon = JavaMariaTypes.polygon
    open val multipoint = JavaMariaTypes.multipoint
    open val multilinestring = JavaMariaTypes.multilinestring
    open val multipolygon = JavaMariaTypes.multipolygon
    open val geometrycollection = JavaMariaTypes.geometrycollection
    open val unknown = JavaMariaTypes.unknown

    // Parameterized methods
    open fun decimal(precision: Int, scale: Int): MariaType<java.math.BigDecimal> =
        JavaMariaTypes.decimal(precision, scale)

    open fun char_(length: Int): MariaType<String> = JavaMariaTypes.char_(length)

    open fun varchar(length: Int): MariaType<String> = JavaMariaTypes.varchar(length)

    open fun binary(length: Int): MariaType<ByteArray> = JavaMariaTypes.binary(length)

    open fun varbinary(length: Int): MariaType<ByteArray> = JavaMariaTypes.varbinary(length)

    open fun time(fsp: Int): MariaType<java.time.LocalTime> = JavaMariaTypes.time(fsp)

    open fun datetime(fsp: Int): MariaType<java.time.LocalDateTime> = JavaMariaTypes.datetime(fsp)

    open fun timestamp(fsp: Int): MariaType<java.time.LocalDateTime> = JavaMariaTypes.timestamp(fsp)

    open fun <E : Enum<E>> ofEnum(sqlType: String, fromString: java.util.function.Function<String, E>): MariaType<E> =
        JavaMariaTypes.ofEnum(sqlType, fromString)

    companion object : MariaTypes()
}
