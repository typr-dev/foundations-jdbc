package dev.typr.foundationssc

object QueryAnalyzer:
  def analyze(op: Operation[?], conn: java.sql.Connection): List[QueryAnalysis] =
    import scala.jdk.CollectionConverters.*
    dev.typr.foundations.QueryAnalyzer.analyze(op.underlying, conn).asScala.toList

  def analyze(template: SqlTemplate[?, ?], conn: java.sql.Connection): List[QueryAnalysis] =
    import scala.jdk.CollectionConverters.*
    dev.typr.foundations.QueryAnalyzer.analyze(template.underlying, conn).asScala.toList

  def analyze(template: RowSqlTemplate[?, ?], conn: java.sql.Connection): List[QueryAnalysis] =
    import scala.jdk.CollectionConverters.*
    dev.typr.foundations.QueryAnalyzer.analyze(template.underlying, conn).asScala.toList
