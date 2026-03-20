package dev.typr.foundationssc

object QueryAnalyzer:
  def analyze(analyzable: Analyzable, conn: java.sql.Connection): List[QueryAnalysis] =
    import scala.jdk.CollectionConverters.*
    dev.typr.foundations.QueryAnalyzer.analyze(analyzable.analyzable, conn).asScala.toList

  def analyze(op: Operation[?], conn: java.sql.Connection): List[QueryAnalysis] =
    import scala.jdk.CollectionConverters.*
    dev.typr.foundations.QueryAnalyzer.analyze(op.underlying, conn).asScala.toList

  def analyze(template: Template[?, ?], conn: java.sql.Connection): List[QueryAnalysis] =
    import scala.jdk.CollectionConverters.*
    dev.typr.foundations.QueryAnalyzer.analyze(template.underlying, conn).asScala.toList
