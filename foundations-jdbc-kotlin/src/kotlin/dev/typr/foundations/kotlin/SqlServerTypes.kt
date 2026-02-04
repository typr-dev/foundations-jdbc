package dev.typr.foundations.kotlin

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

    companion object : SqlServerTypes()
}
