package dev.typr.scalafoundations

import java.sql.ResultSet

/** Scala wrapper for dev.typr.foundations.ResultSetParser that provides Scala-native methods.
  *
  * Wraps the Java ResultSetParser to provide interop with Java APIs.
  */
class ResultSetParser[Out](val underlying: dev.typr.foundations.ResultSetParser[Out]) {
  def apply(rs: ResultSet): Out = underlying.apply(rs)
}

/** Convert a Java ResultSetParser to a Scala ResultSetParser.
  */
extension [Out](javaParser: dev.typr.foundations.ResultSetParser[Out]) {
  def asScalaResultSetParser: ResultSetParser[Out] = new ResultSetParser(javaParser)
}
