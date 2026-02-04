package dev.typr.kotlinfoundations

import dev.typr.foundations.SqlServerType
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
    open val smallint: SqlServerType<Short> = JavaSqlServerTypes.smallint.bimap(SqlFunction { it }, { it })
    open val int_: SqlServerType<Int> = JavaSqlServerTypes.int_.bimap(SqlFunction { it }, { it })
    open val bigint: SqlServerType<Long> = JavaSqlServerTypes.bigint.bimap(SqlFunction { it }, { it })
    open val real: SqlServerType<Float> = JavaSqlServerTypes.real.bimap(SqlFunction { it }, { it })
    open val float_: SqlServerType<Double> = JavaSqlServerTypes.float_.bimap(SqlFunction { it }, { it })
    open val bit: SqlServerType<Boolean> = JavaSqlServerTypes.bit.bimap(SqlFunction { it }, { it })

    // Forward all other types directly from Java
    open val tinyint = JavaSqlServerTypes.tinyint
    open val decimal = JavaSqlServerTypes.decimal
    open val numeric = JavaSqlServerTypes.numeric
    open val money = JavaSqlServerTypes.money
    open val smallmoney = JavaSqlServerTypes.smallmoney
    open val char_ = JavaSqlServerTypes.char_
    open val varchar = JavaSqlServerTypes.varchar
    open val varcharMax = JavaSqlServerTypes.varcharMax
    open val text = JavaSqlServerTypes.text
    open val nchar = JavaSqlServerTypes.nchar
    open val nvarchar = JavaSqlServerTypes.nvarchar
    open val nvarcharMax = JavaSqlServerTypes.nvarcharMax
    open val ntext = JavaSqlServerTypes.ntext
    open val binary = JavaSqlServerTypes.binary
    open val varbinary = JavaSqlServerTypes.varbinary
    open val varbinaryMax = JavaSqlServerTypes.varbinaryMax
    open val image = JavaSqlServerTypes.image
    open val date = JavaSqlServerTypes.date
    open val time = JavaSqlServerTypes.time
    open val datetime = JavaSqlServerTypes.datetime
    open val smalldatetime = JavaSqlServerTypes.smalldatetime
    open val datetime2 = JavaSqlServerTypes.datetime2
    open val datetimeoffset = JavaSqlServerTypes.datetimeoffset
    open val uniqueidentifier = JavaSqlServerTypes.uniqueidentifier
    open val xml = JavaSqlServerTypes.xml
    open val json = JavaSqlServerTypes.json
    open val vector = JavaSqlServerTypes.vector
    open val rowversion = JavaSqlServerTypes.rowversion
    open val timestamp = JavaSqlServerTypes.timestamp
    open val hierarchyid = JavaSqlServerTypes.hierarchyid
    open val sqlVariant = JavaSqlServerTypes.sqlVariant
    open val geography = JavaSqlServerTypes.geography
    open val geometry = JavaSqlServerTypes.geometry
    open val unknown = JavaSqlServerTypes.unknown

    // Parameterized methods
    open fun decimal(precision: Int, scale: Int): SqlServerType<java.math.BigDecimal> =
        JavaSqlServerTypes.decimal(precision, scale)

    open fun numeric(precision: Int, scale: Int): SqlServerType<java.math.BigDecimal> =
        JavaSqlServerTypes.numeric(precision, scale)

    open fun char_(length: Int): SqlServerType<String> = JavaSqlServerTypes.char_(length)

    open fun varchar(length: Int): SqlServerType<String> = JavaSqlServerTypes.varchar(length)

    open fun nchar(length: Int): SqlServerType<String> = JavaSqlServerTypes.nchar(length)

    open fun nvarchar(length: Int): SqlServerType<String> = JavaSqlServerTypes.nvarchar(length)

    open fun binary(length: Int): SqlServerType<ByteArray> = JavaSqlServerTypes.binary(length)

    open fun varbinary(length: Int): SqlServerType<ByteArray> = JavaSqlServerTypes.varbinary(length)

    open fun time(scale: Int): SqlServerType<java.time.LocalTime> = JavaSqlServerTypes.time(scale)

    open fun datetime2(scale: Int): SqlServerType<java.time.LocalDateTime> = JavaSqlServerTypes.datetime2(scale)

    open fun datetimeoffset(scale: Int): SqlServerType<java.time.OffsetDateTime> = JavaSqlServerTypes.datetimeoffset(scale)

    companion object : SqlServerTypes()
}
