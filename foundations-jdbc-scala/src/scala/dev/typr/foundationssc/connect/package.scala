package dev.typr.foundationssc

// NOTE: Using type aliases because Gradle's Zinc doesn't handle Scala 3 exports.
// See: https://github.com/gradle/gradle/issues/29286
package object connect:
  // Core
  type DatabaseConfig = dev.typr.foundations.connect.DatabaseConfig
  type DatabaseKind = dev.typr.foundations.connect.DatabaseKind
  object DatabaseKind:
    val POSTGRESQL: DatabaseKind = dev.typr.foundations.connect.DatabaseKind.POSTGRESQL
    val MARIADB: DatabaseKind = dev.typr.foundations.connect.DatabaseKind.MARIADB
    val DUCKDB: DatabaseKind = dev.typr.foundations.connect.DatabaseKind.DUCKDB
    val ORACLE: DatabaseKind = dev.typr.foundations.connect.DatabaseKind.ORACLE
    val SQLSERVER: DatabaseKind = dev.typr.foundations.connect.DatabaseKind.SQLSERVER
    val DB2: DatabaseKind = dev.typr.foundations.connect.DatabaseKind.DB2
  type ConnectionSettings = dev.typr.foundations.connect.ConnectionSettings
  object ConnectionSettings:
    def builder(): dev.typr.foundations.connect.ConnectionSettings.Builder =
      dev.typr.foundations.connect.ConnectionSettings.builder()
    val EMPTY: ConnectionSettings = dev.typr.foundations.connect.ConnectionSettings.EMPTY
  type TransactionIsolation = dev.typr.foundations.connect.TransactionIsolation
  object TransactionIsolation:
    val READ_UNCOMMITTED: TransactionIsolation = dev.typr.foundations.connect.TransactionIsolation.READ_UNCOMMITTED
    val READ_COMMITTED: TransactionIsolation = dev.typr.foundations.connect.TransactionIsolation.READ_COMMITTED
    val REPEATABLE_READ: TransactionIsolation = dev.typr.foundations.connect.TransactionIsolation.REPEATABLE_READ
    val SERIALIZABLE: TransactionIsolation = dev.typr.foundations.connect.TransactionIsolation.SERIALIZABLE
    val NONE: TransactionIsolation = dev.typr.foundations.connect.TransactionIsolation.NONE

  // PostgreSQL
  type PostgresConfig = dev.typr.foundations.connect.PostgresConfig
  object PostgresConfig:
    def builder(host: String, port: Int, database: String, username: String, password: String): dev.typr.foundations.connect.PostgresConfig.Builder =
      dev.typr.foundations.connect.PostgresConfig.builder(host, port, database, username, password)
  type PgSslMode = dev.typr.foundations.connect.PgSslMode
  object PgSslMode:
    val DISABLE: PgSslMode = dev.typr.foundations.connect.PgSslMode.DISABLE
    val ALLOW: PgSslMode = dev.typr.foundations.connect.PgSslMode.ALLOW
    val PREFER: PgSslMode = dev.typr.foundations.connect.PgSslMode.PREFER
    val REQUIRE: PgSslMode = dev.typr.foundations.connect.PgSslMode.REQUIRE
    val VERIFY_CA: PgSslMode = dev.typr.foundations.connect.PgSslMode.VERIFY_CA
    val VERIFY_FULL: PgSslMode = dev.typr.foundations.connect.PgSslMode.VERIFY_FULL
  type PgQueryMode = dev.typr.foundations.connect.PgQueryMode
  object PgQueryMode:
    val SIMPLE: PgQueryMode = dev.typr.foundations.connect.PgQueryMode.SIMPLE
    val EXTENDED: PgQueryMode = dev.typr.foundations.connect.PgQueryMode.EXTENDED
    val EXTENDED_FOR_PREPARED: PgQueryMode = dev.typr.foundations.connect.PgQueryMode.EXTENDED_FOR_PREPARED
    val EXTENDED_CACHE_EVERYTHING: PgQueryMode = dev.typr.foundations.connect.PgQueryMode.EXTENDED_CACHE_EVERYTHING
  type PgAutosave = dev.typr.foundations.connect.PgAutosave
  object PgAutosave:
    val NEVER: PgAutosave = dev.typr.foundations.connect.PgAutosave.NEVER
    val ALWAYS: PgAutosave = dev.typr.foundations.connect.PgAutosave.ALWAYS
    val CONSERVATIVE: PgAutosave = dev.typr.foundations.connect.PgAutosave.CONSERVATIVE
  type PgReadOnlyMode = dev.typr.foundations.connect.PgReadOnlyMode
  object PgReadOnlyMode:
    val IGNORE: PgReadOnlyMode = dev.typr.foundations.connect.PgReadOnlyMode.IGNORE
    val TRANSACTION: PgReadOnlyMode = dev.typr.foundations.connect.PgReadOnlyMode.TRANSACTION
    val ALWAYS: PgReadOnlyMode = dev.typr.foundations.connect.PgReadOnlyMode.ALWAYS
  type PgReplication = dev.typr.foundations.connect.PgReplication
  object PgReplication:
    val FALSE: PgReplication = dev.typr.foundations.connect.PgReplication.FALSE
    val TRUE: PgReplication = dev.typr.foundations.connect.PgReplication.TRUE
    val DATABASE: PgReplication = dev.typr.foundations.connect.PgReplication.DATABASE
  type PgTargetServerType = dev.typr.foundations.connect.PgTargetServerType
  object PgTargetServerType:
    val ANY: PgTargetServerType = dev.typr.foundations.connect.PgTargetServerType.ANY
    val PRIMARY: PgTargetServerType = dev.typr.foundations.connect.PgTargetServerType.PRIMARY
    val MASTER: PgTargetServerType = dev.typr.foundations.connect.PgTargetServerType.MASTER
    val SECONDARY: PgTargetServerType = dev.typr.foundations.connect.PgTargetServerType.SECONDARY
    val SLAVE: PgTargetServerType = dev.typr.foundations.connect.PgTargetServerType.SLAVE
    val PREFER_SECONDARY: PgTargetServerType = dev.typr.foundations.connect.PgTargetServerType.PREFER_SECONDARY
    val PREFER_SLAVE: PgTargetServerType = dev.typr.foundations.connect.PgTargetServerType.PREFER_SLAVE
  type PgSslNegotiation = dev.typr.foundations.connect.PgSslNegotiation
  object PgSslNegotiation:
    val POSTGRES: PgSslNegotiation = dev.typr.foundations.connect.PgSslNegotiation.POSTGRES
    val DIRECT: PgSslNegotiation = dev.typr.foundations.connect.PgSslNegotiation.DIRECT
  type PgGssLib = dev.typr.foundations.connect.PgGssLib
  object PgGssLib:
    val AUTO: PgGssLib = dev.typr.foundations.connect.PgGssLib.AUTO
    val GSSAPI: PgGssLib = dev.typr.foundations.connect.PgGssLib.GSSAPI
    val SSPI: PgGssLib = dev.typr.foundations.connect.PgGssLib.SSPI
  type PgGssEncMode = dev.typr.foundations.connect.PgGssEncMode
  object PgGssEncMode:
    val DISABLE: PgGssEncMode = dev.typr.foundations.connect.PgGssEncMode.DISABLE
    val PREFER: PgGssEncMode = dev.typr.foundations.connect.PgGssEncMode.PREFER
    val REQUIRE: PgGssEncMode = dev.typr.foundations.connect.PgGssEncMode.REQUIRE
  type PgChannelBinding = dev.typr.foundations.connect.PgChannelBinding
  object PgChannelBinding:
    val DISABLE: PgChannelBinding = dev.typr.foundations.connect.PgChannelBinding.DISABLE
    val PREFER: PgChannelBinding = dev.typr.foundations.connect.PgChannelBinding.PREFER
    val REQUIRE: PgChannelBinding = dev.typr.foundations.connect.PgChannelBinding.REQUIRE
  type PgEscapeSyntaxCallMode = dev.typr.foundations.connect.PgEscapeSyntaxCallMode
  object PgEscapeSyntaxCallMode:
    val SELECT: PgEscapeSyntaxCallMode = dev.typr.foundations.connect.PgEscapeSyntaxCallMode.SELECT
    val CALL_IF_NO_RETURN: PgEscapeSyntaxCallMode = dev.typr.foundations.connect.PgEscapeSyntaxCallMode.CALL_IF_NO_RETURN
    val CALL: PgEscapeSyntaxCallMode = dev.typr.foundations.connect.PgEscapeSyntaxCallMode.CALL

  // MariaDB/MySQL
  type MariaDbConfig = dev.typr.foundations.connect.MariaDbConfig
  object MariaDbConfig:
    def builder(host: String, port: Int, database: String, username: String, password: String): dev.typr.foundations.connect.MariaDbConfig.Builder =
      dev.typr.foundations.connect.MariaDbConfig.builder(host, port, database, username, password)
  type MariaSslMode = dev.typr.foundations.connect.MariaSslMode
  object MariaSslMode:
    val DISABLE: MariaSslMode = dev.typr.foundations.connect.MariaSslMode.DISABLE
    val TRUST: MariaSslMode = dev.typr.foundations.connect.MariaSslMode.TRUST
    val VERIFY_CA: MariaSslMode = dev.typr.foundations.connect.MariaSslMode.VERIFY_CA
    val VERIFY_FULL: MariaSslMode = dev.typr.foundations.connect.MariaSslMode.VERIFY_FULL

  // DuckDB
  type DuckDbConfig = dev.typr.foundations.connect.DuckDbConfig
  object DuckDbConfig:
    def builder(path: String): dev.typr.foundations.connect.DuckDbConfig.Builder =
      dev.typr.foundations.connect.DuckDbConfig.builder(path)
    def inMemory(): dev.typr.foundations.connect.DuckDbConfig.Builder =
      dev.typr.foundations.connect.DuckDbConfig.inMemory()

  // Oracle
  type OracleConfig = dev.typr.foundations.connect.OracleConfig
  object OracleConfig:
    def builder(host: String, port: Int, database: String, username: String, password: String): dev.typr.foundations.connect.OracleConfig.Builder =
      dev.typr.foundations.connect.OracleConfig.builder(host, port, database, username, password)

  // SQL Server
  type SqlServerConfig = dev.typr.foundations.connect.SqlServerConfig
  object SqlServerConfig:
    def builder(host: String, port: Int, database: String, username: String, password: String): dev.typr.foundations.connect.SqlServerConfig.Builder =
      dev.typr.foundations.connect.SqlServerConfig.builder(host, port, database, username, password)
  type SqlServerEncrypt = dev.typr.foundations.connect.SqlServerEncrypt
  object SqlServerEncrypt:
    val FALSE: SqlServerEncrypt = dev.typr.foundations.connect.SqlServerEncrypt.FALSE
    val STRICT: SqlServerEncrypt = dev.typr.foundations.connect.SqlServerEncrypt.STRICT
    val TRUE: SqlServerEncrypt = dev.typr.foundations.connect.SqlServerEncrypt.TRUE
    val OPTIONAL: SqlServerEncrypt = dev.typr.foundations.connect.SqlServerEncrypt.OPTIONAL
  type SqlServerAuthentication = dev.typr.foundations.connect.SqlServerAuthentication
  object SqlServerAuthentication:
    val SQL_PASSWORD: SqlServerAuthentication = dev.typr.foundations.connect.SqlServerAuthentication.SQL_PASSWORD
    val ACTIVE_DIRECTORY_PASSWORD: SqlServerAuthentication = dev.typr.foundations.connect.SqlServerAuthentication.ACTIVE_DIRECTORY_PASSWORD
    val ACTIVE_DIRECTORY_INTEGRATED: SqlServerAuthentication = dev.typr.foundations.connect.SqlServerAuthentication.ACTIVE_DIRECTORY_INTEGRATED
    val ACTIVE_DIRECTORY_INTERACTIVE: SqlServerAuthentication = dev.typr.foundations.connect.SqlServerAuthentication.ACTIVE_DIRECTORY_INTERACTIVE
    val ACTIVE_DIRECTORY_SERVICE_PRINCIPAL: SqlServerAuthentication = dev.typr.foundations.connect.SqlServerAuthentication.ACTIVE_DIRECTORY_SERVICE_PRINCIPAL
    val ACTIVE_DIRECTORY_MANAGED_IDENTITY: SqlServerAuthentication = dev.typr.foundations.connect.SqlServerAuthentication.ACTIVE_DIRECTORY_MANAGED_IDENTITY
    val ACTIVE_DIRECTORY_DEFAULT: SqlServerAuthentication = dev.typr.foundations.connect.SqlServerAuthentication.ACTIVE_DIRECTORY_DEFAULT
    val NTLM: SqlServerAuthentication = dev.typr.foundations.connect.SqlServerAuthentication.NTLM
    val ACCESS_TOKEN: SqlServerAuthentication = dev.typr.foundations.connect.SqlServerAuthentication.ACCESS_TOKEN
  type SqlServerApplicationIntent = dev.typr.foundations.connect.SqlServerApplicationIntent
  object SqlServerApplicationIntent:
    val READ_WRITE: SqlServerApplicationIntent = dev.typr.foundations.connect.SqlServerApplicationIntent.READ_WRITE
    val READ_ONLY: SqlServerApplicationIntent = dev.typr.foundations.connect.SqlServerApplicationIntent.READ_ONLY
  type SqlServerAuthenticationScheme = dev.typr.foundations.connect.SqlServerAuthenticationScheme
  object SqlServerAuthenticationScheme:
    val NATIVE_AUTHENTICATION: SqlServerAuthenticationScheme = dev.typr.foundations.connect.SqlServerAuthenticationScheme.NATIVE_AUTHENTICATION
    val NTLM: SqlServerAuthenticationScheme = dev.typr.foundations.connect.SqlServerAuthenticationScheme.NTLM
    val KERBEROS: SqlServerAuthenticationScheme = dev.typr.foundations.connect.SqlServerAuthenticationScheme.KERBEROS
    val JAVA_KERBEROS: SqlServerAuthenticationScheme = dev.typr.foundations.connect.SqlServerAuthenticationScheme.JAVA_KERBEROS
  type SqlServerSelectMethod = dev.typr.foundations.connect.SqlServerSelectMethod
  object SqlServerSelectMethod:
    val DIRECT: SqlServerSelectMethod = dev.typr.foundations.connect.SqlServerSelectMethod.DIRECT
    val CURSOR: SqlServerSelectMethod = dev.typr.foundations.connect.SqlServerSelectMethod.CURSOR
  type SqlServerColumnEncryptionSetting = dev.typr.foundations.connect.SqlServerColumnEncryptionSetting
  object SqlServerColumnEncryptionSetting:
    val DISABLED: SqlServerColumnEncryptionSetting = dev.typr.foundations.connect.SqlServerColumnEncryptionSetting.DISABLED
    val ENABLED: SqlServerColumnEncryptionSetting = dev.typr.foundations.connect.SqlServerColumnEncryptionSetting.ENABLED
  type SqlServerResponseBuffering = dev.typr.foundations.connect.SqlServerResponseBuffering
  object SqlServerResponseBuffering:
    val FULL: SqlServerResponseBuffering = dev.typr.foundations.connect.SqlServerResponseBuffering.FULL
    val ADAPTIVE: SqlServerResponseBuffering = dev.typr.foundations.connect.SqlServerResponseBuffering.ADAPTIVE

  // DB2
  type Db2Config = dev.typr.foundations.connect.Db2Config
  object Db2Config:
    def builder(host: String, port: Int, database: String, username: String, password: String): dev.typr.foundations.connect.Db2Config.Builder =
      dev.typr.foundations.connect.Db2Config.builder(host, port, database, username, password)
