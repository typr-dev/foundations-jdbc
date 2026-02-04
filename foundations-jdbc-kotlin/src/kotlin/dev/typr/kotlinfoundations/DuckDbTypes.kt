package dev.typr.kotlinfoundations

import dev.typr.foundations.DuckDbType
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
    open val tinyint: DuckDbType<Byte> = JavaDuckDbTypes.tinyint.bimap(SqlFunction { it }, { it })
    open val smallint: DuckDbType<Short> = JavaDuckDbTypes.smallint.bimap(SqlFunction { it }, { it })
    open val integer: DuckDbType<Int> = JavaDuckDbTypes.integer.bimap(SqlFunction { it }, { it })
    open val bigint: DuckDbType<Long> = JavaDuckDbTypes.bigint.bimap(SqlFunction { it }, { it })
    open val float_: DuckDbType<Float> = JavaDuckDbTypes.float_.bimap(SqlFunction { it }, { it })
    open val double_: DuckDbType<Double> = JavaDuckDbTypes.double_.bimap(SqlFunction { it }, { it })
    open val boolean_: DuckDbType<Boolean> = JavaDuckDbTypes.boolean_.bimap(SqlFunction { it }, { it })
    open val bool: DuckDbType<Boolean> = JavaDuckDbTypes.bool.bimap(SqlFunction { it }, { it })

    // Forward all other types directly from Java
    open val decimal = JavaDuckDbTypes.decimal
    open val numeric = JavaDuckDbTypes.numeric
    open val hugeint = JavaDuckDbTypes.hugeint
    open val utinyint = JavaDuckDbTypes.utinyint
    open val usmallint = JavaDuckDbTypes.usmallint
    open val uinteger = JavaDuckDbTypes.uinteger
    open val ubigint = JavaDuckDbTypes.ubigint
    open val uhugeint = JavaDuckDbTypes.uhugeint
    open val real = JavaDuckDbTypes.real
    open val float4 = JavaDuckDbTypes.float4
    open val float8 = JavaDuckDbTypes.float8
    open val varchar = JavaDuckDbTypes.varchar
    open val text = JavaDuckDbTypes.text
    open val string = JavaDuckDbTypes.string
    open val char_ = JavaDuckDbTypes.char_
    open val bpchar = JavaDuckDbTypes.bpchar
    open val blob = JavaDuckDbTypes.blob
    open val bytea = JavaDuckDbTypes.bytea
    open val binary = JavaDuckDbTypes.binary
    open val varbinary = JavaDuckDbTypes.varbinary
    open val bit = JavaDuckDbTypes.bit
    open val bitstring = JavaDuckDbTypes.bitstring
    open val date = JavaDuckDbTypes.date
    open val time = JavaDuckDbTypes.time
    open val timestamp = JavaDuckDbTypes.timestamp
    open val datetime = JavaDuckDbTypes.datetime
    open val timestamptz = JavaDuckDbTypes.timestamptz
    open val timetz = JavaDuckDbTypes.timetz
    open val timestamp_s = JavaDuckDbTypes.timestamp_s
    open val timestamp_ms = JavaDuckDbTypes.timestamp_ms
    open val timestamp_ns = JavaDuckDbTypes.timestamp_ns
    open val interval = JavaDuckDbTypes.interval
    open val uuid = JavaDuckDbTypes.uuid
    open val json = JavaDuckDbTypes.json
    open val tinyintArray = JavaDuckDbTypes.tinyintArray
    open val smallintArray = JavaDuckDbTypes.smallintArray
    open val integerArray = JavaDuckDbTypes.integerArray
    open val bigintArray = JavaDuckDbTypes.bigintArray
    open val hugeintArray = JavaDuckDbTypes.hugeintArray
    open val utinyintArray = JavaDuckDbTypes.utinyintArray
    open val usmallintArray = JavaDuckDbTypes.usmallintArray
    open val uintegerArray = JavaDuckDbTypes.uintegerArray
    open val ubigintArray = JavaDuckDbTypes.ubigintArray
    open val floatArray = JavaDuckDbTypes.floatArray
    open val doubleArray = JavaDuckDbTypes.doubleArray
    open val decimalArray = JavaDuckDbTypes.decimalArray
    open val booleanArray = JavaDuckDbTypes.booleanArray
    open val varcharArray = JavaDuckDbTypes.varcharArray
    open val blobArray = JavaDuckDbTypes.blobArray
    open val dateArray = JavaDuckDbTypes.dateArray
    open val timeArray = JavaDuckDbTypes.timeArray
    open val timestampArray = JavaDuckDbTypes.timestampArray
    open val timestamptzArray = JavaDuckDbTypes.timestamptzArray
    open val intervalArray = JavaDuckDbTypes.intervalArray
    open val uuidArray = JavaDuckDbTypes.uuidArray
    open val jsonArray = JavaDuckDbTypes.jsonArray
    open val listBoolean = JavaDuckDbTypes.listBoolean
    open val listTinyint = JavaDuckDbTypes.listTinyint
    open val listSmallint = JavaDuckDbTypes.listSmallint
    open val listInteger = JavaDuckDbTypes.listInteger
    open val listBigint = JavaDuckDbTypes.listBigint
    open val listFloat = JavaDuckDbTypes.listFloat
    open val listDouble = JavaDuckDbTypes.listDouble
    open val listVarchar = JavaDuckDbTypes.listVarchar
    open val listUuid = JavaDuckDbTypes.listUuid
    open val listDate = JavaDuckDbTypes.listDate
    open val listTime = JavaDuckDbTypes.listTime
    open val listTimestamp = JavaDuckDbTypes.listTimestamp
    open val listTimestamptz = JavaDuckDbTypes.listTimestamptz
    open val listDecimal = JavaDuckDbTypes.listDecimal
    open val listHugeint = JavaDuckDbTypes.listHugeint
    open val listInterval = JavaDuckDbTypes.listInterval
    open val unknown = JavaDuckDbTypes.unknown

    // Parameterized methods
    open fun decimal(precision: Int, scale: Int): DuckDbType<java.math.BigDecimal> =
        JavaDuckDbTypes.decimal(precision, scale)

    open fun varchar(length: Int): DuckDbType<String> = JavaDuckDbTypes.varchar(length)

    open fun char_(length: Int): DuckDbType<String> = JavaDuckDbTypes.char_(length)

    open fun bit(length: Int): DuckDbType<String> = JavaDuckDbTypes.bit(length)

    open fun <E : Enum<E>> ofEnum(enumTypeName: String, fromString: java.util.function.Function<String, E>): DuckDbType<E> =
        JavaDuckDbTypes.ofEnum(enumTypeName, fromString)

    companion object : DuckDbTypes()
}
