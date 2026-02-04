package dev.typr

// NOTE: Using type aliases because Gradle's Zinc doesn't handle Scala 3 exports.
// See: https://github.com/gradle/gradle/issues/29286
// For Java classes with static methods, use fully qualified names (e.g., Fragment.interpolate).
package object scalafoundations:
  // Core types
  type DbType[T] = dev.typr.foundations.DbType[T]
  // Fragment is defined as a class in Fragment.scala
  // Operation is defined as a sealed trait in Operation.scala
  type And[A, B] = dev.typr.foundations.And[A, B]
  type Transactor = dev.typr.foundations.Transactor

  // Wrapper object for Transactor static methods
  object Transactor:
    type Strategy = dev.typr.foundations.Transactor.Strategy
    def defaultStrategy(): Strategy = dev.typr.foundations.Transactor.defaultStrategy()
    def autoCommitStrategy(): Strategy = dev.typr.foundations.Transactor.autoCommitStrategy()
    def rollbackOnErrorStrategy(): Strategy = dev.typr.foundations.Transactor.rollbackOnErrorStrategy()
    def testStrategy(): Strategy = dev.typr.foundations.Transactor.testStrategy()

  type SqlFunction[T, R] = dev.typr.foundations.SqlFunction[T, R]
  // Operation is defined as a sealed trait in Operation.scala
  type NonEmptyBlob = dev.typr.foundations.NonEmptyBlob
  type NonEmptyString = dev.typr.foundations.NonEmptyString
  type PaddedString = dev.typr.foundations.PaddedString
  type PgStruct[A] = dev.typr.foundations.PgStruct[A]
  object PgStruct:
    def builder[A](typeName: String): dev.typr.foundations.PgStructBuilders.Builder0[A] =
      dev.typr.foundations.PgStruct.builder(typeName)
  type DuckDbStruct[A] = dev.typr.foundations.DuckDbStruct[A]
  object DuckDbStruct:
    def builder[A](structName: String): dev.typr.foundations.DuckDbStructBuilders.Builder0[A] =
      dev.typr.foundations.DuckDbStruct.builder(structName)
  type OracleObject[A] = dev.typr.foundations.OracleObject[A]
  object OracleObject:
    def builder[A](objectTypeName: String): dev.typr.foundations.OracleObjectBuilders.Builder0[A] =
      dev.typr.foundations.OracleObject.builder(objectTypeName)

  // Database-specific type classes
  type PgType[T] = dev.typr.foundations.PgType[T]
  type PgOptType[T] = dev.typr.foundations.PgOptType[T]
  type MariaType[T] = dev.typr.foundations.MariaType[T]
  type DuckDbType[T] = dev.typr.foundations.DuckDbType[T]
  type DuckDbOptType[T] = dev.typr.foundations.DuckDbOptType[T]
  type OracleType[T] = dev.typr.foundations.OracleType[T]
  type SqlServerType[T] = dev.typr.foundations.SqlServerType[T]
  type Db2Type[T] = dev.typr.foundations.Db2Type[T]

  // Analysis types
  type QueryAnalysis = dev.typr.foundations.analysis.QueryAnalysis

  // Wrapper object for QueryAnalyzer static methods
  object QueryAnalyzer:
    // Java Operation overloads
    def analyze[T](query: dev.typr.foundations.Operation.Query[T], conn: java.sql.Connection): QueryAnalysis =
      dev.typr.foundations.analysis.QueryAnalyzer.analyze(query, conn)
    def analyze(update: dev.typr.foundations.Operation.Update, conn: java.sql.Connection): QueryAnalysis =
      dev.typr.foundations.analysis.QueryAnalyzer.analyze(update, conn)
    def analyze[T](op: dev.typr.foundations.Operation.UpdateReturning[T], conn: java.sql.Connection): QueryAnalysis =
      dev.typr.foundations.analysis.QueryAnalyzer.analyze(op, conn)
    // Scala Operation overloads
    def analyze[T](query: Operation.Query[T], conn: java.sql.Connection): QueryAnalysis =
      dev.typr.foundations.analysis.QueryAnalyzer.analyze(query.underlying, conn)
    def analyze(update: Operation.Update, conn: java.sql.Connection): QueryAnalysis =
      dev.typr.foundations.analysis.QueryAnalyzer.analyze(update.underlying, conn)
    def analyze[T](op: Operation.UpdateReturning[T], conn: java.sql.Connection): QueryAnalysis =
      dev.typr.foundations.analysis.QueryAnalyzer.analyze(op.underlying, conn)

  // Connection types
  type ConnectionSource = dev.typr.foundations.connect.ConnectionSource
  type OracleConfig = dev.typr.foundations.connect.oracle.OracleConfig
  object OracleConfig:
    def builder(host: String, port: Int, database: String, username: String, password: String): dev.typr.foundations.connect.oracle.OracleConfig.Builder =
      dev.typr.foundations.connect.oracle.OracleConfig.builder(host, port, database, username, password)
  type PostgresConfig = dev.typr.foundations.connect.postgres.PostgresConfig
  object PostgresConfig:
    def builder(host: String, port: Int, database: String, username: String, password: String): dev.typr.foundations.connect.postgres.PostgresConfig.Builder =
      dev.typr.foundations.connect.postgres.PostgresConfig.builder(host, port, database, username, password)
  type MariaDbConfig = dev.typr.foundations.connect.mariadb.MariaDbConfig
  object MariaDbConfig:
    def builder(host: String, port: Int, database: String, username: String, password: String): dev.typr.foundations.connect.mariadb.MariaDbConfig.Builder =
      dev.typr.foundations.connect.mariadb.MariaDbConfig.builder(host, port, database, username, password)
  type DuckDbConfig = dev.typr.foundations.connect.duckdb.DuckDbConfig
  object DuckDbConfig:
    def builder(path: String): dev.typr.foundations.connect.duckdb.DuckDbConfig.Builder =
      dev.typr.foundations.connect.duckdb.DuckDbConfig.builder(path)
  type SqlServerConfig = dev.typr.foundations.connect.sqlserver.SqlServerConfig
  object SqlServerConfig:
    def builder(host: String, port: Int, database: String, username: String, password: String): dev.typr.foundations.connect.sqlserver.SqlServerConfig.Builder =
      dev.typr.foundations.connect.sqlserver.SqlServerConfig.builder(host, port, database, username, password)
  type Db2Config = dev.typr.foundations.connect.db2.Db2Config
  object Db2Config:
    def builder(host: String, port: Int, database: String, username: String, password: String): dev.typr.foundations.connect.db2.Db2Config.Builder =
      dev.typr.foundations.connect.db2.Db2Config.builder(host, port, database, username, password)

  // Bijection for converting Java Optional to Scala Option
  private def optionalToOptionBijection[T]: dev.typr.foundations.dsl.Bijection[java.util.Optional[T], Option[T]] =
    dev.typr.foundations.dsl.Bijection.of(
      (opt: java.util.Optional[T]) => if opt.isPresent then Some(opt.get) else None,
      (opt: Option[T]) => opt.fold(java.util.Optional.empty[T]())(java.util.Optional.of)
    )

  // Extension methods for Scala-friendly nullable() that returns Option instead of Optional
  // Named "nullable" to avoid conflict with Java opt() method
  // PgType.opt() returns PgOptType; to(Bijection) returns DbType (implementation returns PgType)
  extension [T](dbType: dev.typr.foundations.PgType[T])
    def nullable: dev.typr.foundations.DbType[Option[T]] =
      dbType.opt().to(optionalToOptionBijection)

  extension [T](dbType: dev.typr.foundations.MariaType[T])
    def nullable: dev.typr.foundations.MariaType[Option[T]] =
      dbType.opt().bimap(opt => if opt.isPresent then Some(opt.get) else None, _.fold(java.util.Optional.empty[T]())(java.util.Optional.of))

  // DuckDbType.opt() returns DuckDbOptType; to(Bijection) returns DbType (implementation returns DuckDbType)
  extension [T](dbType: dev.typr.foundations.DuckDbType[T])
    def nullable: dev.typr.foundations.DbType[Option[T]] =
      dbType.opt().to(optionalToOptionBijection)

  extension [T](dbType: dev.typr.foundations.OracleType[T])
    def nullable: dev.typr.foundations.OracleType[Option[T]] =
      dbType.opt().bimap(opt => if opt.isPresent then Some(opt.get) else None, _.fold(java.util.Optional.empty[T]())(java.util.Optional.of))

  extension [T](dbType: dev.typr.foundations.SqlServerType[T])
    def nullable: dev.typr.foundations.SqlServerType[Option[T]] =
      dbType.opt().bimap(opt => if opt.isPresent then Some(opt.get) else None, _.fold(java.util.Optional.empty[T]())(java.util.Optional.of))

  extension [T](dbType: dev.typr.foundations.Db2Type[T])
    def nullable: dev.typr.foundations.Db2Type[Option[T]] =
      dbType.opt().bimap(opt => if opt.isPresent then Some(opt.get) else None, _.fold(java.util.Optional.empty[T]())(java.util.Optional.of))
