package dev.typr.foundationskt

import dev.typr.foundations.SqlFunction
import dev.typr.foundations.DuckDbTypes as JavaDuckDbTypes

/**
 * Kotlin-friendly DuckDbType instances that use Kotlin types instead of Java boxed types.
 * All types from dev.typr.foundations.DuckDbTypes are available here, with primitives converted to Kotlin types.
 *
 * Extend this class to add your own custom types to a shared set of type definitions.
 */
open class DuckDbTypes {
    // Primitives - convert Java boxed types to Kotlin native types
    open val tinyint: DuckDbType<Byte> = DuckDbType(JavaDuckDbTypes.tinyint.transform({ it }, { it }))
    open val smallint: DuckDbType<Short> = DuckDbType(JavaDuckDbTypes.smallint.transform({ it }, { it }))
    open val integer: DuckDbType<Int> = DuckDbType(JavaDuckDbTypes.integer.transform({ it }, { it }))
    open val bigint: DuckDbType<Long> = DuckDbType(JavaDuckDbTypes.bigint.transform({ it }, { it }))
    open val float_: DuckDbType<Float> = DuckDbType(JavaDuckDbTypes.float_.transform({ it }, { it }))
    open val double_: DuckDbType<Double> = DuckDbType(JavaDuckDbTypes.double_.transform({ it }, { it }))
    open val boolean_: DuckDbType<Boolean> = DuckDbType(JavaDuckDbTypes.boolean_.transform({ it }, { it }))

    /** Alias for [boolean_] — aesthetic, avoids the Java-keyword `_` suffix. */
    open val bool: DuckDbType<Boolean> = boolean_


    // Forward all other types directly from Java
    open val decimal: DuckDbType<java.math.BigDecimal> = DuckDbType(JavaDuckDbTypes.decimal)
    open val numeric: DuckDbType<java.math.BigDecimal> = DuckDbType(JavaDuckDbTypes.numeric)
    open val hugeint: DuckDbType<java.math.BigInteger> = DuckDbType(JavaDuckDbTypes.hugeint)
    open val utinyint: DuckDbType<dev.typr.foundations.data.Uint1> = DuckDbType(JavaDuckDbTypes.utinyint)
    open val usmallint: DuckDbType<dev.typr.foundations.data.Uint2> = DuckDbType(JavaDuckDbTypes.usmallint)
    open val uinteger: DuckDbType<dev.typr.foundations.data.Uint4> = DuckDbType(JavaDuckDbTypes.uinteger)
    open val ubigint: DuckDbType<dev.typr.foundations.data.Uint8> = DuckDbType(JavaDuckDbTypes.ubigint)
    open val uhugeint: DuckDbType<java.math.BigInteger> = DuckDbType(JavaDuckDbTypes.uhugeint)
    open val real: DuckDbType<Float> = DuckDbType(JavaDuckDbTypes.real)
    open val float4: DuckDbType<Float> = DuckDbType(JavaDuckDbTypes.float4)
    open val float8: DuckDbType<Double> = DuckDbType(JavaDuckDbTypes.float8)
    open val varchar: DuckDbType<String> = DuckDbType(JavaDuckDbTypes.varchar)
    open val text: DuckDbType<String> = DuckDbType(JavaDuckDbTypes.text)
    open val string: DuckDbType<String> = DuckDbType(JavaDuckDbTypes.string)
    open val char_: DuckDbType<String> = DuckDbType(JavaDuckDbTypes.char_)

    /** Alias for [char_] — aesthetic, avoids the Java-keyword `_` suffix. */
    open val character: DuckDbType<String> = char_
    open val bpchar: DuckDbType<String> = DuckDbType(JavaDuckDbTypes.bpchar)
    open val blob: DuckDbType<ByteArray> = DuckDbType(JavaDuckDbTypes.blob)
    open val bytea: DuckDbType<ByteArray> = DuckDbType(JavaDuckDbTypes.bytea)
    open val binary: DuckDbType<ByteArray> = DuckDbType(JavaDuckDbTypes.binary)
    open val varbinary: DuckDbType<ByteArray> = DuckDbType(JavaDuckDbTypes.varbinary)
    open val bit: DuckDbType<String> = DuckDbType(JavaDuckDbTypes.bit)
    open val bitstring: DuckDbType<String> = DuckDbType(JavaDuckDbTypes.bitstring)
    open val date: DuckDbType<java.time.LocalDate> = DuckDbType(JavaDuckDbTypes.date)
    open val time: DuckDbType<java.time.LocalTime> = DuckDbType(JavaDuckDbTypes.time)
    open val timestamp: DuckDbType<java.time.LocalDateTime> = DuckDbType(JavaDuckDbTypes.timestamp)
    open val datetime: DuckDbType<java.time.LocalDateTime> = DuckDbType(JavaDuckDbTypes.datetime)
    open val timestamptz: DuckDbType<java.time.Instant> = DuckDbType(JavaDuckDbTypes.timestamptz)
    open val timetz: DuckDbType<java.time.OffsetTime> = DuckDbType(JavaDuckDbTypes.timetz)
    open val timestamp_s: DuckDbType<java.time.LocalDateTime> = DuckDbType(JavaDuckDbTypes.timestamp_s)
    open val timestamp_ms: DuckDbType<java.time.LocalDateTime> = DuckDbType(JavaDuckDbTypes.timestamp_ms)
    open val timestamp_ns: DuckDbType<java.time.LocalDateTime> = DuckDbType(JavaDuckDbTypes.timestamp_ns)
    open val interval: DuckDbType<java.time.Duration> = DuckDbType(JavaDuckDbTypes.interval)
    open val uuid: DuckDbType<java.util.UUID> = DuckDbType(JavaDuckDbTypes.uuid)
    open val json: DuckDbType<dev.typr.foundations.data.Json> = DuckDbType(JavaDuckDbTypes.json)
    // Pre-defined array-of-T values removed; users call `.list()` or `.array(size)` on the scalar.
    open val unknown: DuckDbType<dev.typr.foundations.data.Unknown> = DuckDbType(JavaDuckDbTypes.unknown)

