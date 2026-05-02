package dev.typr.foundationskt

import dev.typr.foundations.SqliteTypes as JavaSqliteTypes
import dev.typr.foundationskt.data.Json
import dev.typr.foundationskt.data.Unknown

/**
 * Kotlin-friendly SqliteType instances that use Kotlin types instead of Java boxed types.
 * All types from dev.typr.foundations.SqliteTypes are available here, with primitives converted
 * to Kotlin native types.
 *
 * Extend this class to add your own custom types to a shared set of type definitions.
 */
open class SqliteTypes {
    // INTEGER affinity — convert Java boxed integers to Kotlin native types
    open val integer: SqliteType<Long> = SqliteType(JavaSqliteTypes.integer.transform({ it }, { it }))
    open val bigint: SqliteType<Long> = SqliteType(JavaSqliteTypes.bigint.transform({ it }, { it }))
    open val int_: SqliteType<Int> = SqliteType(JavaSqliteTypes.int_.transform({ it }, { it }))
    open val smallint: SqliteType<Short> = SqliteType(JavaSqliteTypes.smallint.transform({ it }, { it }))
    open val tinyint: SqliteType<Byte> = SqliteType(JavaSqliteTypes.tinyint.transform({ it }, { it }))
    open val boolean_: SqliteType<Boolean> = SqliteType(JavaSqliteTypes.boolean_.transform({ it }, { it }))

    /** Alias for [boolean_] — aesthetic, avoids the Java-keyword `_` suffix. */
    open val bool: SqliteType<Boolean> = boolean_

    // REAL affinity
    open val real: SqliteType<Double> = SqliteType(JavaSqliteTypes.real.transform({ it }, { it }))
    open val double_: SqliteType<Double> = SqliteType(JavaSqliteTypes.double_.transform({ it }, { it }))
    open val doublePrecision: SqliteType<Double> = SqliteType(JavaSqliteTypes.doublePrecision.transform({ it }, { it }))
    open val float_: SqliteType<Float> = SqliteType(JavaSqliteTypes.float_.transform({ it }, { it }))

    // NUMERIC affinity
    open val numeric: SqliteType<java.math.BigDecimal> = SqliteType(JavaSqliteTypes.numeric)
    open val decimal: SqliteType<java.math.BigDecimal> = SqliteType(JavaSqliteTypes.decimal)

    // TEXT affinity
    open val text: SqliteType<String> = SqliteType(JavaSqliteTypes.text)
    open val varchar: SqliteType<String> = SqliteType(JavaSqliteTypes.varchar)
    open val char_: SqliteType<String> = SqliteType(JavaSqliteTypes.char_)
    open val clob: SqliteType<String> = SqliteType(JavaSqliteTypes.clob)

    // BLOB affinity
    open val blob: SqliteType<ByteArray> = SqliteType(JavaSqliteTypes.blob)
    open val binary: SqliteType<ByteArray> = SqliteType(JavaSqliteTypes.binary)
    open val varbinary: SqliteType<ByteArray> = SqliteType(JavaSqliteTypes.varbinary)

    // Date/Time (TEXT, ISO-8601)
    open val date: SqliteType<java.time.LocalDate> = SqliteType(JavaSqliteTypes.date)
    open val time: SqliteType<java.time.LocalTime> = SqliteType(JavaSqliteTypes.time)
    open val datetime: SqliteType<java.time.LocalDateTime> = SqliteType(JavaSqliteTypes.datetime)
    open val timestamp: SqliteType<java.time.LocalDateTime> = SqliteType(JavaSqliteTypes.timestamp)
    open val instant: SqliteType<java.time.Instant> = SqliteType(JavaSqliteTypes.instant)

    // TEXT-backed convenience types
    open val uuid: SqliteType<java.util.UUID> = SqliteType(JavaSqliteTypes.uuid)
    open val json: SqliteType<Json> = SqliteType(JavaSqliteTypes.json)
    open val unknown: SqliteType<Unknown> = SqliteType(JavaSqliteTypes.unknown)

    // Parameterized
    open fun decimalOf(precision: Int, scale: Int): SqliteType<java.math.BigDecimal> =
        SqliteType(JavaSqliteTypes.decimalOf(precision, scale))

    open fun varcharOf(length: Int): SqliteType<String> = SqliteType(JavaSqliteTypes.varcharOf(length))

    open fun charOf(length: Int): SqliteType<String> = SqliteType(JavaSqliteTypes.charOf(length))

    open fun <E : Enum<E>> ofEnum(fromString: java.util.function.Function<String, E>): SqliteType<E> =
        SqliteType(JavaSqliteTypes.ofEnum(fromString))

    open fun <E : Enum<E>> ofEnum(values: Array<E>): SqliteType<E> =
        SqliteType(JavaSqliteTypes.ofEnum(values))

    open fun <E> ofEnum(values: Array<E>, name: java.util.function.Function<E, String>): SqliteType<E> =
        SqliteType(JavaSqliteTypes.ofEnum(values, name))

    companion object : SqliteTypes()
}

inline fun <reified E : Enum<E>> SqliteTypes.Companion.ofEnum(): SqliteType<E> =
    ofEnum(enumValues<E>())
