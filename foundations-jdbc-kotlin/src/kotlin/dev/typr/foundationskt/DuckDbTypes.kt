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
    open val tinyint: DuckDbType<Byte> = DuckDbType(JavaDuckDbTypes.tinyint.transform(SqlFunction { it }, { it }))
    open val smallint: DuckDbType<Short> = DuckDbType(JavaDuckDbTypes.smallint.transform(SqlFunction { it }, { it }))
    open val integer: DuckDbType<Int> = DuckDbType(JavaDuckDbTypes.integer.transform(SqlFunction { it }, { it }))
    open val bigint: DuckDbType<Long> = DuckDbType(JavaDuckDbTypes.bigint.transform(SqlFunction { it }, { it }))
    open val float_: DuckDbType<Float> = DuckDbType(JavaDuckDbTypes.float_.transform(SqlFunction { it }, { it }))
    open val double_: DuckDbType<Double> = DuckDbType(JavaDuckDbTypes.double_.transform(SqlFunction { it }, { it }))
    open val boolean_: DuckDbType<Boolean> = DuckDbType(JavaDuckDbTypes.boolean_.transform(SqlFunction { it }, { it }))


    // Forward all other types directly from Java
    open val decimal = DuckDbType(JavaDuckDbTypes.decimal)
    open val numeric = DuckDbType(JavaDuckDbTypes.numeric)
    open val hugeint = DuckDbType(JavaDuckDbTypes.hugeint)
    open val utinyint = DuckDbType(JavaDuckDbTypes.utinyint)
    open val usmallint = DuckDbType(JavaDuckDbTypes.usmallint)
    open val uinteger = DuckDbType(JavaDuckDbTypes.uinteger)
    open val ubigint = DuckDbType(JavaDuckDbTypes.ubigint)
    open val uhugeint = DuckDbType(JavaDuckDbTypes.uhugeint)
    open val real = DuckDbType(JavaDuckDbTypes.real)
    open val float4 = DuckDbType(JavaDuckDbTypes.float4)
    open val float8 = DuckDbType(JavaDuckDbTypes.float8)
    open val varchar = DuckDbType(JavaDuckDbTypes.varchar)
    open val text = DuckDbType(JavaDuckDbTypes.text)
    open val string = DuckDbType(JavaDuckDbTypes.string)
    open val char_ = DuckDbType(JavaDuckDbTypes.char_)
    open val bpchar = DuckDbType(JavaDuckDbTypes.bpchar)
    open val blob = DuckDbType(JavaDuckDbTypes.blob)
    open val bytea = DuckDbType(JavaDuckDbTypes.bytea)
    open val binary = DuckDbType(JavaDuckDbTypes.binary)
    open val varbinary = DuckDbType(JavaDuckDbTypes.varbinary)
    open val bit = DuckDbType(JavaDuckDbTypes.bit)
    open val bitstring = DuckDbType(JavaDuckDbTypes.bitstring)
    open val date = DuckDbType(JavaDuckDbTypes.date)
    open val time = DuckDbType(JavaDuckDbTypes.time)
    open val timestamp = DuckDbType(JavaDuckDbTypes.timestamp)
    open val datetime = DuckDbType(JavaDuckDbTypes.datetime)
    open val timestamptz = DuckDbType(JavaDuckDbTypes.timestamptz)
    open val timetz = DuckDbType(JavaDuckDbTypes.timetz)
    open val timestamp_s = DuckDbType(JavaDuckDbTypes.timestamp_s)
    open val timestamp_ms = DuckDbType(JavaDuckDbTypes.timestamp_ms)
    open val timestamp_ns = DuckDbType(JavaDuckDbTypes.timestamp_ns)
    open val interval = DuckDbType(JavaDuckDbTypes.interval)
    open val uuid = DuckDbType(JavaDuckDbTypes.uuid)
    open val json = DuckDbType(JavaDuckDbTypes.json)
    // Pre-defined array-of-T values removed; users call `.list()` or `.array(size)` on the scalar.
    open val unknown = DuckDbType(JavaDuckDbTypes.unknown)

    // Parameterized methods
    open fun decimal(precision: Int, scale: Int) = DuckDbType(JavaDuckDbTypes.decimal(precision, scale))

    open fun varchar(length: Int) = DuckDbType(JavaDuckDbTypes.varchar(length))

    open fun char_(length: Int) = DuckDbType(JavaDuckDbTypes.char_(length))

    open fun bit(length: Int) = DuckDbType(JavaDuckDbTypes.bit(length))

    open fun <E : Enum<E>> ofEnum(enumTypeName: String, fromString: java.util.function.Function<String, E>) =
        DuckDbType(JavaDuckDbTypes.ofEnum(enumTypeName, fromString))

    // Composite (STRUCT) types

    open fun <Row : Any> compositeOf(structName: String, codec: RowCodecNamed<Row>) =
        DuckDbType(JavaDuckDbTypes.compositeOf(structName, codec.underlying))

    // JSON-encoded row types

    /** A JSON column type that stores a single row as a positional JSON array: [val1, val2, val3]. */
    open fun <Row : Any> jsonArrayEncoded(parser: RowCodec<Row>) =
        DuckDbType<Row>(JavaDuckDbTypes.jsonArrayEncoded(parser.underlying))

    /** A JSON column type that stores a list of rows, each as a positional JSON array. */
    open fun <Row : Any> jsonArrayEncodedList(parser: RowCodec<Row>) =
        DuckDbType<List<Row>>(JavaDuckDbTypes.jsonArrayEncodedList(parser.underlying))

    /** A JSON column type that stores a single row as a keyed JSON object: {"col": val, ...}. */
    open fun <Row : Any> jsonObjectEncoded(parser: RowCodecNamed<Row>) =
        DuckDbType<Row>(JavaDuckDbTypes.jsonObjectEncoded(parser.underlying))

    /** A JSON column type that stores a list of rows, each as a keyed JSON object. */
    open fun <Row : Any> jsonObjectEncodedList(parser: RowCodecNamed<Row>) =
        DuckDbType<List<Row>>(JavaDuckDbTypes.jsonObjectEncodedList(parser.underlying))

    /** JSON codec for Map<K, V> that serializes as a JSON object. */
    open fun <K, V> mapJson(
        keyJson: dev.typr.foundations.DuckDbJson<K>,
        valueJson: dev.typr.foundations.DuckDbJson<V>
    ): dev.typr.foundations.DuckDbJson<Map<K, V>> =
        JavaDuckDbTypes.mapJson(keyJson, valueJson).transform(
            dev.typr.foundations.SqlFunction { jmap -> jmap.toMap() },
            { kmap -> kmap.toMap(java.util.HashMap()) }
        )

    companion object : DuckDbTypes()
}
