package dev.typr.foundationskt

import dev.typr.foundations.SqlFunction
import dev.typr.foundations.OracleTypes as JavaOracleTypes

/**
 * Kotlin-friendly OracleType instances that use Kotlin types instead of Java boxed types.
 * All types from dev.typr.foundations.OracleTypes are available here, with primitives converted to Kotlin types.
 *
 * Extend this class to add your own custom types to a shared set of type definitions.
 */
open class OracleTypes {
    // Primitives - convert Java boxed types to Kotlin native types
    open val numberInt: OracleType<Int> = OracleType(JavaOracleTypes.numberInt.transform(SqlFunction { it }, { it }))
    open val numberLong: OracleType<Long> = OracleType(JavaOracleTypes.numberLong.transform(SqlFunction { it }, { it }))
    open val binaryFloat: OracleType<Float> = OracleType(JavaOracleTypes.binaryFloat.transform(SqlFunction { it }, { it }))

    /** Alias for [binaryFloat] — aesthetic cross-palette naming. 4B IEEE 754. */
    open val float4: OracleType<Float> = binaryFloat

    open val binaryDouble: OracleType<Double> = OracleType(JavaOracleTypes.binaryDouble.transform(SqlFunction { it }, { it }))

    /** Alias for [binaryDouble] — aesthetic cross-palette naming. 8B IEEE 754. */
    open val float8: OracleType<Double> = binaryDouble
    open val float_: OracleType<Double> = OracleType(JavaOracleTypes.float_.transform(SqlFunction { it }, { it }))
    open val real: OracleType<Double> = OracleType(JavaOracleTypes.real.transform(SqlFunction { it }, { it }))
    open val doublePrecision: OracleType<Double> = OracleType(JavaOracleTypes.doublePrecision.transform(SqlFunction { it }, { it }))
    open val boolean_: OracleType<Boolean> = OracleType(JavaOracleTypes.boolean_.transform(SqlFunction { it }, { it }))

    /** Alias for [boolean_] — aesthetic, avoids the Java-keyword `_` suffix. */
    open val bool: OracleType<Boolean> = boolean_
    open val numberAsBoolean: OracleType<Boolean> = OracleType(JavaOracleTypes.numberAsBoolean.transform(SqlFunction { it }, { it }))

    // Forward all other types directly from Java
    open val number = OracleType(JavaOracleTypes.number)
    open val integer = OracleType(JavaOracleTypes.integer)
    open val smallint = OracleType(JavaOracleTypes.smallint)
    open val varchar2 = OracleType(JavaOracleTypes.varchar2)
    open val char_ = OracleType(JavaOracleTypes.char_)

    /** Alias for [char_] — aesthetic, avoids the Java-keyword `_` suffix. */
    open val character = char_
    open val nvarchar2 = OracleType(JavaOracleTypes.nvarchar2)
    open val nchar = OracleType(JavaOracleTypes.nchar)
    open val clob = OracleType(JavaOracleTypes.clob)
    open val clobNonEmpty = OracleType(JavaOracleTypes.clobNonEmpty)
    open val nclob = OracleType(JavaOracleTypes.nclob)
    open val nclobNonEmpty = OracleType(JavaOracleTypes.nclobNonEmpty)
    open val long_ = OracleType(JavaOracleTypes.long_)

    /** Alias for [long_] — aesthetic, avoids the Java-keyword `_` suffix. */
    open val longColumn = long_
    open val raw = OracleType(JavaOracleTypes.raw)
    open val blob = OracleType(JavaOracleTypes.blob)
    open val blobNonEmpty = OracleType(JavaOracleTypes.blobNonEmpty)
    open val longRaw = OracleType(JavaOracleTypes.longRaw)
    open val date = OracleType(JavaOracleTypes.date)
    open val timestamp = OracleType(JavaOracleTypes.timestamp)
    open val timestampWithTimeZone = OracleType(JavaOracleTypes.timestampWithTimeZone)
    open val timestampWithLocalTimeZone = OracleType(JavaOracleTypes.timestampWithLocalTimeZone)
    open val intervalYearToMonth = OracleType(JavaOracleTypes.intervalYearToMonth)
    open val intervalDayToSecond = OracleType(JavaOracleTypes.intervalDayToSecond)
    open val rowId = OracleType(JavaOracleTypes.rowId)
    open val uRowId = OracleType(JavaOracleTypes.uRowId)
    open val xmlType = OracleType(JavaOracleTypes.xmlType)
    open val json = OracleType(JavaOracleTypes.json)
    open val unknown = OracleType(JavaOracleTypes.unknown)

