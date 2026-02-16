@file:Suppress("unused")
package dev.typr.foundationskt.connect

// Core connection types
typealias DatabaseConfig = dev.typr.foundations.connect.DatabaseConfig
typealias ConnectionSettings = dev.typr.foundations.connect.ConnectionSettings
typealias DatabaseKind = dev.typr.foundations.connect.DatabaseKind
typealias TransactionIsolation = dev.typr.foundations.connect.TransactionIsolation

// PostgreSQL
typealias PostgresConfig = dev.typr.foundations.connect.PostgresConfig
typealias PgSslMode = dev.typr.foundations.connect.PgSslMode
typealias PgQueryMode = dev.typr.foundations.connect.PgQueryMode
typealias PgAutosave = dev.typr.foundations.connect.PgAutosave
typealias PgReadOnlyMode = dev.typr.foundations.connect.PgReadOnlyMode
typealias PgReplication = dev.typr.foundations.connect.PgReplication
typealias PgTargetServerType = dev.typr.foundations.connect.PgTargetServerType
typealias PgSslNegotiation = dev.typr.foundations.connect.PgSslNegotiation
typealias PgGssLib = dev.typr.foundations.connect.PgGssLib
typealias PgGssEncMode = dev.typr.foundations.connect.PgGssEncMode
typealias PgChannelBinding = dev.typr.foundations.connect.PgChannelBinding
typealias PgEscapeSyntaxCallMode = dev.typr.foundations.connect.PgEscapeSyntaxCallMode

// MariaDB/MySQL
typealias MariaDbConfig = dev.typr.foundations.connect.MariaDbConfig
typealias MariaSslMode = dev.typr.foundations.connect.MariaSslMode

// DuckDB
typealias DuckDbConfig = dev.typr.foundations.connect.DuckDbConfig

// SQL Server
typealias SqlServerConfig = dev.typr.foundations.connect.SqlServerConfig
typealias SqlServerAuthentication = dev.typr.foundations.connect.SqlServerAuthentication
typealias SqlServerEncrypt = dev.typr.foundations.connect.SqlServerEncrypt
typealias SqlServerApplicationIntent = dev.typr.foundations.connect.SqlServerApplicationIntent
typealias SqlServerAuthenticationScheme = dev.typr.foundations.connect.SqlServerAuthenticationScheme
typealias SqlServerSelectMethod = dev.typr.foundations.connect.SqlServerSelectMethod
typealias SqlServerColumnEncryptionSetting = dev.typr.foundations.connect.SqlServerColumnEncryptionSetting
typealias SqlServerResponseBuffering = dev.typr.foundations.connect.SqlServerResponseBuffering

// Oracle
typealias OracleConfig = dev.typr.foundations.connect.OracleConfig

// DB2
typealias Db2Config = dev.typr.foundations.connect.Db2Config
