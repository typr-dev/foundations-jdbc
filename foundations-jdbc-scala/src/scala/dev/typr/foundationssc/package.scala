package dev.typr

// NOTE: Using type aliases because Gradle's Zinc doesn't handle Scala 3 exports.
// See: https://github.com/gradle/gradle/issues/29286
// For Java classes with static methods, use fully qualified names (e.g., Fragment.of).
package object foundationssc:
  // Core types
  type SqlFunction[T, R] = dev.typr.foundations.SqlFunction[T, R]
  type DbJson[T] = dev.typr.foundations.DbJson[T]

  // Analysis types
  type QueryAnalysis = dev.typr.foundations.QueryAnalysis
  type QueryListener = dev.typr.foundations.QueryListener
  type QueryEvent = dev.typr.foundations.QueryEvent

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
