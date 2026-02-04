package dev.typr.kotlinfoundations

import dev.typr.foundations.OracleType
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
    open val numberInt: OracleType<Int> = JavaOracleTypes.numberInt.bimap(SqlFunction { it }, { it })
    open val numberLong: OracleType<Long> = JavaOracleTypes.numberLong.bimap(SqlFunction { it }, { it })
    open val binaryFloat: OracleType<Float> = JavaOracleTypes.binaryFloat.bimap(SqlFunction { it }, { it })
    open val binaryDouble: OracleType<Double> = JavaOracleTypes.binaryDouble.bimap(SqlFunction { it }, { it })
    open val float_: OracleType<Double> = JavaOracleTypes.float_.bimap(SqlFunction { it }, { it })
    open val real: OracleType<Double> = JavaOracleTypes.real.bimap(SqlFunction { it }, { it })
    open val doublePrecision: OracleType<Double> = JavaOracleTypes.doublePrecision.bimap(SqlFunction { it }, { it })
    open val boolean_: OracleType<Boolean> = JavaOracleTypes.boolean_.bimap(SqlFunction { it }, { it })
    open val numberAsBoolean: OracleType<Boolean> = JavaOracleTypes.numberAsBoolean.bimap(SqlFunction { it }, { it })

    // Forward all other types directly from Java
    open val number = JavaOracleTypes.number
    open val integer = JavaOracleTypes.integer
    open val smallint = JavaOracleTypes.smallint
    open val varchar2 = JavaOracleTypes.varchar2
    open val char_ = JavaOracleTypes.char_
    open val nvarchar2 = JavaOracleTypes.nvarchar2
    open val nchar = JavaOracleTypes.nchar
    open val clob = JavaOracleTypes.clob
    open val clobNonEmpty = JavaOracleTypes.clobNonEmpty
    open val nclob = JavaOracleTypes.nclob
    open val nclobNonEmpty = JavaOracleTypes.nclobNonEmpty
    open val long_ = JavaOracleTypes.long_
    open val raw = JavaOracleTypes.raw
    open val blob = JavaOracleTypes.blob
    open val blobNonEmpty = JavaOracleTypes.blobNonEmpty
    open val longRaw = JavaOracleTypes.longRaw
    open val date = JavaOracleTypes.date
    open val timestamp = JavaOracleTypes.timestamp
    open val timestampWithTimeZone = JavaOracleTypes.timestampWithTimeZone
    open val timestampWithLocalTimeZone = JavaOracleTypes.timestampWithLocalTimeZone
    open val intervalYearToMonth = JavaOracleTypes.intervalYearToMonth
    open val intervalDayToSecond = JavaOracleTypes.intervalDayToSecond
    open val rowId = JavaOracleTypes.rowId
    open val uRowId = JavaOracleTypes.uRowId
    open val xmlType = JavaOracleTypes.xmlType
    open val json = JavaOracleTypes.json
    open val unknown = JavaOracleTypes.unknown

    // Parameterized methods
    open fun number(precision: Int): OracleType<java.math.BigDecimal> = JavaOracleTypes.number(precision)

    open fun number(precision: Int, scale: Int): OracleType<java.math.BigDecimal> =
        JavaOracleTypes.number(precision, scale)

    open fun numberAsInt(precision: Int): OracleType<Int> =
        JavaOracleTypes.numberAsInt(precision).bimap(SqlFunction { it }, { it })

    open fun numberAsLong(precision: Int): OracleType<Long> =
        JavaOracleTypes.numberAsLong(precision).bimap(SqlFunction { it }, { it })

    open fun float_(binaryPrecision: Int): OracleType<Double> =
        JavaOracleTypes.float_(binaryPrecision).bimap(SqlFunction { it }, { it })

    open fun varchar2(maxLength: Int): OracleType<String> = JavaOracleTypes.varchar2(maxLength)

    open fun varchar2NonEmpty(maxLength: Int): OracleType<dev.typr.foundations.NonEmptyString> =
        JavaOracleTypes.varchar2NonEmpty(maxLength)

    open fun char_(length: Int): OracleType<String> = JavaOracleTypes.char_(length)

    open fun charPadded(length: Int): OracleType<dev.typr.foundations.PaddedString> =
        JavaOracleTypes.charPadded(length)

    open fun nvarchar2(maxLength: Int): OracleType<String> = JavaOracleTypes.nvarchar2(maxLength)

    open fun nvarchar2NonEmpty(maxLength: Int): OracleType<dev.typr.foundations.NonEmptyString> =
        JavaOracleTypes.nvarchar2NonEmpty(maxLength)

    open fun nchar(length: Int): OracleType<String> = JavaOracleTypes.nchar(length)

    open fun ncharPadded(length: Int): OracleType<dev.typr.foundations.PaddedString> =
        JavaOracleTypes.ncharPadded(length)

    open fun raw(maxLength: Int): OracleType<ByteArray> = JavaOracleTypes.raw(maxLength)

    open fun rawNonEmpty(maxLength: Int): OracleType<dev.typr.foundations.NonEmptyBlob> =
        JavaOracleTypes.rawNonEmpty(maxLength)

    open fun timestamp(fractionalSecondsPrecision: Int): OracleType<java.time.LocalDateTime> =
        JavaOracleTypes.timestamp(fractionalSecondsPrecision)

    open fun timestampWithTimeZone(fractionalSecondsPrecision: Int): OracleType<java.time.OffsetDateTime> =
        JavaOracleTypes.timestampWithTimeZone(fractionalSecondsPrecision)

    open fun timestampWithLocalTimeZone(fractionalSecondsPrecision: Int): OracleType<java.time.OffsetDateTime> =
        JavaOracleTypes.timestampWithLocalTimeZone(fractionalSecondsPrecision)

    open fun intervalYearToMonth(yearPrecision: Int): OracleType<dev.typr.foundations.data.OracleIntervalYM> =
        JavaOracleTypes.intervalYearToMonth(yearPrecision)

    open fun intervalDayToSecond(dayPrecision: Int, fractionalSecondsPrecision: Int): OracleType<dev.typr.foundations.data.OracleIntervalDS> =
        JavaOracleTypes.intervalDayToSecond(dayPrecision, fractionalSecondsPrecision)

    open fun uRowId(maxLength: Int): OracleType<String> = JavaOracleTypes.uRowId(maxLength)

    open fun <E : Enum<E>> ofEnum(sqlType: String, fromString: java.util.function.Function<String, E>): OracleType<E> =
        JavaOracleTypes.ofEnum(sqlType, fromString)

    companion object : OracleTypes()
}
