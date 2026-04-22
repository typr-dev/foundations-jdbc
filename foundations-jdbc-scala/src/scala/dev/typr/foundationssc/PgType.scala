package dev.typr.foundationssc

class PgType[T](override val underlying: dev.typr.foundations.PgType[T]) extends DbType[T](underlying):
  override def opt: PgType[Option[T]] =
    PgType(underlying.opt().to(Bijections.optionalToOption))

  override def to[B](bijection: Bijection[T, B]): PgType[B] =
    PgType(underlying.to(bijection))

  def transform[B](f: T => B, g: B => T): PgType[B] =
    PgType(underlying.transform(v => f(v), v => g(v)))

  /** Variable-length PG array of this type — Scala-side as immutable {@code List[T]}. */
  def array: PgType[List[T]] = PgType(
    underlying
      .array()
      .transform(
        jlist => scala.jdk.CollectionConverters.ListHasAsScala(jlist).asScala.toList,
        slist => {
          val al = new java.util.ArrayList[T](slist.size)
          slist.foreach(al.add)
          al
        }
      )
  )

  def encode(value: T): Fragment = new Fragment(underlying.encode(value))

  def pgText(): PgText[T] = underlying.pgText()

  def withTypename(typename: PgTypename[T]): PgType[T] = PgType(underlying.withTypename(typename))
  def withTypename(sqlType: String): PgType[T] = PgType(underlying.withTypename(sqlType))
  def renamed(value: String): PgType[T] = PgType(underlying.renamed(value))
  def renamedDropPrecision(value: String): PgType[T] = PgType(underlying.renamedDropPrecision(value))

  def withRead(read: PgRead[T]): PgType[T] = PgType(underlying.withRead(read))
  def withWrite(write: PgWrite[T]): PgType[T] = PgType(underlying.withWrite(write))
  def withText(text: PgText[T]): PgType[T] = PgType(underlying.withText(text))
  def withJson(json: PgJson[T]): PgType[T] = PgType(underlying.withJson(json))
  def withOutParam(outParam: PgOutParam[T]): PgType[T] = PgType(underlying.withOutParam(outParam))
  def withArrayCodec(codec: PgElementCodec[T]): PgType[T] = PgType(underlying.withArrayCodec(codec))
  def withAnalysis(opts: AnalysisOptions): PgType[T] = PgType(underlying.withAnalysis(opts))

  def unchecked(): PgType[T] = PgType(underlying.unchecked())
  def nullableOk(): PgType[T] = PgType(underlying.nullableOk())
