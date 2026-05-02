package dev.typr

// NOTE: Using type aliases because Gradle's Zinc doesn't handle Scala 3 exports.
// See: https://github.com/gradle/gradle/issues/29286
// For Java classes with static methods, use fully qualified names (e.g., Fragment.of).
package object foundationssc:
  // Functional interfaces
  type SqlFunction[T, R] = dev.typr.foundations.SqlFunction[T, R]
  type SqlConsumer[T] = dev.typr.foundations.SqlConsumer[T]
  type SqlSupplier[T] = dev.typr.foundations.SqlSupplier[T]
  type SqlBiConsumer[T, U] = dev.typr.foundations.SqlBiConsumer[T, U]
  type SqlBiFunction[T, U, R] = dev.typr.foundations.SqlBiFunction[T, U, R]

  // Analysis types
  type QueryAnalysis = dev.typr.foundations.QueryAnalysis
  type CheckReport = dev.typr.foundations.CheckReport
  type QueryListener = dev.typr.foundations.QueryListener
  type QueryEvent = dev.typr.foundations.QueryEvent
  type TransactionEvent = dev.typr.foundations.TransactionEvent
  type ConnectionRead = dev.typr.foundations.ConnectionRead
  type Connection = dev.typr.foundations.Connection

  // Codec internals (needed for struct/array construction)
  type DbJson[T] = dev.typr.foundations.DbJson[T]
  type PgJson[T] = dev.typr.foundations.PgJson[T]
  type DuckDbJson[T] = dev.typr.foundations.DuckDbJson[T]
  type DbJsonRow = dev.typr.foundations.DbJsonRow
  type PgRead[T] = dev.typr.foundations.PgRead[T]
  type PgWrite[T] = dev.typr.foundations.PgWrite[T]
  type PgCompositeText[T] = dev.typr.foundations.PgCompositeText[T]
  type PgElementCodec[T] = dev.typr.foundations.PgElementCodec[T]
  type PgOutParam[T] = dev.typr.foundations.PgOutParam[T]
  type PgTypename[T] = dev.typr.foundations.PgTypename[T]
  type AnalysisOptions = dev.typr.foundations.AnalysisOptions
  type DuckDbRead[T] = dev.typr.foundations.DuckDbRead[T]
  type DuckDbWrite[T] = dev.typr.foundations.DuckDbWrite[T]
  type DuckDbStringifier[T] = dev.typr.foundations.DuckDbStringifier[T]
  type DuckDbTypename[T] = dev.typr.foundations.DuckDbTypename[T]
  type DuckDbListCodec[T] = dev.typr.foundations.DuckDbListCodec[T]
  type SqliteJson[T] = dev.typr.foundations.SqliteJson[T]
  type SqliteRead[T] = dev.typr.foundations.SqliteRead[T]
  type SqliteWrite[T] = dev.typr.foundations.SqliteWrite[T]
  type SqliteTypename[T] = dev.typr.foundations.SqliteTypename[T]

  // Exceptions
  type DatabaseException = dev.typr.foundations.DatabaseException

  // Bijection (used in DbType.to signatures)
  type Bijection[A, B] = dev.typr.foundations.Bijection[A, B]

  // Scanner results
  type AnalyzableScannerResult = dev.typr.foundations.AnalyzableScanner.Result
  type ScanDirective = dev.typr.foundations.ScanDirective
  type RoutineDef = dev.typr.foundations.RoutineDef
  type Procedure[T] = dev.typr.foundations.Procedure[T]

  // Oracle collection wrappers — return Scala OracleType[List[T]], not Java OracleType[java.util.List[T]]
  object OracleVArray:
    def of[T](typeName: String, maxSize: Int, elementType: OracleType[T]): OracleType[List[T]] =
      import _root_.scala.jdk.CollectionConverters.*
      OracleType(
        dev.typr.foundations.OracleVArray
          .of(typeName, maxSize, elementType.underlying)
          .transform(jl => jl.asScala.toList, sl => { val al = new java.util.ArrayList[T](sl.size); sl.foreach(al.add); al })
      )

  object OracleNestedTable:
    def of[T](typeName: String, elementType: OracleType[T]): OracleType[List[T]] =
      import _root_.scala.jdk.CollectionConverters.*
      OracleType(
        dev.typr.foundations.OracleNestedTable
          .of(typeName, elementType.underlying)
          .transform(jl => jl.asScala.toList, sl => { val al = new java.util.ArrayList[T](sl.size); sl.foreach(al.add); al })
      )

  // Extension methods for Scala-friendly DbJson combinators
  extension [A](codec: DbJson[A])
    /** Create a list codec that uses Scala List instead of java.util.List. */
    def asList: DbJson[List[A]] =
      import _root_.scala.jdk.CollectionConverters.*
      val javaListCodec = codec.list()
      new dev.typr.foundations.DbJson[List[A]]:
        override def toJson(value: List[A]): dev.typr.foundations.data.JsonValue =
          javaListCodec.toJson(value.asJava)
        override def fromJson(json: dev.typr.foundations.data.JsonValue): List[A] =
          javaListCodec.fromJson(json).asScala.toList

    /** Create an optional codec that uses Scala Option instead of java.util.Optional. */
    def asOption: DbJson[Option[A]] =
      val javaOptCodec = codec.opt()
      new dev.typr.foundations.DbJson[Option[A]]:
        override def toJson(value: Option[A]): dev.typr.foundations.data.JsonValue =
          javaOptCodec.toJson(value.fold(java.util.Optional.empty[A]())(java.util.Optional.of))
        override def fromJson(json: dev.typr.foundations.data.JsonValue): Option[A] =
          val opt = javaOptCodec.fromJson(json)
          if opt.isPresent then Some(opt.get) else None
