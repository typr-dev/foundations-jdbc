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
    open val tinyint: SqlServerType<dev.typr.foundations.data.Uint1> = SqlServerType(JavaSqlServerTypes.tinyint)
    open val decimal: SqlServerType<java.math.BigDecimal> = SqlServerType(JavaSqlServerTypes.decimal)
    open val numeric: SqlServerType<java.math.BigDecimal> = SqlServerType(JavaSqlServerTypes.numeric)
    open val money: SqlServerType<java.math.BigDecimal> = SqlServerType(JavaSqlServerTypes.money)
    open val smallmoney: SqlServerType<java.math.BigDecimal> = SqlServerType(JavaSqlServerTypes.smallmoney)
    open val char_: SqlServerType<String> = SqlServerType(JavaSqlServerTypes.char_)

    /** Alias for [char_] — aesthetic, avoids the Java-keyword `_` suffix. */
    open val character: SqlServerType<String> = char_
    open val varchar: SqlServerType<String> = SqlServerType(JavaSqlServerTypes.varchar)
    open val varcharMax: SqlServerType<String> = SqlServerType(JavaSqlServerTypes.varcharMax)
    open val text: SqlServerType<String> = SqlServerType(JavaSqlServerTypes.text)
    open val nchar: SqlServerType<String> = SqlServerType(JavaSqlServerTypes.nchar)
    open val nvarchar: SqlServerType<String> = SqlServerType(JavaSqlServerTypes.nvarchar)
    open val nvarcharMax: SqlServerType<String> = SqlServerType(JavaSqlServerTypes.nvarcharMax)
    open val ntext: SqlServerType<String> = SqlServerType(JavaSqlServerTypes.ntext)
    open val binary: SqlServerType<ByteArray> = SqlServerType(JavaSqlServerTypes.binary)
    open val varbinary: SqlServerType<ByteArray> = SqlServerType(JavaSqlServerTypes.varbinary)
    open val varbinaryMax: SqlServerType<ByteArray> = SqlServerType(JavaSqlServerTypes.varbinaryMax)
    open val image: SqlServerType<ByteArray> = SqlServerType(JavaSqlServerTypes.image)
    open val date: SqlServerType<java.time.LocalDate> = SqlServerType(JavaSqlServerTypes.date)
    open val time: SqlServerType<java.time.LocalTime> = SqlServerType(JavaSqlServerTypes.time)
    open val datetime: SqlServerType<java.time.LocalDateTime> = SqlServerType(JavaSqlServerTypes.datetime)
    open val smalldatetime: SqlServerType<java.time.LocalDateTime> = SqlServerType(JavaSqlServerTypes.smalldatetime)
    open val datetime2: SqlServerType<java.time.LocalDateTime> = SqlServerType(JavaSqlServerTypes.datetime2)
    open val datetimeoffset: SqlServerType<java.time.OffsetDateTime> = SqlServerType(JavaSqlServerTypes.datetimeoffset)
    open val uniqueidentifier: SqlServerType<java.util.UUID> = SqlServerType(JavaSqlServerTypes.uniqueidentifier)
    open val xml: SqlServerType<dev.typr.foundations.data.Xml> = SqlServerType(JavaSqlServerTypes.xml)
    open val json: SqlServerType<dev.typr.foundations.data.Json> = SqlServerType(JavaSqlServerTypes.json)
    open val vector: SqlServerType<ByteArray> = SqlServerType(JavaSqlServerTypes.vector)
    open val rowversion: SqlServerType<ByteArray> = SqlServerType(JavaSqlServerTypes.rowversion)
    open val timestamp: SqlServerType<ByteArray> = SqlServerType(JavaSqlServerTypes.timestamp)
    open val hierarchyid: SqlServerType<dev.typr.foundations.data.HierarchyId> = SqlServerType(JavaSqlServerTypes.hierarchyid)
    open val sqlVariant: SqlServerType<Any> = SqlServerType(JavaSqlServerTypes.sqlVariant)
    open val geography: SqlServerType<com.microsoft.sqlserver.jdbc.Geography> = SqlServerType(JavaSqlServerTypes.geography)
    open val geometry: SqlServerType<com.microsoft.sqlserver.jdbc.Geometry> = SqlServerType(JavaSqlServerTypes.geometry)
    open val unknown: SqlServerType<dev.typr.foundations.data.Unknown> = SqlServerType(JavaSqlServerTypes.unknown)

    // Parameterized methods
    open fun decimalOf(precision: Int, scale: Int): SqlServerType<java.math.BigDecimal> = SqlServerType(JavaSqlServerTypes.decimalOf(precision, scale))

    open fun numericOf(precision: Int, scale: Int): SqlServerType<java.math.BigDecimal> = SqlServerType(JavaSqlServerTypes.numericOf(precision, scale))

    open fun char_Of(length: Int): SqlServerType<String> = SqlServerType(JavaSqlServerTypes.char_Of(length))

    open fun varcharOf(length: Int): SqlServerType<String> = SqlServerType(JavaSqlServerTypes.varcharOf(length))

    open fun ncharOf(length: Int): SqlServerType<String> = SqlServerType(JavaSqlServerTypes.ncharOf(length))

    open fun nvarcharOf(length: Int): SqlServerType<String> = SqlServerType(JavaSqlServerTypes.nvarcharOf(length))

    open fun binaryOf(length: Int): SqlServerType<ByteArray> = SqlServerType(JavaSqlServerTypes.binaryOf(length))

    open fun varbinaryOf(length: Int): SqlServerType<ByteArray> = SqlServerType(JavaSqlServerTypes.varbinaryOf(length))

    open fun timeOf(scale: Int): SqlServerType<java.time.LocalTime> = SqlServerType(JavaSqlServerTypes.timeOf(scale))

    open fun datetime2Of(scale: Int): SqlServerType<java.time.LocalDateTime> = SqlServerType(JavaSqlServerTypes.datetime2Of(scale))

    open fun datetimeoffsetOf(scale: Int): SqlServerType<java.time.OffsetDateTime> = SqlServerType(JavaSqlServerTypes.datetimeoffsetOf(scale))

    // JSON-encoded row types

    /** A JSON column type that stores a single row as a positional JSON array: [val1, val2, val3]. */
    open fun <Row : Any> jsonArrayEncoded(parser: RowCodec<Row>): SqlServerType<Row> =
        SqlServerType(JavaSqlServerTypes.jsonArrayEncoded(parser.underlying))

    /** A JSON column type that stores a list of rows, each as a positional JSON array. */
    open fun <Row : Any> jsonArrayEncodedList(parser: RowCodec<Row>): SqlServerType<List<Row>> =
        SqlServerType(JavaSqlServerTypes.jsonArrayEncodedList(parser.underlying))

    /** A JSON column type that stores a single row as a keyed JSON object: {"col": val, ...}. */
    open fun <Row : Any> jsonObjectEncoded(parser: RowCodecNamed<Row>): SqlServerType<Row> =
        SqlServerType(JavaSqlServerTypes.jsonObjectEncoded(parser.underlying))

    /** A JSON column type that stores a list of rows, each as a keyed JSON object. */
    open fun <Row : Any> jsonObjectEncodedList(parser: RowCodecNamed<Row>): SqlServerType<List<Row>> =
        SqlServerType(JavaSqlServerTypes.jsonObjectEncodedList(parser.underlying))

    open fun <E, U> ofEnum(underlying: SqlServerType<U>, values: Array<E>, toUnderlying: java.util.function.Function<E, U>): SqlServerType<E> =
        SqlServerType(JavaSqlServerTypes.ofEnum(underlying.underlying, values, toUnderlying))

    open fun <E : Enum<E>> ofEnum(values: Array<E>): SqlServerType<E> =
        SqlServerType(JavaSqlServerTypes.ofEnum(values))

    companion object : SqlServerTypes()
}

inline fun <reified E : Enum<E>> SqlServerTypes.Companion.ofEnum(): SqlServerType<E> =
    ofEnum(enumValues<E>())
