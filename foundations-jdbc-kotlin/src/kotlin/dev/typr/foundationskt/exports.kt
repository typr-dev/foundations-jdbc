@file:Suppress("unused")
package dev.typr.foundationskt

// Functional interfaces
typealias SqlFunction<T, R> = dev.typr.foundations.SqlFunction<T, R>
typealias SqlConsumer<T> = dev.typr.foundations.SqlConsumer<T>
typealias SqlSupplier<T> = dev.typr.foundations.SqlSupplier<T>
typealias SqlBiConsumer<T, U> = dev.typr.foundations.SqlBiConsumer<T, U>
typealias SqlBiFunction<T, U, R> = dev.typr.foundations.SqlBiFunction<T, U, R>

// Query analysis
typealias QueryAnalysis = dev.typr.foundations.QueryAnalysis
typealias CheckReport = dev.typr.foundations.CheckReport
typealias QueryListener = dev.typr.foundations.QueryListener
typealias QueryEvent = dev.typr.foundations.QueryEvent

// Codec internals (needed for struct/array construction)
typealias DbJson<T> = dev.typr.foundations.DbJson<T>
typealias DbJsonRow = dev.typr.foundations.DbJsonRow
typealias PgText<T> = dev.typr.foundations.PgText<T>
typealias PgRead<T> = dev.typr.foundations.PgRead<T>
typealias PgWrite<T> = dev.typr.foundations.PgWrite<T>
typealias PgCompositeText<T> = dev.typr.foundations.PgCompositeText<T>
typealias PgRecordParser = dev.typr.foundations.PgRecordParser

// Oracle collection wrappers (return Kotlin OracleType, not Java)
object OracleNestedTable {
    fun <T> of(typeName: String, elementType: OracleType<T>): OracleType<List<T>> =
        OracleType(dev.typr.foundations.OracleNestedTable.of(typeName, elementType.underlying))
}

object OracleVArray {
    fun <T> of(typeName: String, maxSize: Int, elementType: OracleType<T>): OracleType<List<T>> =
        OracleType(dev.typr.foundations.OracleVArray.of(typeName, maxSize, elementType.underlying))
}

// Exceptions
typealias DatabaseException = dev.typr.foundations.DatabaseException
