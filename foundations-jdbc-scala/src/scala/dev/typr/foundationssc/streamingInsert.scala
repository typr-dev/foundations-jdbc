package dev.typr.scalafoundations

object streamingInsert:
  import _root_.scala.jdk.CollectionConverters.*
  def of[T](copyCommand: String, batchSize: Int, rows: Iterator[T], text: dev.typr.foundations.PgText[T]): Operation.StreamingCopy =
    new Operation.StreamingCopy(dev.typr.foundations.streamingInsert.of(copyCommand, batchSize, rows.asJava, text))
  def insert[T](copyCommand: String, batchSize: Int, rows: Iterator[T], c: java.sql.Connection, t: dev.typr.foundations.PgText[T]): Long =
    dev.typr.foundations.streamingInsert.insert(copyCommand, batchSize, rows.asJava, c, t)
  def insertUnchecked[T](copyCommand: String, batchSize: Int, rows: Iterator[T], c: java.sql.Connection, t: dev.typr.foundations.PgText[T]): Long =
    dev.typr.foundations.streamingInsert.insertUnchecked(copyCommand, batchSize, rows.asJava, c, t)
