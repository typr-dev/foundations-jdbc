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
    open val tinyintArray = DuckDbType(JavaDuckDbTypes.tinyintArray)
    open val smallintArray = DuckDbType(JavaDuckDbTypes.smallintArray)
    open val integerArray = DuckDbType(JavaDuckDbTypes.integerArray)
    open val bigintArray = DuckDbType(JavaDuckDbTypes.bigintArray)
    open val hugeintArray = DuckDbType(JavaDuckDbTypes.hugeintArray)
    open val utinyintArray = DuckDbType(JavaDuckDbTypes.utinyintArray)
    open val usmallintArray = DuckDbType(JavaDuckDbTypes.usmallintArray)
    open val uintegerArray = DuckDbType(JavaDuckDbTypes.uintegerArray)
    open val ubigintArray = DuckDbType(JavaDuckDbTypes.ubigintArray)
    open val floatArray = DuckDbType(JavaDuckDbTypes.floatArray)
    open val doubleArray = DuckDbType(JavaDuckDbTypes.doubleArray)
    open val decimalArray = DuckDbType(JavaDuckDbTypes.decimalArray)
    open val booleanArray = DuckDbType(JavaDuckDbTypes.booleanArray)
    open val varcharArray = DuckDbType(JavaDuckDbTypes.varcharArray)
    // blobArray removed: BLOB[] not supported (binary can't be serialized in DuckDBUserArray)
    open val dateArray = DuckDbType(JavaDuckDbTypes.dateArray)
    open val timeArray = DuckDbType(JavaDuckDbTypes.timeArray)
    open val timestampArray = DuckDbType(JavaDuckDbTypes.timestampArray)
    open val timestamptzArray = DuckDbType(JavaDuckDbTypes.timestamptzArray)
    open val intervalArray = DuckDbType(JavaDuckDbTypes.intervalArray)
    open val uuidArray = DuckDbType(JavaDuckDbTypes.uuidArray)
    open val jsonArray = DuckDbType(JavaDuckDbTypes.jsonArray)
    open val listBoolean = DuckDbType(JavaDuckDbTypes.listBoolean)
    open val listTinyint = DuckDbType(JavaDuckDbTypes.listTinyint)
    open val listSmallint = DuckDbType(JavaDuckDbTypes.listSmallint)
    open val listInteger = DuckDbType(JavaDuckDbTypes.listInteger)
    open val listBigint = DuckDbType(JavaDuckDbTypes.listBigint)
    open val listFloat = DuckDbType(JavaDuckDbTypes.listFloat)
    open val listDouble = DuckDbType(JavaDuckDbTypes.listDouble)
    open val listVarchar = DuckDbType(JavaDuckDbTypes.listVarchar)
    open val listUuid = DuckDbType(JavaDuckDbTypes.listUuid)
    open val listDate = DuckDbType(JavaDuckDbTypes.listDate)
    open val listTime = DuckDbType(JavaDuckDbTypes.listTime)
    open val listTimestamp = DuckDbType(JavaDuckDbTypes.listTimestamp)
    open val listTimestamptz = DuckDbType(JavaDuckDbTypes.listTimestamptz)
    open val listDecimal = DuckDbType(JavaDuckDbTypes.listDecimal)
    open val listHugeint = DuckDbType(JavaDuckDbTypes.listHugeint)
    open val listInterval = DuckDbType(JavaDuckDbTypes.listInterval)
    open val unknown = DuckDbType(JavaDuckDbTypes.unknown)

    // Parameterized methods
    open fun decimal(precision: Int, scale: Int) = DuckDbType(JavaDuckDbTypes.decimal(precision, scale))

    open fun varchar(length: Int) = DuckDbType(JavaDuckDbTypes.varchar(length))

    open fun char_(length: Int) = DuckDbType(JavaDuckDbTypes.char_(length))

    open fun bit(length: Int) = DuckDbType(JavaDuckDbTypes.bit(length))

    open fun <E : Enum<E>> ofEnum(enumTypeName: String, fromString: java.util.function.Function<String, E>) =
        DuckDbType(JavaDuckDbTypes.ofEnum(enumTypeName, fromString))

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

    companion object : DuckDbTypes()
}