    // Parameterized methods
    open fun decimalOf(precision: Int, scale: Int): DuckDbType<java.math.BigDecimal> = DuckDbType(JavaDuckDbTypes.decimalOf(precision, scale))

    open fun varcharOf(length: Int): DuckDbType<String> = DuckDbType(JavaDuckDbTypes.varcharOf(length))

    open fun char_Of(length: Int): DuckDbType<String> = DuckDbType(JavaDuckDbTypes.char_Of(length))

    open fun bitOf(length: Int): DuckDbType<String> = DuckDbType(JavaDuckDbTypes.bitOf(length))

    open fun <E : Enum<E>> ofEnum(enumTypeName: String, fromString: java.util.function.Function<String, E>): DuckDbType<E> =
        DuckDbType(JavaDuckDbTypes.ofEnum(enumTypeName, fromString))

    open fun <E : Enum<E>> ofEnum(enumTypeName: String, values: Array<E>): DuckDbType<E> =
        DuckDbType(JavaDuckDbTypes.ofEnum(enumTypeName, values))

    open fun <E> ofEnum(enumTypeName: String, values: Array<E>, name: java.util.function.Function<E, String>): DuckDbType<E> =
        DuckDbType(JavaDuckDbTypes.ofEnum(enumTypeName, values, name))

    // Composite (STRUCT) types

    open fun <Row : Any> compositeOf(structName: String, codec: RowCodecNamed<Row>): DuckDbType<Row> =
        DuckDbType(JavaDuckDbTypes.compositeOf(structName, codec.underlying))

    // JSON-encoded row types

    /** A JSON column type that stores a single row as a positional JSON array: [val1, val2, val3]. */
    open fun <Row : Any> jsonArrayEncoded(parser: RowCodec<Row>): DuckDbType<Row> =
        DuckDbType(JavaDuckDbTypes.jsonArrayEncoded(parser.underlying))

    /** A JSON column type that stores a list of rows, each as a positional JSON array. */
    open fun <Row : Any> jsonArrayEncodedList(parser: RowCodec<Row>): DuckDbType<List<Row>> =
        DuckDbType(JavaDuckDbTypes.jsonArrayEncodedList(parser.underlying))

    /** A JSON column type that stores a single row as a keyed JSON object: {"col": val, ...}. */
    open fun <Row : Any> jsonObjectEncoded(parser: RowCodecNamed<Row>): DuckDbType<Row> =
        DuckDbType(JavaDuckDbTypes.jsonObjectEncoded(parser.underlying))

    /** A JSON column type that stores a list of rows, each as a keyed JSON object. */
    open fun <Row : Any> jsonObjectEncodedList(parser: RowCodecNamed<Row>): DuckDbType<List<Row>> =
        DuckDbType(JavaDuckDbTypes.jsonObjectEncodedList(parser.underlying))

    /** JSON codec for Map<K, V> that serializes as a JSON object. */
    open fun <K, V> mapJson(
        keyJson: dev.typr.foundations.DuckDbJson<K>,
        valueJson: dev.typr.foundations.DuckDbJson<V>
    ): dev.typr.foundations.DuckDbJson<Map<K, V>> =
        JavaDuckDbTypes.mapJson(keyJson, valueJson).transform(
            { jmap -> jmap.toMap() },
            { kmap -> kmap.toMap(java.util.HashMap()) }
        )

    companion object : DuckDbTypes()
}

inline fun <reified E : Enum<E>> DuckDbTypes.Companion.ofEnum(enumTypeName: String): DuckDbType<E> =
    ofEnum(enumTypeName, enumValues<E>())
