package dev.typr.foundationssc

object QueryAnalyzer:
  def analyze(op: Operation[?], conn: java.sql.Connection): List[QueryAnalysis] =
    import scala.jdk.CollectionConverters.*
    dev.typr.foundations.QueryAnalyzer.analyze(op.underlying, conn).asScala.toList

  def analyze(name: String, op: Operation[?], conn: java.sql.Connection): List[QueryAnalysis] =
    import scala.jdk.CollectionConverters.*
    dev.typr.foundations.QueryAnalyzer.analyze(name, op.underlying, conn).asScala.toList