    // Parameterized methods
    open fun numberOf(precision: Int) = OracleType(JavaOracleTypes.numberOf(precision))

    open fun numberOf(precision: Int, scale: Int) = OracleType(JavaOracleTypes.numberOf(precision, scale))

    open fun numberAsInt(precision: Int): OracleType<Int> =
        OracleType(JavaOracleTypes.numberAsInt(precision).transform(SqlFunction { it }, { it }))

    open fun numberAsLong(precision: Int): OracleType<Long> =
        OracleType(JavaOracleTypes.numberAsLong(precision).transform(SqlFunction { it }, { it }))

    open fun float_Of(binaryPrecision: Int): OracleType<Double> =
        OracleType(JavaOracleTypes.float_Of(binaryPrecision).transform(SqlFunction { it }, { it }))

    open fun varchar2Of(maxLength: Int) = OracleType(JavaOracleTypes.varchar2Of(maxLength))

    open fun varchar2NonEmpty(maxLength: Int) = OracleType(JavaOracleTypes.varchar2NonEmpty(maxLength))

    open fun char_Of(length: Int) = OracleType(JavaOracleTypes.char_Of(length))

    open fun charPadded(length: Int) = OracleType(JavaOracleTypes.charPadded(length))

    open fun nvarchar2Of(maxLength: Int) = OracleType(JavaOracleTypes.nvarchar2Of(maxLength))

    open fun nvarchar2NonEmpty(maxLength: Int) = OracleType(JavaOracleTypes.nvarchar2NonEmpty(maxLength))

    open fun ncharOf(length: Int) = OracleType(JavaOracleTypes.ncharOf(length))

    open fun ncharPadded(length: Int) = OracleType(JavaOracleTypes.ncharPadded(length))

    open fun rawOf(maxLength: Int) = OracleType(JavaOracleTypes.rawOf(maxLength))

    open fun rawNonEmpty(maxLength: Int) = OracleType(JavaOracleTypes.rawNonEmpty(maxLength))

    open fun timestampOf(fractionalSecondsPrecision: Int) = OracleType(JavaOracleTypes.timestampOf(fractionalSecondsPrecision))

    open fun timestampWithTimeZone(fractionalSecondsPrecision: Int) =
        OracleType(JavaOracleTypes.timestampWithTimeZone(fractionalSecondsPrecision))

    open fun timestampWithLocalTimeZone(fractionalSecondsPrecision: Int) =
        OracleType(JavaOracleTypes.timestampWithLocalTimeZone(fractionalSecondsPrecision))

    open fun intervalYearToMonth(yearPrecision: Int) = OracleType(JavaOracleTypes.intervalYearToMonth(yearPrecision))

    open fun intervalDayToSecond(dayPrecision: Int, fractionalSecondsPrecision: Int) =
        OracleType(JavaOracleTypes.intervalDayToSecond(dayPrecision, fractionalSecondsPrecision))

    open fun uRowId(maxLength: Int) = OracleType(JavaOracleTypes.uRowId(maxLength))

    open fun <E : Enum<E>> ofEnum(sqlType: String, fromString: java.util.function.Function<String, E>) =
        OracleType(JavaOracleTypes.ofEnum(sqlType, fromString))

    // JSON-encoded row types

    /** A JSON column type that stores a single row as a positional JSON array: [val1, val2, val3]. */
    open fun <Row : Any> jsonArrayEncoded(parser: RowCodec<Row>) =
        OracleType<Row>(JavaOracleTypes.jsonArrayEncoded(parser.underlying))

    /** A JSON column type that stores a list of rows, each as a positional JSON array. */
    open fun <Row : Any> jsonArrayEncodedList(parser: RowCodec<Row>) =
        OracleType<List<Row>>(JavaOracleTypes.jsonArrayEncodedList(parser.underlying))

    /** A JSON column type that stores a single row as a keyed JSON object: {"col": val, ...}. */
    open fun <Row : Any> jsonObjectEncoded(parser: RowCodecNamed<Row>) =
        OracleType<Row>(JavaOracleTypes.jsonObjectEncoded(parser.underlying))

    /** A JSON column type that stores a list of rows, each as a keyed JSON object. */
    open fun <Row : Any> jsonObjectEncodedList(parser: RowCodecNamed<Row>) =
        OracleType<List<Row>>(JavaOracleTypes.jsonObjectEncodedList(parser.underlying))

    /** Build a named Oracle OBJECT type from a RowCodecNamed. */
    open fun <Row : Any> compositeOf(objectTypeName: String, codec: RowCodecNamed<Row>) =
        OracleType<Row>(JavaOracleTypes.compositeOf(objectTypeName, codec.underlying))

    companion object : OracleTypes()
}
