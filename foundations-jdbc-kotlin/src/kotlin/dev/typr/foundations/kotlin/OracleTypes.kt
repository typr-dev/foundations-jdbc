package dev.typr.foundations.kotlin

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

    companion object : OracleTypes()
}
