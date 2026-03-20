package dev.typr.foundationssc

object StreamingInsert:
  import _root_.scala.jdk.CollectionConverters.*
  def of[T](copyCommand: String, batchSize: Int, rows: Iterator[T], text: dev.typr.foundations.PgText[T]): Operation.StreamingCopy =
    new Operation.StreamingCopy(dev.typr.foundations.StreamingInsert.of(copyCommand, batchSize, rows.asJava, text))
  def insert[T](copyCommand: String, batchSize: Int, rows: Iterator[T], c: java.sql.Connection, t: dev.typr.foundations.PgText[T]): Long =
    dev.typr.foundations.StreamingInsert.insert(copyCommand, batchSize, rows.asJava, c, t)
  def insertUnchecked[T](copyCommand: String, batchSize: Int, rows: Iterator[T], c: java.sql.Connection, t: dev.typr.foundations.PgText[T]): Long =
    dev.typr.foundations.StreamingInsert.insertUnchecked(copyCommand, batchSize, rows.asJava, c, t)
