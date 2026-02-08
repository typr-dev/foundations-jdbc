@file:Suppress("unused")
package dev.typr.kotlinfoundations

// Re-export core types from Java package
typealias DbType<T> = dev.typr.foundations.DbType<T>
typealias Fragment = dev.typr.foundations.Fragment
typealias And<L, R> = dev.typr.foundations.And<L, R>
typealias Transactor = dev.typr.foundations.Transactor
typealias SqlFunction<T, R> = dev.typr.foundations.SqlFunction<T, R>
typealias Operation<T> = dev.typr.foundations.Operation<T>

// Nested types need direct typealiases since Kotlin typealiases don't expose nested classes
typealias Query<T> = dev.typr.foundations.Operation.Query<T>
typealias Update = dev.typr.foundations.Operation.Update
typealias UpdateReturning<T> = dev.typr.foundations.Operation.UpdateReturning<T>
typealias Strategy = dev.typr.foundations.Transactor.Strategy

typealias DbJson<T> = dev.typr.foundations.DbJson<T>
typealias NonEmptyBlob = dev.typr.foundations.NonEmptyBlob
typealias NonEmptyString = dev.typr.foundations.NonEmptyString
typealias PaddedString = dev.typr.foundations.PaddedString
typealias PgStruct<T> = dev.typr.foundations.PgStruct<T>
typealias DuckDbStruct<T> = dev.typr.foundations.DuckDbStruct<T>
typealias OracleObject<T> = dev.typr.foundations.OracleObject<T>

// Re-export database-specific type classes
typealias PgType<T> = dev.typr.foundations.PgType<T>
typealias MariaType<T> = dev.typr.foundations.MariaType<T>
typealias DuckDbType<T> = dev.typr.foundations.DuckDbType<T>
typealias OracleType<T> = dev.typr.foundations.OracleType<T>
typealias SqlServerType<T> = dev.typr.foundations.SqlServerType<T>
typealias Db2Type<T> = dev.typr.foundations.Db2Type<T>

// Re-export analysis types
typealias QueryAnalysis = dev.typr.foundations.analysis.QueryAnalysis
typealias QueryAnalyzer = dev.typr.foundations.analysis.QueryAnalyzer

// Re-export connection types
typealias ConnectionSource = dev.typr.foundations.connect.ConnectionSource
typealias OracleConfig = dev.typr.foundations.connect.oracle.OracleConfig
typealias PostgresConfig = dev.typr.foundations.connect.postgres.PostgresConfig
typealias MariaDbConfig = dev.typr.foundations.connect.mariadb.MariaDbConfig
typealias DuckDbConfig = dev.typr.foundations.connect.duckdb.DuckDbConfig
typealias SqlServerConfig = dev.typr.foundations.connect.sqlserver.SqlServerConfig
typealias Db2Config = dev.typr.foundations.connect.db2.Db2Config
