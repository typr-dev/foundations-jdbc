package dev.typr.foundationssc

type PgText[T] = dev.typr.foundations.PgText[T]

object PgText:
  def from[A](rowParser: RowParser[A]): dev.typr.foundations.PgText[A] =
    dev.typr.foundations.PgText.from(rowParser.underlying)
  def from[A](rowParser: dev.typr.foundations.RowParser[A]): dev.typr.foundations.PgText[A] =
    dev.typr.foundations.PgText.from(rowParser)
  def instance[A](f: java.util.function.BiConsumer[A, java.lang.StringBuilder]): dev.typr.foundations.PgText[A] =
    dev.typr.foundations.PgText.instance(f)
