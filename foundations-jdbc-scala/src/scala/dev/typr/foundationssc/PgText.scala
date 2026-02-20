package dev.typr.foundationssc

type PgText[T] = dev.typr.foundations.PgText[T]

object PgText:
  def from[A](rowCodec: RowCodec[A]): dev.typr.foundations.PgText[A] =
    dev.typr.foundations.PgText.from(rowCodec.underlying)
  def from[A](rowCodec: dev.typr.foundations.RowCodec[A]): dev.typr.foundations.PgText[A] =
    dev.typr.foundations.PgText.from(rowCodec)
  def instance[A](f: java.util.function.BiConsumer[A, java.lang.StringBuilder]): dev.typr.foundations.PgText[A] =
    dev.typr.foundations.PgText.instance(f)
