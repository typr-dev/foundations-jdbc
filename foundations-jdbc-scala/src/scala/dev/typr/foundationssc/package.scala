package dev.typr

// NOTE: Using type aliases because Gradle's Zinc doesn't handle Scala 3 exports.
// See: https://github.com/gradle/gradle/issues/29286
// For Java classes with static methods, use fully qualified names (e.g., Fragment.of).
package object foundationssc:
  // Core types
  type And[A, B] = (A, B)
  type SqlFunction[T, R] = dev.typr.foundations.SqlFunction[T, R]
  type DbJson[T] = dev.typr.foundations.DbJson[T]

  // Analysis types
  type QueryAnalysis = dev.typr.foundations.QueryAnalysis

  // Connection types
  type OracleConfig = dev.typr.foundations.connect.OracleConfig
  object OracleConfig:
    def builder(host: String, port: Int, database: String, username: String, password: String): dev.typr.foundations.connect.OracleConfig.Builder =
      dev.typr.foundations.connect.OracleConfig.builder(host, port, database, username, password)
  type PostgresConfig = dev.typr.foundations.connect.PostgresConfig
  object PostgresConfig:
    def builder(host: String, port: Int, database: String, username: String, password: String): dev.typr.foundations.connect.PostgresConfig.Builder =
      dev.typr.foundations.connect.PostgresConfig.builder(host, port, database, username, password)
  type MariaDbConfig = dev.typr.foundations.connect.MariaDbConfig
  object MariaDbConfig:
    def builder(host: String, port: Int, database: String, username: String, password: String): dev.typr.foundations.connect.MariaDbConfig.Builder =
      dev.typr.foundations.connect.MariaDbConfig.builder(host, port, database, username, password)
  type DuckDbConfig = dev.typr.foundations.connect.DuckDbConfig
  object DuckDbConfig:
    def builder(path: String): dev.typr.foundations.connect.DuckDbConfig.Builder =
      dev.typr.foundations.connect.DuckDbConfig.builder(path)
    def inMemory(): dev.typr.foundations.connect.DuckDbConfig.Builder =
      dev.typr.foundations.connect.DuckDbConfig.inMemory()
  type SqlServerConfig = dev.typr.foundations.connect.SqlServerConfig
  object SqlServerConfig:
    def builder(host: String, port: Int, database: String, username: String, password: String): dev.typr.foundations.connect.SqlServerConfig.Builder =
      dev.typr.foundations.connect.SqlServerConfig.builder(host, port, database, username, password)
  type Db2Config = dev.typr.foundations.connect.Db2Config
  object Db2Config:
    def builder(host: String, port: Int, database: String, username: String, password: String): dev.typr.foundations.connect.Db2Config.Builder =
      dev.typr.foundations.connect.Db2Config.builder(host, port, database, username, password)

  // Extension methods for Scala-friendly DbJson combinators
  extension [A](codec: dev.typr.foundations.DbJson[A])
    /** Create a list codec that uses Scala List instead of java.util.List. */
    def asList: dev.typr.foundations.DbJson[List[A]] =
      import _root_.scala.jdk.CollectionConverters.*
      val javaListCodec = codec.list()
      new dev.typr.foundations.DbJson[List[A]]:
        override def toJson(value: List[A]): dev.typr.foundations.data.JsonValue =
          javaListCodec.toJson(value.asJava)
        override def fromJson(json: dev.typr.foundations.data.JsonValue): List[A] =
          javaListCodec.fromJson(json).asScala.toList

    /** Create an optional codec that uses Scala Option instead of java.util.Optional. */
    def asOption: dev.typr.foundations.DbJson[Option[A]] =
      val javaOptCodec = codec.opt()
      new dev.typr.foundations.DbJson[Option[A]]:
        override def toJson(value: Option[A]): dev.typr.foundations.data.JsonValue =
          javaOptCodec.toJson(value.fold(java.util.Optional.empty[A]())(java.util.Optional.of))
        override def fromJson(json: dev.typr.foundations.data.JsonValue): Option[A] =
          val opt = javaOptCodec.fromJson(json)
          if opt.isPresent then Some(opt.get) else None
